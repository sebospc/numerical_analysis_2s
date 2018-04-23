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
import com.example.sacrew.numericov4.fragments.home;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.FixedPoint;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.FixedPointListAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.sacrew.numericov4.graphMethods.functionRevision;
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
    private ListView listView;
    private TextView xvalue, textFunctionG,iter,textError;
    private AutoCompleteTextView textFunction;
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
        runFixed = view.findViewById(R.id.runFixed);
        runFixed.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                execute();
            }
        });
        runHelp = view.findViewById(R.id.runHelp);
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
                (getActivity(), android.R.layout.select_dialog_item, home.allFunctions));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp(){
        Intent i = new Intent(getContext().getApplicationContext(), popUpFixedPoint.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(){
        boolean error = false;
        Double xValue = 0.0;
        int ite = 0;
        Double errorValue= 0.0;
        String originalFunc = textFunction.getText().toString();
        try{

            this.function = new Expression(functionRevision(originalFunc));

            (function.with("x", BigDecimal.valueOf(1)).eval()).doubleValue();
            if(!home.allFunctions.contains(originalFunc)){
                home.allFunctions.add(originalFunc);
                textFunction.setAdapter(new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, home.allFunctions));
            }
        }catch (Exception e){
            textFunction.setError("Invalid function");

            error = true;
        }

        try{
            String originalFuncG = textFunctionG.getText().toString();
            this.functionG = new Expression(functionRevision(originalFuncG));

            (function.with("x", BigDecimal.valueOf(1)).eval()).doubleValue();
            if(!home.allFunctions.contains(originalFuncG)){
                home.allFunctions.add(originalFuncG);
                textFunction.setAdapter(new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, home.allFunctions));
            }
        }catch (Exception e){
            textFunctionG.setError("Invalid function");
            error = true;
        }

        try{
            xValue = Double.parseDouble(xvalue.getText().toString());
        }catch(Exception e){
            xvalue.setError("Invalid Xi");
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

    public static String convertirCientifica(double val){
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("0.##E0");
        return num.format(val);
    }

    public static String convertirNormal(double val){
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("0.##");
        return num.format(val);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fixedPointMethod(Double x0, Double tol, int ite, boolean errorRel) {
        try {
            graph.removeAllSeries();

            function.setPrecision(100);
            ArrayList<FixedPoint> listValues = new ArrayList<>();
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
                        graphSerie(xa-0.5, xa, function.getExpression(), graph, Color.BLUE);
                        graphSerie(xa-0.5, xa, functionG.getExpression(), graph, Color.RED);
                        if (y0 == 0) {
                            graphPoint(xa, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                            Toast.makeText(getContext(),  convertirNormal(xa) + " is a root", Toast.LENGTH_SHORT).show();
                            //System.out.println(xa + " is a root");
                        } else if (error <= tol) {
                            y0 = (this.function.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                            graphPoint(xa, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                            Toast.makeText(getContext(),  convertirNormal(xa) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                            //System.out.println(xa + " is an aproximate root");
                        } else {
                            System.out.println("Failed the interval!");
                            Toast.makeText(getContext(),  "Failed the interval!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        graphPoint(x0, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                        Toast.makeText(getContext(),  convertirNormal(x0) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                        //System.out.println(x0 + " is a root");
                    }
                } else {
                    iter.setError("Wrong iterates");
                    Toast.makeText(getContext(),  "Wrong iterates!", Toast.LENGTH_SHORT).show();
                    //System.out.println("Wrong iterates!");
                }
            } else {
                textError.setError("Tolerance must be < 0");
                Toast.makeText(getContext(),  "Tolerance must be < 0", Toast.LENGTH_SHORT).show();
                //System.out.println("Tolerance < 0");
            }
            FixedPointListAdapter adapter = new FixedPointListAdapter(getContext(), R.layout.fixed_point_list_adapter, listValues);
            listView.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(getActivity(), "Unexpected error posibly nan", Toast.LENGTH_SHORT).show();
        }
        }


}
