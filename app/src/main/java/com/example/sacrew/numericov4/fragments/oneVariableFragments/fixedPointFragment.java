package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpFixedPoint;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpIncrementalSearch;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;

import static com.example.sacrew.numericov4.graphMethods.graphPoint;
import static com.example.sacrew.numericov4.graphMethods.graphSerie;

/**
 * A simple {@link Fragment} subclass.
 */
public class fixedPointFragment extends Fragment {

    private Button runFixed;
    private Button runHelp;
    private GraphView graph;
    private Expression function,functionG;
    private View view;
    private TextView xvalue, textFunctionG,iter,textFunction,textError;
    private ToggleButton errorToggle;

    public fixedPointFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fixed_pont,container,false);
        runFixed = view.findViewById(R.id.runFixed);
        runFixed.setOnClickListener(new View.OnClickListener() {
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
        graph = view.findViewById(R.id.fixedGraph);
        textFunction = view.findViewById(R.id.function);
        iter = view.findViewById(R.id.iterations);
        textError = view.findViewById(R.id.error);
        xvalue = view.findViewById(R.id.xValue);
        textFunctionG = view.findViewById(R.id.functionG);
        errorToggle = view.findViewById(R.id.errorToggle);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp(){
        Intent i = new Intent(getContext().getApplicationContext(), popUpFixedPoint.class);
        startActivity(i);
    }

    public void execute(){
        boolean error = false;
        Double xValue = 0.0;
        int ite = 0;
        Double errorValue= 0.0;

        try{
            this.function = new Expression(textFunction.getText().toString());

            (function.with("x", BigDecimal.valueOf(1)).eval()).doubleValue();
        }catch (Exception e){
            textFunction.setError("Invalid function");

            error = true;
        }
        try{
            xValue = Double.parseDouble(xvalue.getText().toString());
        }catch(Exception e){
            xvalue.setError("Invalid Xi");
            error = true;
        }
        try{
            this.functionG = new Expression(textFunctionG.getText().toString());

            (function.with("x", BigDecimal.valueOf(1)).eval()).doubleValue();
        }catch (Exception e){
            textFunctionG.setError("Invalid function");
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
                fixedPointMethod(xValue,errorValue,ite,true);
            }else{
                fixedPointMethod(xValue,errorValue,ite,false);
            }
        }
    }

    private void fixedPointMethod(Double x0, Double tol, int ite, boolean errorRel){
        graph.removeAllSeries();
        function.setPrecision(100);
        if(tol >= 0){
            if(ite > 0){
                double y0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                if(y0 != 0){
                    int cont = 0;
                    double error = tol + 1;
                    double xa = x0;

                    while((y0 != 0) && (error > tol) && (cont < ite)){
                        double xn = (this.functionG.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                        y0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();

                        if(errorRel)
                            error = Math.abs(xn - xa)/xn;
                        else
                            error = Math.abs(xn - xa);
                        xa = xn;
                        cont++;
                    }
                    graphSerie(x0,xa,function.getExpression(),graph, Color.BLUE);
                    graphSerie(x0,xa,functionG.getExpression(),graph, Color.RED);
                    if(y0 == 0){
                        graphPoint(xa,y0,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                        //System.out.println(xa + " is a root");
                    }else if(error <= tol){
                        graphPoint(xa,y0,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                        //System.out.println(xa + " is an aproximate root");
                    }else{
                        //System.out.println("Failed the interval!");
                    }
                }else{
                    graphPoint(x0,y0,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                    //System.out.println(x0 + " is a root");
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
