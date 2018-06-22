package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpMultipleRoots;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.MultipleRoots;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.MultipleRootsListAdapter;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.fragments.homeFragment.poolColors;


/**
 * A simple {@link Fragment} subclass.
 */
public class multipleRootsFragment extends baseOneVariableFragments {


    private Expression functionDeriv1, functionDeriv2;
    private View view;
    private EditText xvalue;
    private EditText textFunctionDeriv1, textFunctionGDeriv2;
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
        xvalue = view.findViewById(R.id.xValue);
        textFunctionDeriv1 = view.findViewById(R.id.functionG);
        textFunctionGDeriv2 = view.findViewById(R.id.functionGprim);

        registerEditText(textFunction, getContext(), getActivity());
        registerEditText(textFunctionDeriv1, getContext(), getActivity());
        registerEditText(textFunctionGDeriv2, getContext(), getActivity());
        registerEditText(iter, getContext(), getActivity());
        registerEditText(textError, getContext(), getActivity());
        registerEditText(xvalue, getContext(), getActivity());

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


        String originalFuncG = textFunctionDeriv1.getText().toString(); // primera derivada
        String originalFuncGPrim = textFunctionGDeriv2.getText().toString(); // segunda derivada
        error = checkSyntax(originalFuncG, textFunctionDeriv1) && checkSyntax(originalFuncGPrim, textFunctionGDeriv2);

        this.functionDeriv1 = new Expression(functionRevision(originalFuncG));

        this.functionDeriv2 = new Expression(functionRevision(originalFuncGPrim));
        try {
            xValue = Double.parseDouble(xvalue.getText().toString());
        } catch (Exception e) {
            xvalue.setError("Invalid Xi");
            error = false;
        }

        if (error) {
            if (errorToggle.isChecked()) {
                multipleRootsMethod(xValue, errorValue, ite, true);
            } else {
                multipleRootsMethod(xValue, errorValue, ite, false);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void multipleRootsMethod(double x0, Double tol, int ite, boolean errorRel) {
        try {
            function.setPrecision(100);
            functionDeriv1.setPrecision(100);
            functionDeriv2.setPrecision(100);
            ArrayList<MultipleRoots> listValues = new ArrayList<>();
            MultipleRoots titles = new MultipleRoots("n", "Xn", "f(Xn)", "f'(Xn)", "f''(Xn)", "Error");
            listValues.add(titles);
            listValuesTitles = new LinkedList<>();
            listValuesTitles.add("Xn");
            listValuesTitles.add("f(Xn)");
            listValuesTitles.add("f'(Xn)");
            listValuesTitles.add("f''(Xn)");
            listValuesTitles.add("Error");
            completeList = new LinkedList<>();
            if (tol >= 0) {
                if (ite > 0) {
                    double y0 = Double.NaN;
                    double y0p1 = Double.NaN;
                    double y0p2 = Double.NaN;
                    try {
                        y0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                        y0p1 = (this.functionDeriv1.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                        y0p2 = (this.functionDeriv2.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    } catch (Exception e) {
                        styleWrongMessage("Unexpected error posibly NaN");
                    }
                    double den = Math.pow(y0p1, 2) - y0 * y0p2;

                    int cont = 0;
                    double error = tol + 1;

                    MultipleRoots iteZero = new MultipleRoots(String.valueOf(cont), String.valueOf(normalTransformation(x0)), String.valueOf(cientificTransformation(y0)), String.valueOf(cientificTransformation(y0p1)), String.valueOf(cientificTransformation(y0p2)), String.valueOf(cientificTransformation(error)));
                    listValues.add(iteZero);

                    List<String> listValuesIteZero = new LinkedList<>();
                    listValuesIteZero.add(String.valueOf(x0));
                    listValuesIteZero.add(String.valueOf(cientificTransformation(y0)));
                    listValuesIteZero.add(String.valueOf(cientificTransformation(y0p1)));
                    listValuesIteZero.add(String.valueOf(cientificTransformation(y0p2)));
                    listValuesIteZero.add(String.valueOf(""));
                    completeList.add(listValuesIteZero);

                    calc = true;
                    Expression multipleRootsFunction;
                    while ((y0 != 0) && (error > tol)&& den != 0 && (cont < ite)) {
                        ArrayList<String> listValuesIteNext = new ArrayList<String>();

                        multipleRootsFunction = new Expression("x-(" + y0 * y0p1 + ")/(" + den + ")");
                        multipleRootsFunction.setPrecision(100);

                        double xn = (multipleRootsFunction.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                        try {
                            y0 = (this.function.with("x", BigDecimal.valueOf(xn)).eval()).doubleValue();
                            y0p1 = (this.functionDeriv1.with("x", BigDecimal.valueOf(xn)).eval()).doubleValue();
                            y0p2 = (this.functionDeriv2.with("x", BigDecimal.valueOf(xn)).eval()).doubleValue();
                        } catch (Exception e) {
                            styleWrongMessage("Unexpected error posibly NaN");
                        }

                        den = Math.pow(y0p1, 2) - y0 * y0p2;
                        if (errorRel)
                            error = Math.abs(xn - x0) / xn;
                        else
                            error = Math.abs(xn - x0);
                        x0 = xn;
                        cont++;
                        MultipleRoots iteNext = new MultipleRoots(String.valueOf(cont), String.valueOf(normalTransformation(x0)), String.valueOf(cientificTransformation(y0)), String.valueOf(cientificTransformation(y0p1)), String.valueOf(cientificTransformation(y0p2)), String.valueOf(cientificTransformation(error)));
                        listValues.add(iteNext);
                        listValuesIteNext.add(String.valueOf(x0));
                        listValuesIteNext.add(String.valueOf(cientificTransformation(y0)));
                        listValuesIteNext.add(String.valueOf(cientificTransformation(y0p1)));
                        listValuesIteNext.add(String.valueOf(cientificTransformation(y0p2)));
                        listValuesIteNext.add(String.valueOf(cientificTransformation(error)));
                        completeList.add(listValuesIteNext);
                    }
                    listValues.add(new MultipleRoots("", "", "", "", "", ""));
                    int color = poolColors.remove(0);
                    poolColors.add(color);
                    graphSerie(function.getExpression(), 0, x0, color);
                    if (y0 == 0) {
                        color = poolColors.remove(0);
                        poolColors.add(color);
                        graphPoint(x0, y0, color);
                        styleCorrectMessage(normalTransformation(x0) + " is a root");
                    } else if (error <= tol) {
                        color = poolColors.remove(0);
                        poolColors.add(color);
                        graphPoint(x0, y0, color);

                        styleCorrectMessage(normalTransformation(x0) + " is an aproximate root");
                    } else {
                        styleWrongMessage("The method failed in iteration: " + ite);
                    }
                } else {
                    iter.setError("Wrong iterates");
                    styleWrongMessage("Wrong iterates");
                }
            } else {
                textError.setError("Tolerance must be > 0");
                styleWrongMessage("Tolerance must be > 0");

            }
            MultipleRootsListAdapter adapter = new MultipleRootsListAdapter(getContext(), R.layout.list_adapter_multiple_roots, listValues);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            styleWrongMessage("Unexpected error posibly nan");
        }
    }


}






