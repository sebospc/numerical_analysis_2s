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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpFixedPoint;
import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.FixedPoint;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.FixedPointListAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;



/**
 * A simple {@link Fragment} subclass.
 */
public class fixedPointFragment extends baseOneVariableFragments{

    private GraphView graph;
    private Expression functionG;
    private View view;
    private ListView listView;
    private TextView xvalue;
    private AutoCompleteTextView textFunctionG;
    private ToggleButton errorToggle;

    public fixedPointFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
        view = inflater.inflate(R.layout.fragment_fixed_point,container,false);
        }catch (InflateException e){
            // ignorable
       }
        Button runFixed = view.findViewById(R.id.runFixed);
        runFixed.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                bootStrap();
            }
        });
        Button runHelp = view.findViewById(R.id.runHelp);
        listView = view.findViewById(R.id.listView);
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

        textFunction.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
        textFunctionG.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp(){
        Intent i = new Intent(getContext().getApplicationContext(), popUpFixedPoint.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(boolean error, double errorValue, int ite){
        Double xValue = 0.0;

            String originalFuncG = textFunctionG.getText().toString();
            error = checkSyntax(originalFuncG,textFunctionG) && error;
            this.functionG = new Expression(functionRevision(originalFuncG));
            updatefunctions(originalFuncG,error);


        try{
            xValue = Double.parseDouble(xvalue.getText().toString());
        }catch(Exception e){
            xvalue.setError("Invalid Xi");
            error = false;
        }


        if(error) {
            if(errorToggle.isChecked()){
                fixedPointMethod(xValue,errorValue,ite,true);
            }else{
                fixedPointMethod(xValue,errorValue,ite,false);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fixedPointMethod(Double x0, Double tol, int ite, boolean errorRel) {
        try {
            graph.removeAllSeries();

            function.setPrecision(100);
            ArrayList<FixedPoint> listValues = new ArrayList<>();
        FixedPoint titles = new FixedPoint("n", "Xn", "f(Xn)", "g(Xn)", "Error");
        listValues.add(titles);
            if (tol >= 0) {
                if (ite > 0) {
                    double y0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    double g0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    if (y0 != 0) {
                        int cont = 0;
                        double error = tol + 1;
                        FixedPoint iteZero = new FixedPoint(String.valueOf(cont), String.valueOf(convertirNormal(x0)), String.valueOf(convertirCientifica(y0)), String.valueOf(convertirCientifica(g0)), String.valueOf(convertirCientifica(error)));
                        listValues.add(iteZero);
                        double xa = x0;
                            while ((y0 != 0) && (error > tol) && (cont < ite)) {
                                double xn = (this.functionG.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                                y0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();

                                if (errorRel)
                                    error = Math.abs(xn - xa) / xn;
                                else
                                    error = Math.abs(xn - xa);
                                xa = xn;
                                cont++;
                                FixedPoint iteNext = new FixedPoint(String.valueOf(cont), String.valueOf(convertirNormal(xa)), String.valueOf(convertirCientifica(y0)), String.valueOf(convertirCientifica(xn)), String.valueOf(convertirCientifica(error)));
                                listValues.add(iteNext);
                            }

                            graphSerie(xa - 0.5, xa, function.getExpression(), graph, Color.BLUE);
                            graphSerie(xa - 0.5, xa, functionG.getExpression(), graph, Color.RED);

                            if (y0 == 0) {
                                graphPoint(xa, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                                Toast.makeText(getContext(), convertirNormal(xa) + " is a root", Toast.LENGTH_SHORT).show();
                            } else if (error <= tol) {
                                y0 = (this.function.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                                graphPoint(xa, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                                Toast.makeText(getContext(), convertirNormal(xa) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed the interval!", Toast.LENGTH_SHORT).show();
                            }

                    } else {
                        graphPoint(x0, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                        Toast.makeText(getContext(),  convertirNormal(x0) + " is an aproximate root", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    iter.setError("Wrong iterates");
                }
            } else {
                textError.setError("Tolerance must be < 0");

            }
            FixedPointListAdapter adapter = new FixedPointListAdapter(getContext(), R.layout.list_adapter_fixed_point, listValues);
            listView.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(getActivity(), "Unexpected error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    }
    public void updatefunctions(String function,boolean error){
        if(!graphFragment.allFunctions.contains(function) && error){
            graphFragment.allFunctions.add(function);
            textFunction.setAdapter(new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
            textFunctionG.setAdapter(new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
        }
    }
}
