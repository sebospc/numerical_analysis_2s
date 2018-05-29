package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.tableview.TableViewModel;
import com.example.sacrew.numericov4.fragments.MainActivityTable;
import com.example.sacrew.numericov4.fragments.creditsFragment;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpBisection;
import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.Bisection;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.BisectionListAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class bisectionFragment extends baseOneVariableFragments {


    public bisectionFragment() {
        // Required empty public constructor
    }

    private GraphView graph;
    public TextView textViewXm, textViewMessage;
    private EditText xi, xs;

    private ToggleButton errorToggle;
    private ListView listView;


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
        Button runHelp = view.findViewById(R.id.runHelp);
        listView = view.findViewById(R.id.listView);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        graph = view.findViewById(R.id.bisectionGraph);
        textFunction = view.findViewById(R.id.function);
        textViewMessage = view.findViewById(R.id.textViewMessage);
        textViewXm = view.findViewById(R.id.textViewXm);
        iter = view.findViewById(R.id.iterations);
        textError = view.findViewById(R.id.error);
        xi = view.findViewById(R.id.xi);
        xs = view.findViewById(R.id.xs);
        errorToggle = view.findViewById(R.id.errorToggle);

        textFunction.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));

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
        graph.removeAllSeries();
        function.setPrecision(100);
        ArrayList<Bisection> listValues = new ArrayList<>();
        List<String> listValuesTitles = new LinkedList<>();
        //String matrix[][];
        Bisection titles = new Bisection("n", "Xi", "Xs", "Xm", "f(Xm)", "Error");
        listValues.add(titles);
        listValuesTitles.add("Xi");
        listValuesTitles.add("Xs");
        listValuesTitles.add("Xm");
        listValuesTitles.add("f(Xm)");
        listValuesTitles.add("Error");
        TableViewModel.getTitles(listValuesTitles);
        List<List<String>> completeList = new LinkedList<>();
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
                            Bisection iteZero = new Bisection(String.valueOf(0), String.valueOf(convertirNormal(xi)), String.valueOf(convertirNormal(xs)), String.valueOf(convertirNormal(xm)), String.valueOf(convertirCientifica(ym)), String.valueOf(convertirCientifica(error)));
                            listValues.add(iteZero);
                            List<String> listValuesIteZero = new LinkedList<>();
                            listValuesIteZero.add(String.valueOf(xi));
                            listValuesIteZero.add(String.valueOf(xs));
                            listValuesIteZero.add(String.valueOf(xm));
                            listValuesIteZero.add(String.valueOf(ym));
                            listValuesIteZero.add(String.valueOf(convertirCientifica(error)));
                            //TableViewModel.getCeldas(listValuesIteZero);


                            //TableViewModel.getSimpleCellList();
                            int cont = 1;
                            double xaux = xm;



                            completeList.add(listValuesIteZero);
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
                                graphPoint(xm, ym, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#FA4659", false);
                                if (errorRel) {
                                    error = Math.abs(xm - xaux) / xm;
                                } else {
                                    error = Math.abs(xm - xaux);
                                }
                                Bisection iteNext = new Bisection(String.valueOf(cont), String.valueOf(convertirNormal(xi)), String.valueOf(convertirNormal(xs)), String.valueOf(convertirNormal(xm)), String.valueOf(convertirCientifica(ym)), String.valueOf(convertirCientifica(error)));
                                listValues.add(iteNext);
                                listValuesIteNext.add(String.valueOf(xi));
                                listValuesIteNext.add(String.valueOf(xs));
                                listValuesIteNext.add(String.valueOf(xm));
                                listValuesIteNext.add(String.valueOf(ym));
                                listValuesIteNext.add(String.valueOf(convertirCientifica(error)));
                                completeList.add(listValuesIteNext);
                                cont++;
                            }
                            TableViewModel.getCeldas(completeList);
                            if (ym == 0) {
                                graphPoint(xm, ym, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                            } else if (error < tol) {
                                graphPoint(xaux, ym, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                                graphSerie(xaux-0.2, xaux+0.2, this.function.getExpression(), graph, Color.BLUE);
                                Toast.makeText(getContext(), convertirNormal(xaux) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                            }
                            calc= true;
                        } else {
                            Toast.makeText(getContext(), "The interval dont have root", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getContext(), convertirNormal(xs) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                        graphPoint(xs, ys, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                    }
                } else {
                    graphPoint(xi, yi, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);

                }
            } else {
                iter.setError("Wrong iterates");
            }
        } else {
            textError.setError("Tolerance must be > 0");
        }

        BisectionListAdapter adapter = new BisectionListAdapter(getContext(), R.layout.list_adapter_bisection, listValues);
        listView.setAdapter(adapter);
    }
}


