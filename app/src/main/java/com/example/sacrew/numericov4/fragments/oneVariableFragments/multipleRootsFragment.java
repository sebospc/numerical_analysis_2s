package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.sacrew.numericov4.fragments.tableview.TableViewModel;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static com.example.sacrew.numericov4.fragments.homeFragment.poolColors;


/**
 * A simple {@link Fragment} subclass.
 */
public class multipleRootsFragment extends baseOneVariableFragments {


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
                cleanGraph();
                bootStrap();
            }
        });
        Button runHelp = view.findViewById(R.id.runHelp);
        Button runChart = view.findViewById(R.id.runChart);
        runChart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                executeChart(getContext());
            }
        });
        listView = view.findViewById(R.id.listView);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
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
        updatefunctions(originalFuncGPrim, error);
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
        String message = "";
        Expression multipleRootsFunction = new Expression(compose);
        try {


            function.setPrecision(100);
            ArrayList<MultipleRoots> listValues = new ArrayList<>();
            MultipleRoots titles = new MultipleRoots("n", "Xn", "f(Xn)", "f'(Xn)", "f''(Xn)", "Error");
            listValues.add(titles);
            listValuesTitles = new LinkedList<>();
            listValuesTitles.add("Xn");
            listValuesTitles.add("f(Xn)");
            listValuesTitles.add("f'(Xn)");
            listValuesTitles.add("f''(Xn)");
            listValuesTitles.add("Error");
            //TableViewModel.getTitles(listValuesTitles);
            completeList = new LinkedList<>();
            if (tol >= 0) {
                if (ite > 0) {
                    double y0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    double y0p1 = (this.functionG.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    double y0p2 = (this.functionGprim.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    if (y0 != 0) {
                        int cont = 0;
                        double error = tol + 1;
                        double xa = x0;
                        MultipleRoots iteZero = new MultipleRoots(String.valueOf(cont), String.valueOf(convertirNormal(x0)), String.valueOf(convertirNormal(y0)), String.valueOf(convertirNormal(y0p1)), String.valueOf(convertirNormal(y0p2)), String.valueOf(convertirCientifica(error)));
                        listValues.add(iteZero);
                        List<String> listValuesIteZero = new LinkedList<>();
                        listValuesIteZero.add(String.valueOf(x0));
                        listValuesIteZero.add(String.valueOf(y0));
                        listValuesIteZero.add(String.valueOf(y0p1));
                        listValuesIteZero.add(String.valueOf(y0p2));
                        listValuesIteZero.add(String.valueOf(convertirCientifica(error)));
                        completeList.add(listValuesIteZero);
                        calc= true;
                        while ((y0 != 0) && (error > tol) && (cont < ite)) {
                            ArrayList<String> listValuesIteNext = new ArrayList<String>();
                            if (xa == 0) {
                                Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                                break;
                            }

                            double xn = (multipleRootsFunction.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();

                            y0 = (this.function.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                            y0p1 = (this.functionG.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                            y0p2 = (this.functionGprim.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                            if (errorRel)
                                error = Math.abs(xn - xa) / xn;
                            else
                                error = Math.abs(xn - xa);
                            xa = xn;
                            cont++;
                            MultipleRoots iteNext = new MultipleRoots(String.valueOf(cont), String.valueOf(convertirNormal(x0)), String.valueOf(convertirNormal(y0)), String.valueOf(convertirNormal(y0p1)), String.valueOf(convertirNormal(y0p2)), String.valueOf(convertirCientifica(error)));
                            listValues.add(iteNext);
                            listValuesIteNext.add(String.valueOf(xa));
                            listValuesIteNext.add(String.valueOf(y0));
                            listValuesIteNext.add(String.valueOf(y0p1));
                            listValuesIteNext.add(String.valueOf(y0p2));
                            listValuesIteNext.add(String.valueOf(convertirCientifica(error)));
                            completeList.add(listValuesIteNext);
                        }
                        //TableViewModel.getCeldas(completeList);
                        //calc = true;
                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphSerie(function.getExpression(),0,xa*2,color);
                        if (y0 == 0) {
                            //graphSerie(xa - 0.2, xa + 0.2, function.getExpression(), graph, Color.BLUE);
                            //graphPoint(xa, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), Color.parseColor("#0E9577"), true);
                            color = poolColors.remove(0);
                            poolColors.add(color);
                            graphPoint(xa,y0,color);
                            //Toast.makeText(getContext(), convertirNormal(xa) + " is a root", Toast.LENGTH_SHORT).show();
                            message = convertirNormal(xa) + " is a root";
                            styleCorrectMessage(message);
                        } else if (error <= tol) {
                            //graphSerie(xa - 0.2, xa + 0.2, function.getExpression(), graph, Color.BLUE);
                            color = poolColors.remove(0);
                            poolColors.add(color);
                            graphPoint(xa,y0,color);
                            //graphPoint(xa, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), Color.parseColor("#0E9577"), true);
                            message = convertirNormal(xa) + " is an aproximate root";
                            styleCorrectMessage(message);
                            //Toast.makeText(getContext(), convertirNormal(xa) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                            message = "The method failed!";
                            styleWrongMessage(message);
                        }
                    } else {
                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphPoint(x0,y0,color);
                        //graphPoint(x0, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), Color.parseColor("#0E9577"), true);
                        message = convertirNormal(x0) + " is an aproximate root";
                        styleCorrectMessage(message);
                        //Toast.makeText(getContext(), convertirNormal(x0) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    iter.setError("Wrong iterates");
                    message = "Wrong iterates";
                    styleWrongMessage(message);
                }
            } else {
                textError.setError("Tolerance must be > 0");
                message = "Tolerance must be > 0";
                styleWrongMessage(message);

            }
            MultipleRootsListAdapter adapter = new MultipleRootsListAdapter(getContext(), R.layout.list_adapter_multiple_roots, listValues);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            //Toast.makeText(getActivity(), "Unexpected error posibly nan", Toast.LENGTH_SHORT).show();
            message = "Unexpected error posibly nan";
            styleWrongMessage(message);
        }
    }

    public void updatefunctions(String function, boolean error) {
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






