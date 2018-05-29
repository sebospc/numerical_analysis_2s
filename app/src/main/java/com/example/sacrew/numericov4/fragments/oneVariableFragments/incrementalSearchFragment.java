package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpIncrementalSearch;
import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.IncrementalSearch;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.IncrementalSearchListAdapter;
import com.example.sacrew.numericov4.fragments.tableview.TableViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import org.apache.commons.math3.analysis.function.Exp;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class incrementalSearchFragment extends baseOneVariableFragments {


    public incrementalSearchFragment() {
        // Required empty public constructor
    }

    private View view;
    private GraphView graph;
    private TextView xValue;
    private TextView delta;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            view = inflater.inflate(R.layout.fragment_incremental_search, container, false);
        } catch (InflateException e) {
            // ignorable
        }
        Button runIncremental = view.findViewById(R.id.runIncremental);
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
        graph = view.findViewById(R.id.incrementalGraph);
        textFunction = view.findViewById(R.id.function);
        xValue = view.findViewById(R.id.x_value);
        delta = view.findViewById(R.id.delta);
        iter = view.findViewById(R.id.iterations);
        runIncremental.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                bootStrap();
            }
        });
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });


        textFunction.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpIncrementalSearch.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void bootStrap(){
        boolean error = true;
        int ite = 0;
            String originalFunc = textFunction.getText().toString();
            error = checkSyntax(originalFunc,textFunction);
            this.function = new Expression(functionRevision(originalFunc));
            if(!graphFragment.allFunctions.contains(originalFunc) && error) {
                graphFragment.allFunctions.add(originalFunc);
                textFunction.setAdapter(new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
            }

        try {
            ite = Integer.parseInt(iter.getText().toString());
        }catch (Exception e){
            iter.setError("Wrong iterations");
            error = false;
        }

        execute(error,ite);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(boolean error, int ite) {

        Double x = 0.0;
        Double deltaPrim = 0.0;

        try {
            x = Double.parseDouble(xValue.getText().toString());
        } catch (Exception e) {
            xValue.setError("Invalid x");
            error = false;
        }
        try {
            deltaPrim = Double.parseDouble(delta.getText().toString());
        } catch (Exception e) {
            delta.setError("Invalid delta");
            error = false;
        }
        if (error) {
            incrementalSearchMethod(x, deltaPrim, ite);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void incrementalSearchMethod(Double x0, Double delta, int ite) {
        graph.removeAllSeries();
        function.setPrecision(100);
        try {
            ArrayList<IncrementalSearch> listValues = new ArrayList<>();
            IncrementalSearch titles = new IncrementalSearch("n", "Xn", "f(Xn)");
            listValues.add(titles);
            List<String> listValuesTitles = new LinkedList<>();
            listValuesTitles.add("Xn");
            listValuesTitles.add("f(Xn)");
            TableViewModel.getTitles(listValuesTitles);
            List<List<String>> completeList = new LinkedList<>();
            if (delta != 0) {
                if (ite > 0) {
                    double y0 = (function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    IncrementalSearch iteZero = new IncrementalSearch(String.valueOf(0), String.valueOf(convertirNormal(x0)), String.valueOf(convertirCientifica(y0)));
                    listValues.add(iteZero);
                    List<String> listValuesIteZero = new LinkedList<>();
                    listValuesIteZero.add(String.valueOf(x0));
                    listValuesIteZero.add(String.valueOf(y0));
                    completeList.add(listValuesIteZero);
                    if (y0 != 0) {
                        int cont = 1;
                        double x1 = x0 + delta;
                        double y1 = (function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                        IncrementalSearch iterFirst = new IncrementalSearch(String.valueOf(cont), String.valueOf(convertirNormal(x1)), String.valueOf(convertirCientifica(y1)));
                        listValues.add(iterFirst);
                        List<String> listValuesFirst = new LinkedList<>();
                        listValuesFirst.add(String.valueOf(x0));
                        listValuesFirst.add(String.valueOf(y0));
                        completeList.add(listValuesFirst);
                        while (((y1 * y0) > 0) && (cont < ite)) {
                            cont++;
                            x0 = x1;
                            y0 = y1;
                            x1 = x0 + delta;
                            y1 = (function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                            IncrementalSearch iterNext = new IncrementalSearch(String.valueOf(cont), String.valueOf(convertirNormal(x1)), String.valueOf(convertirCientifica(y1)));
                            listValues.add(iterNext);
                            List<String> listValuesNext = new LinkedList<>();
                            listValuesNext.add(String.valueOf(x0));
                            listValuesNext.add(String.valueOf(y0));
                            completeList.add(listValuesNext);
                        }
                        TableViewModel.getCeldas(completeList);


                        if (y1 == 0) {
                            graphPoint(x1, y1, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                            Toast.makeText(getContext(), convertirNormal(x1) + " is a root", Toast.LENGTH_SHORT).show();

                        } else if (y1 * y0 < 0) {
                            Toast.makeText(getContext(), "[" + convertirNormal(x0) + ", " + convertirNormal(x1) + "] is an interval with root", Toast.LENGTH_SHORT).show();
                            graphSerie(x0-0.2,x1+0.2,function.getExpression(),graph,getResources().getColor(R.color.colorPrimary));
                        }

                    } else {
                        graphPoint(x0, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                        Toast.makeText(getContext(), convertirNormal(x0) + " is a root", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    iter.setError("Iterate needs be >0");
                }
                calc= true;
            } else {
                this.delta.setError("Delta cannot be zero");
            }
            IncrementalSearchListAdapter adapter = new IncrementalSearchListAdapter(getContext(), R.layout.list_adapter_incremental_search, listValues);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Unexpected error posibly nan", Toast.LENGTH_SHORT).show();
        }


    }
}



