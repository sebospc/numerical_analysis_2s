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
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.Bisection;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.IncrementalSearch;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.IncrementalSearchListAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;
import java.text.DecimalFormat;
import java.util.Locale;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.example.sacrew.numericov4.utilMethods.functionRevision;
import static com.example.sacrew.numericov4.utilMethods.graphPoint;

/**
 * A simple {@link Fragment} subclass.
 */
public class incrementalSearchFragment extends Fragment {


    public incrementalSearchFragment() {
        // Required empty public constructor
    }

    private View view;
    private Button runIncremental;
    private Button runHelp;
    private ImageButton runChart;
    private GraphView graph;
    private Expression function;
    private AutoCompleteTextView textFunction;
    private TextView xValue;
    private TextView delta;
    private TextView iter;
    private ListView listView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       try{
        view = inflater.inflate(R.layout.fragment_incremental_search,container,false);
        }catch (InflateException e){
            // ignorable
        }
        runIncremental = view.findViewById(R.id.runIncremental);
        runHelp = view.findViewById(R.id.runHelp);
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
                execute();
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
    public void executeHelp(){
        Intent i = new Intent(getContext().getApplicationContext(), popUpIncrementalSearch.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(){

        boolean error = false;
        Double x = 0.0;
        Double deltaPrim = 0.0;
        int ite = 0;
        try{
            String originalFunc = textFunction.getText().toString();
            this.function = new Expression(functionRevision(originalFunc));

            (function.with("x", BigDecimal.valueOf(1)).eval()).doubleValue();
            if(!graphFragment.allFunctions.contains(originalFunc)){
                graphFragment.allFunctions.add(originalFunc);
                textFunction.setAdapter(new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
            }
        }catch (Exception e){
            textFunction.setError("Invalid function");
            error = true;
        }
        try{
            x = Double.parseDouble(xValue.getText().toString());
            (function.with("x", BigDecimal.valueOf(x)).eval()).doubleValue();
        }catch(Exception e){
            xValue.setError("Invalid x");
            error = true;
        }
        try{
            deltaPrim = Double.parseDouble(delta.getText().toString());
        }catch (Exception e){
            delta.setError("Invalid delta");
            error = true;
        }

        try {
            ite = Integer.parseInt(iter.getText().toString());
        }catch (Exception e){
            iter.setError("Wrong iterations");
            error = true;
        }
        if(!error) {
            incrementalSearchMethod(x, deltaPrim, ite);
        }
    }

    public static String convertirCientifica(double val){
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("0.##E0");
        return num.format(val);
    }

    public static String convertirNormal(double val){
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("0.##");
        return num.format(val);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void incrementalSearchMethod(Double x0, Double delta, int ite) {
        graph.removeAllSeries();
        function.setPrecision(100);
        ArrayList<IncrementalSearch> listValues = new ArrayList<>();
        IncrementalSearch titles = new IncrementalSearch("n", "Xn", "f(Xn)");
        listValues.add(titles);
            if (delta != 0) {
                if (ite > 0) {
                    double y0 = (function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    IncrementalSearch iteZero = new IncrementalSearch(String.valueOf(0),String.valueOf(convertirNormal(x0)),String.valueOf(convertirCientifica(y0)));
                    listValues.add(iteZero);
                    if (y0 != 0) {
                        int cont = 1;
                        double x1 = x0 + delta;
                        double y1 = (function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                        LineGraphSeries<DataPoint> serie = new LineGraphSeries<>();
                        serie.appendData(new DataPoint(x1, y1), false, ite);
                        IncrementalSearch iterFirst = new IncrementalSearch(String.valueOf(cont),String.valueOf(convertirNormal(x1)),String.valueOf(convertirCientifica(y1)));
                        listValues.add(iterFirst);
                        while (((y1 * y0) > 0) && (cont < ite)) {
                            cont++;
                            x0 = x1;
                            y0 = y1;
                            x1 = x0 + delta;
                            y1 = (function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                            if (delta >= 0)
                                serie.appendData(new DataPoint(x1, y1), false, ite);
                            else {
                                // no se puede graficar funciones al reves :(
                            }
                            IncrementalSearch iterNext = new IncrementalSearch(String.valueOf(cont),String.valueOf(convertirNormal(x1)),String.valueOf(convertirCientifica(y1)));
                            listValues.add(iterNext);
                        }

                        graph.addSeries(serie);
                        if (y1 == 0) {
                            graphPoint(x1, y1, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                            Toast.makeText(getContext(), convertirNormal(x1) + " is a root", Toast.LENGTH_SHORT).show();
                            //System.out.println(x1 + " is a root");
                        } else if (y1 * y0 < 0) {
                            Toast.makeText(getContext(), "[" + convertirNormal(x0) + ", " + convertirNormal(x1) + "] is an interval with root", Toast.LENGTH_SHORT).show();
                            //System.out.println("[" + x0 + ", " + x1 + "] is an interval");
                        } else {
                            Toast.makeText(getContext(), "Failed the interval!", Toast.LENGTH_SHORT).show();
                            // System.out.println("Failed the interval!");
                        }
                    } else {
                        graphPoint(x0, y0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                        Toast.makeText(getContext(), convertirNormal(x0) + " is a root", Toast.LENGTH_SHORT).show();
                        //System.out.println(x0 + " is a root");
                    }

            } else {
                    iter.setError("Iterate needs be >0");
                    Toast.makeText(getContext(), "Iterate needs be >0", Toast.LENGTH_SHORT).show();
                }

        }else {
                this.delta.setError("Delta cannot be zero");
                Toast.makeText(getContext(), "Delta cannot be zero", Toast.LENGTH_SHORT).show();
            }
        IncrementalSearchListAdapter adapter = new IncrementalSearchListAdapter(getContext(), R.layout.incremental_search_list_adapter, listValues);
        listView.setAdapter(adapter);
    }
//
    }



