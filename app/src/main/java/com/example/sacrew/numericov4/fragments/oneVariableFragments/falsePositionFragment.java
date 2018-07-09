package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpFalsePosition;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.FalsePosition;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.FalsePositionListAdapter;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.example.sacrew.numericov4.fragments.homeFragment.poolColors;


/**
 * A simple {@link Fragment} subclass.
 */
public class falsePositionFragment extends baseOneVariableFragments {


    private View view;
    private ListView listView;
    private EditText xi, xs;

    public falsePositionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_false_position, container, false);
        } catch (InflateException e) {
            //ojo
        }
        Button runFake = view.findViewById(R.id.runFalse);
        runFake.setOnClickListener(new View.OnClickListener() {
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
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpFalsePosition.class);
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

        try {
            errorValue = new Expression(textError.getText().toString()).eval().doubleValue();
        } catch (Exception e) {
            textError.setError("Invalid error value");
        }
        if (error) {
            if (errorToggle.isChecked()) {
                falsePosition(xiValue, xsValue, errorValue, ite, true);
            } else {
                falsePosition(xiValue, xsValue, errorValue, ite, false);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void falsePosition(double xi, double xs, double tol, int ite, boolean errorRel) {
        double initial = xs;
        function.setPrecision(100);
        ArrayList<FalsePosition> listValues = new ArrayList<>();
        FalsePosition titles = new FalsePosition("n", "Xi", "Xs", "Xm", "f(Xm)", "Error");
        listValues.add(titles);
        listValuesTitles = new LinkedList<>();
        listValuesTitles.add("Xi");
        listValuesTitles.add("Xs");
        listValuesTitles.add("Xm");
        listValuesTitles.add("f(Xm)");
        listValuesTitles.add("Error");
        completeList = new LinkedList<>();
        if (tol >= 0) {
            if (ite > 0) {
                double yi = (this.function.with("x", BigDecimal.valueOf(xi)).eval()).doubleValue();
                if (yi != 0) {
                    double ys = (this.function.with("x", BigDecimal.valueOf(xs)).eval()).doubleValue();
                    if (ys != 0) {
                        if (yi * ys < 0) {
                            double xm = xi - (yi * (xi - xs)) / (yi - ys);
                            double ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();
                            double error = tol + 1;
                            FalsePosition iteZero = new FalsePosition(String.valueOf(0), String.valueOf(normalTransformation(xi)), String.valueOf(normalTransformation(xs)), String.valueOf(normalTransformation(xm)), String.valueOf(cientificTransformation(ym)), String.valueOf(cientificTransformation(error)));
                            listValues.add(iteZero);
                            List<String> listValuesIteZero = new LinkedList<>();
                            listValuesIteZero.add(String.valueOf(xi));
                            listValuesIteZero.add(String.valueOf(xs));
                            listValuesIteZero.add(String.valueOf(xm));
                            listValuesIteZero.add(String.valueOf(cientificTransformation(ym)));
                            listValuesIteZero.add(String.valueOf(""));
                            int cont = 0;
                            double xaux = xm;
                            completeList.add(listValuesIteZero);
                            calc = true;
                            while ((ym != 0) && (error > tol) && (cont < ite)) {
                                if (yi * ym < 0) {
                                    xs = xm;
                                    ys = ym;
                                } else {
                                    xi = xm;
                                    yi = ym;
                                }
                                xaux = xm;
                                xm = xi - ((yi * (xi - xs)) / (yi - ys));
                                ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();
                                graphPoint(xm, ym, Color.parseColor("#FA4659"));

                                if (errorRel) {
                                    error = Math.abs(xm - xaux) / xm;
                                } else {
                                    error = Math.abs(xm - xaux);
                                }
                                cont++;
                                FalsePosition iteNext = new FalsePosition(String.valueOf(cont), String.valueOf(normalTransformation(xi)), String.valueOf(normalTransformation(xs)), String.valueOf(normalTransformation(xm)), String.valueOf(cientificTransformation(ym)), String.valueOf(cientificTransformation(error)));
                                listValues.add(iteNext);
                                ArrayList<String> listValuesIteNext = new ArrayList<String>();
                                listValuesIteNext.add(String.valueOf(xi));
                                listValuesIteNext.add(String.valueOf(xs));
                                listValuesIteNext.add(String.valueOf(xm));
                                listValuesIteNext.add(String.valueOf(cientificTransformation(ym)));
                                listValuesIteNext.add(String.valueOf(cientificTransformation(error)));
                                completeList.add(listValuesIteNext);
                            }
                            listValues.add(new FalsePosition("", "", "", "", "", ""));
                            int color = poolColors.remove(0);
                            poolColors.add(color);
                            graphSerie(function.getExpression(), initial, color);
                            if (ym == 0) {
                                color = poolColors.remove(0);
                                poolColors.add(color);
                                graphPoint(xm, ym, color);

                                styleCorrectMessage(normalTransformation(xm) + " is an aproximate root");
                            } else if (error < tol) {
                                color = poolColors.remove(0);
                                poolColors.add(color);
                                graphPoint(xm, ym, color);
                                styleCorrectMessage(normalTransformation(xaux) + " is an aproximate root");

                            } else {
                                styleWrongMessage("The method failed with " + ite + " iterations!");
                            }
                        } else {
                            styleWrongMessage("Bad interval");
                        }
                    } else {
                        styleCorrectMessage(normalTransformation(xs) + " is an aproximate root");
                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphPoint(xs, ys, color);
                        //graphPoint(xs, ys, PointsGraphSeries.Shape.POINT, graph, getActivity(), Color.parseColor("#0E9577"), true);
                    }
                } else {
                    styleCorrectMessage(normalTransformation(xi) + " is an aproximate root");
                    int color = poolColors.remove(0);
                    poolColors.add(color);
                    graphPoint(xi, yi, color);
                }
            } else {
                iter.setError("Wrong iterates");
                styleWrongMessage("Wrong iterates");
            }
        } else {
            textError.setError("Tolerance must be > 0");
            styleWrongMessage("Tolerance must be > 0");
        }
        FalsePositionListAdapter adapter = new FalsePositionListAdapter(getContext(), R.layout.list_adapter_false_position, listValues);
        listView.setAdapter(adapter);
    }


}


