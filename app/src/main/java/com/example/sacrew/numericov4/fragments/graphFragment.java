package com.example.sacrew.numericov4.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;


import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.utils.FunctionStorage;
import com.example.sacrew.numericov4.utils.KeyboardUtils;
import com.example.sacrew.numericov4.utils.graphUtils;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udojava.evalex.Expression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.markushi.ui.CircleButton;

import static com.example.sacrew.numericov4.fragments.homeFragment.poolColors;





/**
 * A simple {@link Fragment} subclass.
 */
public class graphFragment extends Fragment {

    private GraphView graph ;
    private LinearLayout parentLinearLayout;
    private RelativeLayout hiderB;
    private LinearLayout hiderA;
    private boolean isup = true;
    private Map<Integer, List<LineGraphSeries<DataPoint>>> viewToFunction;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, Integer> viewToColor = new HashMap<>();
    private View view;
    private graphUtils graphUtils = new graphUtils();
    public KeyboardUtils keyboardUtils;
    public File temp;
    public FunctionStorage functionStorage;


    public graphFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //clear toasts
        SuperActivityToast.cancelAllSuperToasts();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graph,container,false);
        isup = true;
        parentLinearLayout = (LinearLayout) view.findViewById(R.id.parent_linear_layout);

        hiderA = view.findViewById(R.id.hiderA);
        hiderB = view.findViewById(R.id.hiderB);
        graph = view.findViewById(R.id.graph);
        viewToFunction = new HashMap<>();

        CircleButton addFieldButton = view.findViewById(R.id.add_field_button);

        keyboardUtils = new KeyboardUtils(view,R.id.keyboardView,getContext());
        onAddField(null);
        CircleButton deleteFieldButton = view.findViewById(R.id.delete_button);
        CircleButton graphButton = view.findViewById(R.id.graph_button);
        Button hider = view.findViewById(R.id.buttonHide);


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


        ImageButton homeGraph = view.findViewById(R.id.homeGraphButton);
        final List<LineGraphSeries<DataPoint>> listSeries = graphUtils.graphPharallel(200, "x", 0);
        homeGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (LineGraphSeries<DataPoint> inSerie : listSeries)
                    graph.removeSeries(inSerie);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(-50);
                graph.getViewport().setMaxY(50);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(-50);
                graph.getViewport().setMaxX(50);
                graph.getViewport().setScrollable(true);
                graph.getViewport().setScrollableY(true);
                graph.getViewport().setScalable(true);
                graph.getViewport().setScalableY(true);
                for (LineGraphSeries<DataPoint> inSerie : listSeries)
                    graph.addSeries(inSerie);
            }
        });
        for (LineGraphSeries<DataPoint> inSerie : listSeries)
            graph.addSeries(inSerie);
        for (LineGraphSeries<DataPoint> inSerie : graphUtils.graphPharallel(200, "x", 0))
            graph.addSeries(inSerie);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        for(int i = 0; i <= 8000; i++)System.out.println(":I");
                        return true;
                    }
                }
                return false;
            }
        });
        LinearLayout linear = view.findViewById(R.id.linear);
        view.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("height " + linear.getWidth());
                System.out.println("mesured " + linear.getMeasuredHeight());
                ViewGroup.LayoutParams params = linear.getLayoutParams();
                params.height = linear.getMeasuredHeight() - 1;
                linear.setLayoutParams(params);
            }
        });
        if(!functionStorage.functions.isEmpty())
        for(String function: functionStorage.functions)keyboardUtils.addFunction(function,getContext(),functionStorage,temp);
        return view;


    }



    public void onAddField(View v) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);

        // Add the new field before the add field button.

        parentLinearLayout.addView(rowView, 0);
        EditText edittext_var = ((View) rowView.getParent()).findViewById(R.id.function_edit_text);
        keyboardUtils.registerEdittext(edittext_var,getContext(),getActivity());
        //registerEditText(edittext_var);

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
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CutPasteId")
    public void graphIt(View v){
        boolean error = true;
        try {
            EditText edittext_var;
            edittext_var = ((View) v.getParent()).findViewById(R.id.function_edit_text);
            SeekBar seek = ((SeekBar)((View) v.getParent()).findViewById(R.id.seek));
            int iter = seek.getProgress();
            String function = graphUtils.functionRevision(String.valueOf(edittext_var.getText()));
            if(function.length() !=0)
                function = function.toLowerCase();


            if(!checkSyntax(function)) {
                styleWrongMessage("Invalid function");
                return;
            }

            if(!functionStorage.functions.contains(function)) {
                functionStorage.functions.add(function);
                keyboardUtils.addFunction(function, getContext(), functionStorage,temp);
                functionStorage.updateStorage(temp);
            }
            int code = ((View) v.getParent()).findViewById(R.id.function_edit_text).hashCode();
            // define color
            if (!viewToColor.containsKey(code)) {
                int color = poolColors.remove(0);
                poolColors.add(color);
                viewToColor.put(code, color);
            }
            if(Build.VERSION.SDK_INT > 21)
                seek.setProgressTintList(ColorStateList.valueOf(viewToColor.get(code)));
            /*
             * autocomplete allFunctions
             */
            /**
             * important
             */
            List<LineGraphSeries<DataPoint>> listSeries = graphUtils
                    .graphPharallel(iter, function, viewToColor.get(code));

            if (viewToFunction.containsKey(code))
                for (LineGraphSeries<DataPoint> inSerie : viewToFunction.get(code))
                    graph.removeSeries(inSerie);

            viewToFunction.put(code, listSeries);

            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(-50);
            graph.getViewport().setMaxY(50);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(-50);
            graph.getViewport().setMaxX(50);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setScrollableY(true);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScalableY(true);

            for (LineGraphSeries<DataPoint> inSerie : viewToFunction.get(code))
                graph.addSeries(inSerie);

        }catch (Exception e){
            styleWrongMessage(e.toString());
        }

    }
    public boolean checkSyntax(String function){
        try {
            new Expression(graphUtils.functionRevision(function)).with("x", "0").eval();
        }catch (NumberFormatException e){
            return true;
        }catch (Expression.ExpressionException e) {
            return false;
        }
        return true;
    }
    private final SuperActivityToast.OnButtonClickListener onButtonClickListener =
            new SuperActivityToast.OnButtonClickListener() {

                @Override
                public void onClick(View view, Parcelable token) {
                    SuperActivityToast.cancelAllSuperToasts();
                }
            };

    public void styleWrongMessage(String message){
        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_BUTTON)
                .setIndeterminate(true)
                .setButtonText("UNDO")
                .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                .setProgressBarColor(Color.WHITE)
                .setText(message)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(Color.rgb(244,67,54))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }


}
