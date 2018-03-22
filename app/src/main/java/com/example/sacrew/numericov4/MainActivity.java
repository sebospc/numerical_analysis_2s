package com.example.sacrew.numericov4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udojava.evalex.Expression;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    private GraphView graph ;

    public static List<LineGraphSeries<DataPoint>> listSeries;
    private LinearLayout parentLinearLayout;
    private RelativeLayout hiderB;
    private LinearLayout hiderA;
    private boolean isup = true;
    private Map<Integer, List<LineGraphSeries<DataPoint>>> viewToFunction;
    private int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private Thread[] cores = new Thread[NUMBER_OF_CORES];
    final static double scale = 0.1; //escala de desplazamiento


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @SuppressLint("UseSparseArrays")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        hiderA = findViewById(R.id.hiderA);
        hiderB = findViewById(R.id.hiderB);
        graph = findViewById(R.id.graph);
        viewToFunction = new HashMap<>();

        //create one
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new field before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);




    }
    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new field before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);


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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hiderA = findViewById(R.id.hiderA);
                            graph = findViewById(R.id.graph);
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
            hiderA = findViewById(R.id.hiderA);
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
        int code = ((View) v.getParent()).findViewById(R.id.number_edit_text).hashCode();
        for (LineGraphSeries<DataPoint> inSerie  : viewToFunction.get(code)) {
            graph.removeSeries(inSerie);
        }
        viewToFunction.remove(code);
        parentLinearLayout.removeView((View) v.getParent());
    }
    @SuppressLint("CutPasteId")
    public void graphIt(View v){
        Double start = -40D;
        try {
            EditText edittext_var;
            edittext_var = ((View) v.getParent()).findViewById(R.id.number_edit_text);
            SeekBar seek = ((SeekBar)((View) v.getParent()).findViewById(R.id.seek));
            int iter = seek.getProgress();
            String function = String.valueOf(edittext_var.getText());
            Expression exp = new Expression(function);

            try {
                (exp.with("x", BigDecimal.valueOf(-1)).eval()).doubleValue();
            }catch (Exception e){
                System.out.println("negative alert");
                start = 0.001D;
            }
            Toast.makeText(MainActivity.this, function, Toast.LENGTH_SHORT).show();
            //Expression expression = new Expression(function);

            listSeries = new LinkedList<LineGraphSeries<DataPoint>>();
            graphParalelism(function,iter,start);

            graph.getViewport().setScrollable(true); // enables horizontal scrolling
            graph.getViewport().setScrollableY(true); // enables vertical scrolling
            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(-80);
            graph.getViewport().setMaxX(80);
            graph.getViewport().setMinY(-80);
            graph.getViewport().setMaxY(80);
            int code = ((View) v.getParent()).findViewById(R.id.number_edit_text).hashCode();
            if(viewToFunction.containsKey(code)){
                for (LineGraphSeries<DataPoint> inSerie  : viewToFunction.get(code)) {
                    graph.removeSeries(inSerie);
                }
            }
            viewToFunction.put(code,listSeries);

            for (LineGraphSeries<DataPoint> inSerie  : viewToFunction.get(code)) {
                graph.addSeries(inSerie);
            }



        }catch (Exception e){
            System.out.println(e);
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void graphParalelism(String function,int iter,Double x){
        int end = iter / NUMBER_OF_CORES;

        for(int i = 0; i < cores.length;i++){

            cores[i]=new Thread(new graphParallel(x,end,function));
            cores[i].start();

            x = x + (end*scale)+scale;
        }
        for (Thread core : cores) {
            try {
                core.join();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}
