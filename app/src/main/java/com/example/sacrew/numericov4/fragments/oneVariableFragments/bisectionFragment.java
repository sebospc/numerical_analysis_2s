package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpBisection;
import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.Bisection;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.BisectionListAdapter;
import com.example.sacrew.numericov4.fragments.tableview.TableViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.fragments.homeFragment.poolColors;


/**
 * A simple {@link Fragment} subclass.
 */
public class bisectionFragment extends baseOneVariableFragments {


    public bisectionFragment() {
        // Required empty public constructor
    }

    public TextView textViewXm, textViewMessage;
    private EditText xi, xs;


    private ListView listView;
    public LinearLayout basicSection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_bisection, container, false);
        } catch (InflateException e) {
            // ignorable
        }
        Button runBisection = view.findViewById(R.id.runBisection);
        runBisection.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                cleanGraph();
                bootStrap();
                //execute();
            }
        });
        Button runChart = view.findViewById(R.id.runChart);
        runChart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                executeChart(getContext());
            }
        });
        listView = view.findViewById(R.id.listView);
        Button runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        textViewMessage = view.findViewById(R.id.textViewMessage);
        textViewXm = view.findViewById(R.id.textViewXm);
        xi = view.findViewById(R.id.xi);
        xs = view.findViewById(R.id.xs);

        registerEditText(xi,getContext(),getActivity());
        registerEditText(xs,getContext(),getActivity());
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpBisection.class);
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
                bisectionMethod(xiValue, xsValue, errorValue, ite, true);
            } else {
                bisectionMethod(xiValue, xsValue, errorValue, ite, false);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bisectionMethod(double xi, double xs, double tol, int ite, boolean errorRel) {
        double initial = xs;
        function.setPrecision(100);
        ArrayList<Bisection> listValues = new ArrayList<>();
        Bisection titles = new Bisection("n", "Xi", "Xs", "Xm", "f(Xm)", "Error");
        listValues.add(titles);
        listValuesTitles = new LinkedList<>();
        listValuesTitles.add("Xi");
        listValuesTitles.add("Xs");
        listValuesTitles.add("Xm");
        listValuesTitles.add("f(Xm)");
        listValuesTitles.add("Error");
        TableViewModel.getTitles(listValuesTitles);
        completeList = new LinkedList<>();
        if (tol >= 0) {
            if (ite > 0) {
                double yi = (this.function.with("x", BigDecimal.valueOf(xi)).eval()).doubleValue();
                if (yi != 0) {
                    double ys = (this.function.with("x", BigDecimal.valueOf(xs)).eval()).doubleValue();
                    if (ys != 0) {
                        if (yi * ys < 0) {
                            double xm = (xi + xs) / 2;
                            double ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();
                            double error = tol + 1;
                            Bisection iteZero = new Bisection(String.valueOf(0), String.valueOf(normalTransformation(xi)), String.valueOf(normalTransformation(xs)), String.valueOf(normalTransformation(xm)), String.valueOf(cientificTransformation(ym)), String.valueOf(cientificTransformation(error)));
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
                            calc= true;
                            while ((ym != 0) && (error > tol) && (cont < ite)) {
                                ArrayList<String> listValuesIteNext = new ArrayList<String>();
                                if (yi * ym < 0) {
                                    xs = xm;
                                    ys = ym;
                                } else {
                                    xi = xm;
                                    yi = ym;
                                }
                                xaux = xm;
                                xm = (xi + xs) / 2;
                                ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();

                                graphPoint(xm, ym,  Color.parseColor("#FA4659"));
                                if (errorRel) {
                                    error = Math.abs(xm - xaux) / xm;
                                } else {
                                    error = Math.abs(xm - xaux);
                                }
                                cont++;
                                Bisection iteNext = new Bisection(String.valueOf(cont), String.valueOf(normalTransformation(xi)), String.valueOf(normalTransformation(xs)), String.valueOf(normalTransformation(xm)), String.valueOf(cientificTransformation(ym)), String.valueOf(cientificTransformation(error)));
                                listValues.add(iteNext);
                                listValuesIteNext.add(String.valueOf(xi));
                                listValuesIteNext.add(String.valueOf(xs));
                                listValuesIteNext.add(String.valueOf(xm));
                                listValuesIteNext.add(String.valueOf(cientificTransformation(ym)));
                                listValuesIteNext.add(String.valueOf(cientificTransformation(error)));
                                completeList.add(listValuesIteNext);
                            }
                            listValues.add(new Bisection("","","","","",""));
                            int color = poolColors.remove(0);
                            poolColors.add(color);
                            graphSerie(function.getExpression(),0, initial,color);
                            if (ym == 0) {
                                graphPoint(xm,ym,Color.GREEN);
                                styleCorrectMessage(normalTransformation(xm) + " is an aproximate root");
                            } else if (error < tol) {
                                graphPoint(xm,ym,Color.GREEN);
                                styleCorrectMessage(normalTransformation(xaux) + " is an aproximate root");
                            } else {
                                styleWrongMessage("The method failed with "+ite+" iterations!");
                            }

                        } else {
                            styleWrongMessage("Bad interval");

                        }
                    } else {
                        styleCorrectMessage(normalTransformation(xs) + " is an aproximate root");
                        graphPoint(xs, ys,Color.GREEN);
                    }
                } else {
                    graphPoint(xi,yi,Color.GREEN);
                    styleWrongMessage("The method does not converge");
                }
            } else {
                iter.setError("Wrong iterates");
                styleWrongMessage("Wrong iterates");
            }
        } else {
            textError.setError("Tolerance must be > 0");
            styleWrongMessage("Tolerance must be > 0");
        }

        BisectionListAdapter adapter = new BisectionListAdapter(getContext(), R.layout.list_adapter_bisection, listValues);
        listView.setAdapter(adapter);
    }
}


