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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpMultipleRoots;
import com.example.sacrew.numericov4.fragments.graphFragment;
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


    private Expression functionG, functionGprim;
    private View view;
    private EditText xvalue;
    private EditText textFunctionG, textFunctionGPrim;
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
        xvalue = view.findViewById(R.id.xValue);
        textFunctionG = view.findViewById(R.id.functionG);
        textFunctionGPrim = view.findViewById(R.id.functionGprim);

        registerEditText(textFunction,getContext(),getActivity());
        registerEditText(textFunctionG,getContext(),getActivity());
        registerEditText(textFunctionGPrim,getContext(),getActivity());
        registerEditText(iter,getContext(),getActivity());
        registerEditText(textError,getContext(),getActivity());
        registerEditText(xvalue,getContext(),getActivity());

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

        this.functionGprim = new Expression(functionRevision(originalFuncGPrim));
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
    private void multipleRootsMethod(Double x0, Double tol, int ite, boolean errorRel) {
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
                        MultipleRoots iteZero = new MultipleRoots(String.valueOf(cont), String.valueOf(normalTransformation(xa)), String.valueOf(normalTransformation(y0)), String.valueOf(normalTransformation(y0p1)), String.valueOf(normalTransformation(y0p2)), String.valueOf(""));
                        listValues.add(iteZero);

                        List<String> listValuesIteZero = new LinkedList<>();
                        listValuesIteZero.add(String.valueOf(xa));
                        listValuesIteZero.add(String.valueOf(y0));
                        listValuesIteZero.add(String.valueOf(y0p1));
                        listValuesIteZero.add(String.valueOf(y0p2));
                        listValuesIteZero.add(String.valueOf(""));
                        completeList.add(listValuesIteZero);
                        calc= true;
                        Expression multipleRootsFunction;
                        while ((y0 != 0) && (error > tol) && (cont < ite)) {
                            ArrayList<String> listValuesIteNext = new ArrayList<String>();
                            if (xa == 0) {
                                Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                                break;
                            }

                            y0 = (this.function.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                            y0p1 = (this.functionG.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                            y0p2 = (this.functionGprim.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();

                            multipleRootsFunction = new Expression("x-("+y0*y0p1+")/("+(Math.pow(y0p1,2)-(y0*y0p2))+")");
                            double xn = Double.NaN;
                            try{
                                xn = (multipleRootsFunction.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                            }catch (Exception e){
                                styleWrongMessage("Unexpected error posibly NaN");
                            }

                            if (errorRel)
                                error = Math.abs(xn - xa) / xn;
                            else
                                error = Math.abs(xn - xa);
                            xa = xn;
                            cont++;
                            MultipleRoots iteNext = new MultipleRoots(String.valueOf(cont), String.valueOf(normalTransformation(xa)), String.valueOf(normalTransformation(y0)), String.valueOf(normalTransformation(y0p1)), String.valueOf(normalTransformation(y0p2)), String.valueOf(cientificTransformation(error)));
                            listValues.add(iteNext);
                            listValuesIteNext.add(String.valueOf(xa));
                            listValuesIteNext.add(String.valueOf(y0));
                            listValuesIteNext.add(String.valueOf(y0p1));
                            listValuesIteNext.add(String.valueOf(y0p2));
                            listValuesIteNext.add(String.valueOf(cientificTransformation(error)));
                            completeList.add(listValuesIteNext);
                        }
                        listValues.add(new MultipleRoots("","","","","",""));
                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphSerie(function.getExpression(),0,xa,color);
                        if (y0 == 0) {
                            color = poolColors.remove(0);
                            poolColors.add(color);
                            graphPoint(xa,y0,color);
                            styleCorrectMessage(normalTransformation(xa) + " is a root");
                        } else if (error <= tol) {
                            color = poolColors.remove(0);
                            poolColors.add(color);
                            graphPoint(xa,y0,color);

                            styleCorrectMessage(normalTransformation(xa) + " is an aproximate root");
                        } else {
                            styleWrongMessage("The method failed in iteration: "+ite);
                        }
                    } else {
                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphPoint(x0,y0,color);
                        styleCorrectMessage(normalTransformation(x0) + " is an aproximate root");
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






