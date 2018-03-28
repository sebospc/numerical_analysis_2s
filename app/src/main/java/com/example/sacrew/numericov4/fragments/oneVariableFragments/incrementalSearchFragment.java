package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;

import static com.example.sacrew.numericov4.graphMethods.graphPoint;

/**
 * A simple {@link Fragment} subclass.
 */
public class incrementalSearchFragment extends Fragment {


    public incrementalSearchFragment() {
        // Required empty public constructor
    }

    private View view;
    private Button runIncremental;
    private Button runHelp;
    private ImageButton runChart;
    private GraphView graph;
    private Expression function;
    private TextView textFunction;
    private TextView xValue;
    private TextView delta;
    private TextView iter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_incremental_search,container,false);
        runIncremental = view.findViewById(R.id.runIncremental);
        runHelp = view.findViewById(R.id.runHelp);
        runChart = view.findViewById(R.id.runChart);
        graph = view.findViewById(R.id.incrementalGraph);
        textFunction = view.findViewById(R.id.function);
        xValue = view.findViewById(R.id.x_value);
        delta = view.findViewById(R.id.delta);
        iter = view.findViewById(R.id.iterations);
        runIncremental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execute();
            }
        });
        runHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });


        return view;
    }

    public void executeHelp(){
        Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "This method supposes a continuous function that presents a change of sign and looks for an approximation to the root of a function by means of the repetition of a number of times that the user enters, an initial point and a delta, defined as the change between the initial point and the next point.", Toast.LENGTH_LONG);
        toast1.setGravity(Gravity.CENTER,50,50);
        toast1.show();
    }

    public void execute(){

        boolean error = false;
        Double x = 0.0;
        Double deltaPrim = 0.0;
        int ite = 0;
        try{
            this.function = new Expression(textFunction.getText().toString());

            (function.with("x", BigDecimal.valueOf(1)).eval()).doubleValue();
        }catch (Exception e){
            textFunction.setError("Invalid function");

            error = true;
        }
        try{
            x = Double.parseDouble(xValue.getText().toString());
        }catch(Exception e){
            xValue.setError("Invalid x");
            error = true;
        }
        try{
            deltaPrim = Double.parseDouble(delta.getText().toString());
        }catch (Exception e){
            delta.setError("Invalid delta");
            error = true;
        }

        try {
            ite = Integer.parseInt(iter.getText().toString());
        }catch (Exception e){
            iter.setError("Wrong iterations");
            error = true;
        }
        if(!error) {
            incrementalSearchMethod(x, deltaPrim, ite);
        }
    }

    public void incrementalSearchMethod(Double x0,Double delta,int ite) {
        graph.removeAllSeries();
        function.setPrecision(100);
        if(delta != 0){
            if(ite > 0){
                double y0 = (function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                if(y0 != 0){
                    double x1 = x0 + delta;
                    double y1 = (function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                    int cont = 1;
                    LineGraphSeries<DataPoint> serie = new LineGraphSeries<>();
                    serie.appendData(new DataPoint(x1,y1),false,ite);
                    while(((y1*y0) > 0) && (cont < ite)){
                        x0 = x1;
                        y0 = y1;
                        x1 = x0 + delta;
                        y1 = (function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                        if(delta >= 0)
                        serie.appendData(new DataPoint(x1,y1),false,ite);
                        else {
                            // no se puede graficar funciones alrevez :(
                        }
                        cont++;
                    }
                    graph.addSeries(serie);
                    if(y1 == 0){
                        graphPoint(x1,y1,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#00CD00");
                        //System.out.println(x1 + " is a root");
                    }else if(y1*y0 < 0){
                        //System.out.println("[" + x0 + ", " + x1 + "] is an interval");
                    }else{
                        // System.out.println("Failed the interval!");
                    }
                }else{
                    graphPoint(x0,y0,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#00CD00");
                    //System.out.println(x0 + " is a root");
                }
            }else{
                iter.setError("Iterate needs be >0");
            }
        }else{
            this.delta.setError("Delta cannot be zero");
        }
    }

}
