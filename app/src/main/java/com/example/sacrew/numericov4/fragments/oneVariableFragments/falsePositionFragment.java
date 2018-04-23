package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpFalsePosition;
import com.example.sacrew.numericov4.fragments.home;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.graphMethods.graphPoint;
import static com.example.sacrew.numericov4.graphMethods.graphSerie;

/**
 * A simple {@link Fragment} subclass.
 */
public class falsePositionFragment extends Fragment {


    public falsePositionFragment() {
        // Required empty public constructor
    }


    private Button runFake;
    private Button runHelp;
    private GraphView graph;
    private Expression function;
    private View view;
    private TextView xi,xs,iter,textError;
    private AutoCompleteTextView textFunction;
    private ToggleButton errorToggle;
    private List<Integer> colors = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_false_position, container, false);
        }catch (InflateException e){
            // ignorable
        }
        runFake = view.findViewById(R.id.runFalse);
        runFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execute();
            }
        });
        runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        graph = view.findViewById(R.id.falseGraph);
        textFunction = view.findViewById(R.id.function);
        iter = view.findViewById(R.id.iterations);
        textError = view.findViewById(R.id.error);
        xi = view.findViewById(R.id.xi);
        xs = view.findViewById(R.id.xs);
        errorToggle = view.findViewById(R.id.errorToggle);

        textFunction.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, home.allFunctions));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp(){
        Intent i = new Intent(getContext().getApplicationContext(), popUpFalsePosition.class);
        startActivity(i);
    }

    public void execute(){
        this.xi.setError(null);
        this.xs.setError(null);
        boolean error = false;
        Double xiValue = 0.0;
        Double xsValue = 0.0;
        int ite = 0;
        Double errorValue= 0.0;

        try{
            this.function = new Expression(textFunction.getText().toString());

            (function.with("x", BigDecimal.valueOf(1)).eval()).doubleValue();
            if(!home.allFunctions.contains(function.getExpression())){
                home.allFunctions.add(function.getExpression());
                textFunction.setAdapter(new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, home.allFunctions));
            }
        }catch (Exception e){
            textFunction.setError("Invalid function");

            error = true;
        }
        try{
            xiValue = Double.parseDouble(xi.getText().toString());
        }catch(Exception e){
            xi.setError("Invalid Xi");
            error = true;
        }
        try{
            xsValue = Double.parseDouble(xs.getText().toString());
        }catch (Exception e){
            xs.setError("Invalid xs");
            error = true;
        }

        try {
            ite = Integer.parseInt(iter.getText().toString());
        }catch (Exception e){
            iter.setError("Wrong iterations");
            error = true;
        }
        try {
            errorValue = new Expression(textError.getText().toString()).eval().doubleValue();
            System.out.println("error value  "+errorValue);
        }catch (Exception e){
            textError.setError("Invalid error value");
        }
        if(!error) {
            if(errorToggle.isChecked()){
                falsePosition(xiValue,xsValue,errorValue,ite,true);
            }else{
                falsePosition(xiValue,xsValue,errorValue,ite,false);
            }
        }
    }

    public void falsePosition(Double xi, Double xs, Double tol, int ite, boolean errorRel) {
        graph.removeAllSeries();
        function.setPrecision(100);
        if(tol >= 0){
            if(ite > 0){
                double yi = (this.function.with("x", BigDecimal.valueOf(xi)).eval()).doubleValue();
                if(yi != 0){
                    double ys = (this.function.with("x", BigDecimal.valueOf(xs)).eval()).doubleValue();
                    if(ys != 0){
                        if(yi*ys < 0){

                            double xm = xi -(yi*(xs-xi))/(yi-ys);
                            double ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();
                            double error = tol + 1;
                            int cont = 1;
                            double xaux = xm;
                            graphSerie(xi,xs,this.function.getExpression(),graph, Color.BLUE);
                            while((ym != 0) && (error > tol) && (cont < ite)){
                                if(yi*ym < 0){
                                    xs = xm;
                                    ys = ym;
                                }else{
                                    xi = xm;
                                    yi = ym;
                                }
                                xaux = xm;
                                //graphStraight(xi,yi,xs,ys,graph);
                                xm = xi - ((yi*(xs-xi))/(ys-yi));
                                //graphPoint(xm,0,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#FA4659",false);
                                ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();

                                if(errorRel)
                                    error = Math.abs(xm - xaux)/xm;
                                else
                                    error = Math.abs(xm - xaux);
                                cont++;
                            }

                            if(ym == 0){
                                graphPoint(xm,ym,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                                //System.out.println(xm + " is an aproximate root");

                            }else if(error < tol){
                                graphPoint(xaux,ym,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                                //System.out.println(xaux + " is an aproximate root");
                            }else{
                                //System.out.println("Failed!");
                            }
                        }else{
                            this.xi.setError("Failed the interval");
                            this.xs.setError("Failed the interval");
                            //System.out.println("Failed the interval!");

                        }
                    }else{
                        //System.out.println(xs + " is an aproximate root");
                        graphPoint(xs,ys,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                    }
                }else{
                    //System.out.println(xi + " is an aproximate root");
                    graphPoint(xi,yi,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                }
            }else{
                iter.setError("Wrong iterates");
                //System.out.println("Wrong iterates!");
            }
        }else{
            textError.setError("Tolerance must be < 0");
            //System.out.println("Tolerance < 0");
        }
    }




}


