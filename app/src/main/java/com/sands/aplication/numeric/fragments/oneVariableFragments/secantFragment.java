package com.sands.aplication.numeric.fragments.oneVariableFragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sands.aplication.numeric.R;
import com.sands.aplication.numeric.fragments.customPopUps.popUpSecant;
import com.sands.aplication.numeric.fragments.listViewCustomAdapter.Secant;
import com.sands.aplication.numeric.fragments.listViewCustomAdapter.SecantListAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.sands.aplication.numeric.fragments.homeFragment.poolColors;


/**
 * A simple {@link Fragment} subclass.
 */
public class secantFragment extends baseOneVariableFragments {

    private View view;
    private ListView listView;
    private EditText xi, xs;


    public secantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        xi = view.findViewById(R.id.xi);
        xs = view.findViewById(R.id.xs);


        registerEditText(xi, getContext(), getActivity());
        registerEditText(xs, getContext(), getActivity());
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpSecant.class);
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
            // Titles tables
            ArrayList<Secant> listValues = new ArrayList<>();
            Secant titles = new Secant("n", "Xn", "f(Xn)", "Error");
            listValues.add(titles);
            listValuesTitles = new LinkedList<>();
            listValuesTitles.add("Xn");
            listValuesTitles.add("f(Xn)");
            listValuesTitles.add("Error");
            completeList = new LinkedList<>();
            if (tol >= 0) {
                if (ite > 0) {
                    double fx0 = Double.NaN;
                    try {
                        fx0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    } catch (Exception e) {
                        styleWrongMessage("Unexpected error posibly nan");
                    }
                    if (fx0 != 0) {
                        double fx1 = Double.NaN;
                        try {
                            fx1 = (this.function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                        } catch (Exception e) {
                            styleWrongMessage("Unexpected error posibly nan");
                        }
                        double error = tol + 1;
                        double den = fx1 - fx0;
                        // Small table
                        Secant iteZero = new Secant(String.valueOf(0), String.valueOf(normalTransformation(x0)), String.valueOf(cientificTransformation(fx0)), String.valueOf(cientificTransformation(error)));
                        listValues.add(iteZero);
                        Secant iteFirst = new Secant(String.valueOf(0), String.valueOf(normalTransformation(x1)), String.valueOf(cientificTransformation(fx1)), String.valueOf(cientificTransformation(error)));
                        listValues.add(iteFirst);
                        // Big table
                        List<String> listValuesIteZero = new LinkedList<>();
                        listValuesIteZero.add(String.valueOf(x0));
                        listValuesIteZero.add(String.valueOf(cientificTransformation(fx0)));
                        listValuesIteZero.add(String.valueOf(""));
                        completeList.add(listValuesIteZero);
                        List<String> listValuesIteFirst = new LinkedList<>();
                        listValuesIteFirst.add(String.valueOf(x1));
                        listValuesIteFirst.add(String.valueOf(cientificTransformation(fx1)));
                        listValuesIteFirst.add(String.valueOf(""));
                        completeList.add(listValuesIteFirst);
                        int cont = 0;
                        calc = true;
                        while (fx1 != 0 && error > tol && den != 0 && cont < ite) {

                            ArrayList<String> listValuesIteNext = new ArrayList<String>();
                            Double aux2 = x1 - fx1 * ((x1 - x0) / den);
                            if (errorRel)
                                error = Math.abs(aux2 - x1) / aux2;
                            else
                                error = Math.abs(aux2 - x1);
                            x0 = x1;
                            fx0 = fx1;
                            x1 = aux2;
                            try {
                                fx1 = Double.NaN;
                                fx1 = (this.function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                            } catch (Exception e) {
                                styleWrongMessage("Unexpected error posibly nan");
                            }

                            den = fx1 - fx0;
                            // Small table
                            cont++;
                            Secant iteNext = new Secant(String.valueOf(cont), String.valueOf(normalTransformation(x1)), String.valueOf(cientificTransformation(fx1)), String.valueOf(cientificTransformation(error)));
                            listValues.add(iteNext);

                            // Big table
                            listValuesIteNext.add(String.valueOf(x1));
                            listValuesIteNext.add(String.valueOf(cientificTransformation(fx1)));
                            listValuesIteNext.add(String.valueOf(cientificTransformation(error)));
                            completeList.add(listValuesIteNext);


                        }
                        listValues.add(new Secant("", "", "", ""));

                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphSerie(function.getExpression(), x1, color);
                        if (fx1 == 0) {
                            color = poolColors.remove(0);
                            poolColors.add(color);
                            graphPoint(x1, fx1, color);
                            styleCorrectMessage(normalTransformation(x1) + " is a root");
                        } else if (error <= tol) {
                            color = poolColors.remove(0);
                            poolColors.add(color);
                            graphPoint(x1, fx1, color);
                            styleCorrectMessage(normalTransformation(x1) + " is an aproximate root");
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
