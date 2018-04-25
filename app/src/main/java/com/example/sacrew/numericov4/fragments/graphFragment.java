package com.example.sacrew.numericov4.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.graphParallel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udojava.evalex.Expression;




import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.markushi.ui.CircleButton;




/**
 * A simple {@link Fragment} subclass.
 */
public class graphFragment extends Fragment {

    private LinearLayout linearLayout;
    private GraphView graph ;
    public static List<LineGraphSeries<DataPoint>> listSeries;
    private LinearLayout parentLinearLayout;
    private RelativeLayout hiderB;
    private LinearLayout hiderA;
    private boolean isup = true;
    private Map<Integer, List<LineGraphSeries<DataPoint>>> viewToFunction;
    private Map<Integer, Integer> viewToColor = new HashMap<>();
    private int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private Thread[] cores = new Thread[NUMBER_OF_CORES];
    private double scale = 0.1; //escala de desplazamiento
    private View view;
    private CircleButton addFieldButton,deleteFieldButton,graphButton;
    private Button hider;
    private List <Integer> colors = new LinkedList<>();
    static public List <String> allFunctions = new LinkedList<>();


    public graphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        //define colors
        colors.add(Color.parseColor("#FF6D24"));
        colors.add(Color.parseColor("#00204A"));
        colors.add(Color.parseColor("#248888"));
        colors.add(Color.parseColor("#FD2E2E"));
        colors.add(Color.parseColor("#096C47"));
        colors.add(Color.parseColor("#BB0029"));
        colors.add(Color.parseColor("#4A772F"));
        colors.add(Color.parseColor("#F54D42"));
        colors.add(Color.parseColor("#682666"));

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graph,container,false);

        linearLayout = view.findViewById(R.id.mainLayout);


        parentLinearLayout = (LinearLayout) view.findViewById(R.id.parent_linear_layout);

        hiderA = view.findViewById(R.id.hiderA);
        hiderB = view.findViewById(R.id.hiderB);
        graph = view.findViewById(R.id.graph);
        viewToFunction = new HashMap<>();
        //poop
        addFieldButton = view.findViewById(R.id.add_field_button);
        onAddField(null);
        deleteFieldButton = view.findViewById(R.id.delete_button);
        graphButton = view.findViewById(R.id.graph_button);
        hider = view.findViewById(R.id.buttonHide);
        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddField(view);
            }
        });
        deleteFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDelete(view);
            }
        });
        graphButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                graphIt(view);
            }
        });
        hider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide(view);
            }
        });

        return view;


    }


    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);

        // Add the new field before the add field button.



        parentLinearLayout.addView(rowView, 0);
        AutoCompleteTextView edittext_var = ((View) rowView.getParent()).findViewById(R.id.function_edit_text);
        edittext_var.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, allFunctions));
    }
    public void hide(View v){
        if(isup) { // down
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    hiderB.getHeight()); // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            hiderB.startAnimation(animate);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hiderA = view.findViewById(R.id.hiderA);
                            graph = view.findViewById(R.id.graph);
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0.05f
                            );
                            hiderA.setLayoutParams(param);
                        }
                    }, 200);
                }
            });
            //v.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);
            isup = false;
        }else{ //up
            hiderA = view.findViewById(R.id.hiderA);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.5f
            );
            hiderA.setLayoutParams(param);
            hiderB.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    hiderB.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            hiderB.startAnimation(animate);
            //v.setBackgroundResource(R.drawable.ic_arrow_downward_black_24dp);
            isup = true;
        }


    }

    public void onDelete(View v) {
        int code = ((View) v.getParent()).findViewById(R.id.function_edit_text).hashCode();
        try {
            for (LineGraphSeries<DataPoint> inSerie : viewToFunction.get(code)) {
                graph.removeSeries(inSerie);
            }
        }catch (Exception ignored){

        }
        viewToFunction.remove(code);
        viewToColor.remove(code);
        parentLinearLayout.removeView((View) v.getParent());
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CutPasteId")
    public void graphIt(View v){
        double start = -40D;
        try {
            AutoCompleteTextView edittext_var;
            edittext_var = ((View) v.getParent()).findViewById(R.id.function_edit_text);
            SeekBar seek = ((SeekBar)((View) v.getParent()).findViewById(R.id.seek));
            int iter = seek.getProgress();
            String function = String.valueOf(edittext_var.getText());
            String functionAux = function;
            if(function.length() !=0)
                function = function.toLowerCase();

            Expression exp = new Expression(function);
            //throw expresion, values not be negative
            if(function.contains("sqrt"))
                start = 0.001D;
            else if(function.contains(("e"))){
                this.scale = 0.1D;
            }else if(function.contains(("ln"))) {
                function = function.replace("ln",""+Math.E+"*log10");
            } else {
                try {
                    //evaluateFunction(function,-1);
                    (exp.with("x", BigDecimal.valueOf(-1)).eval()).doubleValue();
                    start = -1 * iter * 0.1;
                    iter = iter * 2;
                    System.out.println(iter + " num " + iter * 0.1 + " start: " + start);
                } catch (java.lang.NumberFormatException e) {
                    start = 0.001D;
                }
            }

            Toast.makeText(getActivity(), function, Toast.LENGTH_SHORT).show();

            //Expression expression = new Expression(function);

            listSeries = new LinkedList<LineGraphSeries<DataPoint>>();
            int code = ((View) v.getParent()).findViewById(R.id.function_edit_text).hashCode();
            // define color
            if(!viewToColor.containsKey(code)){
                int color = colors.remove(0);
                colors.add(color);
                viewToColor.put(code,color);
            }
            seek.setProgressTintList(ColorStateList.valueOf(viewToColor.get(code)));
            /*
             * autocomplete allFunctions
             */
            if(!allFunctions.contains(functionAux)) {
                allFunctions.add(functionAux);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, allFunctions);
                edittext_var.setAdapter(adapter);
            }
            graphParalelism(function, iter, start, code);
            if(viewToFunction.containsKey(code)){
                for (LineGraphSeries<DataPoint> inSerie  : viewToFunction.get(code)) {
                    graph.removeSeries(inSerie);
                }
            }
            viewToFunction.put(code,listSeries);

            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(-50);
            graph.getViewport().setMaxY(50);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(-50);
            graph.getViewport().setMaxX(50);
            graph.getViewport().setScrollable(true); // enables horizontal scrolling
            graph.getViewport().setScrollableY(true); // enables vertical scrolling
            graph.getViewport().setScalable(true);// esto genera errores se podria solucionar pero
            graph.getViewport().setScalableY(true);// es complejo, es para el zoom

            for (LineGraphSeries<DataPoint> inSerie  : viewToFunction.get(code)) {
                graph.addSeries(inSerie);

            }



        }catch (Exception e){
            System.out.println(e);
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void graphParalelism(String function,int iter,double x,int code){
        int end = (int)Math.ceil(iter / NUMBER_OF_CORES);

        for(int i = 0; i < cores.length;i++){

            cores[i]=new Thread(new graphParallel(x,end,function,viewToColor.get(code)));
            cores[i].start();

            x = x + (end*this.scale) - this.scale;
        }
        for (Thread core : cores) {
            try {
                core.join();
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }


}
