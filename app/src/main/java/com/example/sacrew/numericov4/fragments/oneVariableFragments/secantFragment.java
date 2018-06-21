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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpSecant;
import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.Secant;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.SecantListAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.fragments.homeFragment.poolColors;


/**
 * A simple {@link Fragment} subclass.
 */
public class secantFragment extends baseOneVariableFragments {

    private View view;
    private ListView listView;
    private EditText xi, xs;

    private ToggleButton errorToggle;

    public secantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_secant, container, false);
        } catch (InflateException e) {
            // ignorable
        }
        Button runSecant = view.findViewById(R.id.runSecant);
        runSecant.setOnClickListener(new View.OnClickListener() {
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
        xi = view.findViewById(R.id.xi);
        xs = view.findViewById(R.id.xs);
        errorToggle = view.findViewById(R.id.errorToggle);

        registerEditText(textFunction,getContext(),getActivity());
        registerEditText(iter,getContext(),getActivity());
        registerEditText(textError,getContext(),getActivity());
        registerEditText(xi,getContext(),getActivity());
        registerEditText(xs,getContext(),getActivity());
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpSecant.class);
        startActivity(i);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(boolean error, double errorValue, int ite) {
        this.xi.setError(null);
        this.xs.setError(null);
        double xiValue = 0.0;
        double xsValue = 0.0;
        try {
            xiValue = Double.parseDouble(xi.getText().toString());
        } catch (Exception e) {
            xi.setError("Invalid Xi");
            error = false;
        }
        try {
            xsValue = Double.parseDouble(xs.getText().toString());
        } catch (Exception e) {
            xs.setError("Invalid xs");
            error = false;
        }


        if (error) {
            if (errorToggle.isChecked()) {
                secantMethod(xiValue, xsValue, errorValue, ite, true);
            } else {
                secantMethod(xiValue, xsValue, errorValue, ite, false);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void secantMethod(Double x0, Double x1, Double tol, int ite, boolean errorRel) {
        try {

            function.setPrecision(100);
            ArrayList<Secant> listValues = new ArrayList<>();
            Secant titles = new Secant("n", "Xn", "f(Xn)", "Error");
            listValues.add(titles);
            listValuesTitles = new LinkedList<>();
            listValuesTitles.add("Xn");
            listValuesTitles.add("f(Xn)");
            listValuesTitles.add("Error");
            //TableViewModel.getTitles(listValuesTitles);
            completeList = new LinkedList<>();
            if (tol >= 0) {
                if (ite > 0) {
                    double fx0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    if (fx0 != 0) {
                        Double fx1 = (this.function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                        Double error = tol + 1;
                        Double aux0 = x0;
                        Double aux1 = x1;
                        Double den = fx1 - fx0;
                        Secant iteZero = new Secant(String.valueOf(0), String.valueOf(normalTransformation(x0)), String.valueOf(normalTransformation(fx0)), String.valueOf(cientificTransformation(error)));
                        listValues.add(iteZero);
                        Secant iteFirst = new Secant(String.valueOf(0), String.valueOf(normalTransformation(x1)), String.valueOf(normalTransformation(fx1)), String.valueOf(cientificTransformation(error)));
                        listValues.add(iteFirst);
                        List<String> listValuesIteZero = new LinkedList<>();
                        listValuesIteZero.add(String.valueOf(x0));
                        listValuesIteZero.add(String.valueOf(fx0));
                        listValuesIteZero.add(String.valueOf(""));
                        completeList.add(listValuesIteZero);
                        int cont = 0;
                        calc = true;
                        while (fx1 != 0 && den != 0 && error > tol && cont < ite) {
                            ArrayList<String> listValuesIteNext = new ArrayList<String>();
                            Double aux2 = aux1 - (((this.function.with("x", BigDecimal.valueOf(aux1))
                                    .eval().doubleValue())) * (aux1 - aux0) / den);
                            if (errorRel)
                                error = Math.abs(aux2 - aux1) / aux2;
                            else
                                error = Math.abs(aux2 - aux1);
                            aux0 = aux1;
                            fx0 = fx1;
                            aux1 = aux2;
                            try {
                                fx1 = Double.NaN;
                                fx1 = (this.function.with("x", BigDecimal.valueOf(aux1)).eval()).doubleValue();
                            } catch (Exception e) {
                                styleWrongMessage("Unexpected error posibly nan");
                            }

                            den = fx1 - fx0;
                            cont = cont + 1;
                            Secant iteNext = new Secant(String.valueOf(cont), String.valueOf(normalTransformation(aux0)), String.valueOf(normalTransformation(fx0)), String.valueOf(cientificTransformation(error)));
                            listValues.add(iteNext);
                            listValuesIteNext.add(String.valueOf(aux0));
                            listValuesIteNext.add(String.valueOf(fx0));
                            listValuesIteNext.add(String.valueOf(cientificTransformation(error)));
                            completeList.add(listValuesIteNext);
                        }
                        listValues.add(new Secant("", "", "", ""));

                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphSerie(function.getExpression(), 0, aux1, color);
                        if (fx1 == 0) {
                            color = poolColors.remove(0);
                            poolColors.add(color);
                            graphPoint(aux1, fx1, color);
                            styleCorrectMessage(normalTransformation(aux1) + " is a root");
                        } else if (error <= tol) {
                            color = poolColors.remove(0);
                            poolColors.add(color);
                            graphPoint(aux1, fx1, color);
                            styleCorrectMessage(normalTransformation(aux1) + " is an aproximate root");
                        } else {
                            styleWrongMessage("The method failed in iteration " + cont);
                        }
                    } else {
                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphPoint(x0, fx0, color);
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
            SecantListAdapter adapter = new SecantListAdapter(getContext(), R.layout.list_adapter_secant, listValues);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            styleWrongMessage("Unexpected error posibly nan");
        }
    }

}
