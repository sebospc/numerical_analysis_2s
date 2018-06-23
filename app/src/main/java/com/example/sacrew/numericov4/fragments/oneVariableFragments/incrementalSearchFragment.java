package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.support.v4.app.Fragment;
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

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpIncrementalSearch;
import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.IncrementalSearch;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.IncrementalSearchListAdapter;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



import static com.example.sacrew.numericov4.fragments.homeFragment.poolColors;


/**
 * A simple {@link Fragment} subclass.
 */
public class incrementalSearchFragment extends baseOneVariableFragments {


    public incrementalSearchFragment() {
        // Required empty public constructor
    }

    private View view;
    private EditText xValue;
    private EditText delta;
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
        xValue = view.findViewById(R.id.x_value);
        delta = view.findViewById(R.id.delta);
        iter = view.findViewById(R.id.iterations);
        runIncremental.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                cleanGraph();
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

        registerEditText(xValue,getContext(),getActivity());
        registerEditText(delta,getContext(),getActivity());
        registerEditText(iter,getContext(),getActivity());
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
            if(error) updateMyFunctions(originalFunc);
            this.function = new Expression(functionRevision(originalFunc));

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
        function.setPrecision(100);
        try {
            ArrayList<IncrementalSearch> listValues = new ArrayList<>();
            IncrementalSearch titles = new IncrementalSearch("n", "Xn", "f(Xn)");
            listValues.add(titles);
            listValuesTitles = new LinkedList<>();
            listValuesTitles.add("Xn");
            listValuesTitles.add("f(Xn)");
            completeList = new LinkedList<>();
            if (delta != 0) {
                if (ite > 0) {
                    double y0 = (function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    IncrementalSearch iteZero = new IncrementalSearch(String.valueOf(0), String.valueOf(normalTransformation(x0)), String.valueOf(cientificTransformation(y0)));
                    listValues.add(iteZero);
                    List<String> listValuesIteZero = new LinkedList<>();
                    listValuesIteZero.add(String.valueOf(x0));
                    listValuesIteZero.add(String.valueOf(cientificTransformation(y0)));
                    completeList.add(listValuesIteZero);
                    if (y0 != 0) {
                        int cont = 1;
                        double x1 = x0 + delta;
                        double y1 = (function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                        IncrementalSearch iterFirst = new IncrementalSearch(String.valueOf(cont), String.valueOf(normalTransformation(x1)), String.valueOf(cientificTransformation(y1)));
                        listValues.add(iterFirst);
                        List<String> listValuesFirst = new LinkedList<>();
                        listValuesFirst.add(String.valueOf(x1));
                        listValuesFirst.add(String.valueOf(cientificTransformation(y1)));
                        completeList.add(listValuesFirst);
                        calc= true;
                        while (((y1 * y0) > 0) && (cont < ite)) {
                            cont++;
                            x0 = x1;
                            y0 = y1;
                            x1 = x0 + delta;
                            y1 = (function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                            IncrementalSearch iterNext = new IncrementalSearch(String.valueOf(cont), String.valueOf(normalTransformation(x1)), String.valueOf(cientificTransformation(y1)));
                            listValues.add(iterNext);
                            List<String> listValuesNext = new LinkedList<>();
                            listValuesNext.add(String.valueOf(x1));
                            listValuesNext.add(String.valueOf(cientificTransformation(y1)));
                            completeList.add(listValuesNext);
                        }
                        listValues.add(new IncrementalSearch("","",""));
                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphSerie(function.getExpression(),0,x0,color);
                        if (y1 == 0) {
                            color = poolColors.remove(0);
                            poolColors.add(color);
                            graphPoint(x1,y1,color);
                            styleCorrectMessage(normalTransformation(x1) + " is a root");
                        } else if (y1 * y0 < 0) {
                            styleCorrectMessage("[" + normalTransformation(x0) + ", " + normalTransformation(x1) + "] is an interval with root");
                        }else{
                            styleWrongMessage("Root dont found");
                        }

                    } else {
                        int color = poolColors.remove(0);
                        poolColors.add(color);
                        graphPoint(x0,y0,color);
                        styleCorrectMessage(normalTransformation(x0) + " is a root");
                    }

                } else {
                    iter.setError("Iterate needs be >0");
                    styleWrongMessage("Iterate needs be >0");
                }
            } else {
                this.delta.setError("Delta cannot be zero");
                styleWrongMessage("Delta cannot be zero");
            }
            IncrementalSearchListAdapter adapter = new IncrementalSearchListAdapter(getContext(), R.layout.list_adapter_incremental_search, listValues);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            styleWrongMessage("Unexpected error posibly nan");
        }
    }
}



