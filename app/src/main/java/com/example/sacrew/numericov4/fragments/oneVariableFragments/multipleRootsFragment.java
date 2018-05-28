package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.app.Fragment;
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
import com.example.sacrew.numericov4.fragments.customPopUps.popUpMultipleRoots;
import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.MultipleRoots;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.MultipleRootsListAdapter;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.Secant;
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
public class multipleRootsFragment extends baseOneVariableFragments {

    private GraphView graph;
    private Expression functionG, functionGprim;
    private View view;
    private TextView xvalue;
    private AutoCompleteTextView textFunctionG, textFunctionGPrim;
    private ToggleButton errorToggle;
    private ListView listView;

    public multipleRootsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        try {
            view = inflater.inflate(R.layout.fragment_multiple_roots, container, false);
        } catch (InflateException e) {
            // ignorable
        }
        Button runMultiple = view.findViewById(R.id.runMultiple);
        runMultiple.setOnClickListener(new View.OnClickListener() {
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
        graph = view.findViewById(R.id.multipleGraph);
        textFunction = view.findViewById(R.id.function);
        iter = view.findViewById(R.id.iterations);
        textError = view.findViewById(R.id.error);
        xvalue = view.findViewById(R.id.xValue);
        textFunctionG = view.findViewById(R.id.functionG);
        textFunctionGPrim = view.findViewById(R.id.functionGprim);
        errorToggle = view.findViewById(R.id.errorToggle);

        textFunction.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
        textFunctionG.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
        textFunctionGPrim.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpMultipleRoots.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(boolean error, double errorValue, int ite) {
        Double xValue = 0.0;


        String originalFuncG = textFunctionG.getText().toString(); // primera derivada
        String originalFuncGPrim = textFunctionGPrim.getText().toString(); // segunda derivada
        error = checkSyntax(originalFuncG, textFunctionG) && checkSyntax(originalFuncGPrim, textFunctionGPrim);

        this.functionG = new Expression(functionRevision(originalFuncG));
        updatefunctions(originalFuncG, error);

        this.functionGprim = new Expression(functionRevision(originalFuncGPrim));
        updatefunctions(originalFuncGPrim,error);
        try {
            xValue = Double.parseDouble(xvalue.getText().toString());
        } catch (Exception e) {
            xvalue.setError("Invalid Xi");
            error = false;
        }
        String functionCompose = "x-(((" + function.getExpression() + ")*(" + originalFuncG +
                "))/(((" + originalFuncG + ")^2)-((" + function.getExpression() + ")*(" + originalFuncGPrim + "))))";

        if (error) {
            if (errorToggle.isChecked()) {
                multipleRootsMethod(xValue, errorValue, ite, true, functionCompose);
            } else {
                multipleRootsMethod(xValue, errorValue, ite, false, functionCompose);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void multipleRootsMethod(Double x0, Double tol, int ite, boolean errorRel, String compose) {
        Expression multipleRootsFunction = new Expression(compose);
        try {
            graph.removeAllSeries();

            function.setPrecision(100);
            ArrayList<MultipleRoots> listValues = new ArrayList<>();
            MultipleRoots titles = new MultipleRoots("n", "Xn", "f(Xn)", "f'(Xn)", "f''(Xn)", "Error");
            listValues.add(titles);
            if (tol >= 0) {
                if (ite > 0) {
                    double y0 = (multipleRootsFunction.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    if (y0 != 0) {
                        int cont = 0;
                        double error = tol + 1;
                        double xa = x0;
                        //MultipleRoots iteZero = new MultipleRoots(String.valueOf(cont), String.valueOf(convertirNormal(x0)), String.valueOf(convertirCientifica(y0)),String.valueOf(convertirCientifica(y0p1)), String.valueOf(convertirCientifica(y0p2)), String.valueOf(convertirCientifica(error)));
                        //listValues.add(iteZero);
                        while ((y0 != 0) && (error > tol) && (cont < ite)) {
                            double xn = (multipleRootsFunction.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                            y0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                            if (errorRel)
                                error = Math.abs(xn - xa) / xn;
                            else
                                error = Math.abs(xn - xa);
                            xa = xn;
                            cont++;
                            //MultipleRoots iteNext= new MultipleRoots(String.valueOf(cont), String.valueOf(convertirNormal(xa)), String.valueOf(convertirCientifica(y0)),String.valueOf(convertirCientifica(y0p1)), String.valueOf(convertirCientifica(y0p2)), String.valueOf(convertirCientifica(error)));
                            //listValues.add(iteNext);
                        }
                        graphSerie(xa - 0.5, xa, function.getExpression(), graph, Color.BLUE);
                        if (y0 == 0) {
                            graphPoint(xa, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                            Toast.makeText(getContext(), convertirNormal(xa) + " is a root", Toast.LENGTH_SHORT).show();
                            //System.out.println(xa + " is a root");
                        } else if (error <= tol) {
                            y0 = (this.function.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                            graphPoint(xa, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                            Toast.makeText(getContext(), convertirNormal(xa) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                            //System.out.println(xa + " is an aproximate root");
                        } else {
                            System.out.println("Failed the interval!");
                            Toast.makeText(getContext(), "Failed the interval!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        graphPoint(x0, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                        Toast.makeText(getContext(), convertirNormal(x0) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                        //System.out.println(x0 + " is a root");
                    }
                } else {
                    iter.setError("Wrong iterates");
                    Toast.makeText(getContext(), "Wrong iterates!", Toast.LENGTH_SHORT).show();
                    //System.out.println("Wrong iterates!");
                }
            } else {
                textError.setError("Tolerance must be < 0");
                Toast.makeText(getContext(), "Tolerance must be < 0", Toast.LENGTH_SHORT).show();
                //System.out.println("Tolerance < 0");
            }
            //MultipleRootsListAdapter adapter = new MultipleRootsListAdapter(getContext(), R.layout.list_adapter_multiple_roots, listValues);
            //listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Unexpected error posibly nan", Toast.LENGTH_SHORT).show();
        }
    }

    public void updatefunctions(String function,boolean error) {
        if (!graphFragment.allFunctions.contains(function) && error) {
            graphFragment.allFunctions.add(function);
            textFunction.setAdapter(new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
            textFunctionG.setAdapter(new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
            textFunctionGPrim.setAdapter(new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
        }
    }

}






