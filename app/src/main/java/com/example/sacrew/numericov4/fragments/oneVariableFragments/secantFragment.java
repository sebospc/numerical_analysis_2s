package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpSecant;
import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.Secant;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.SecantListAdapter;
import com.example.sacrew.numericov4.fragments.tableview.TableViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;



/**
 * A simple {@link Fragment} subclass.
 */
public class secantFragment extends baseOneVariableFragments {

    private GraphView graph;

    private View view;
    private ListView listView;
    private TextView xi,xs;

    private ToggleButton errorToggle;
    public secantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
        view = inflater.inflate(R.layout.fragment_secant, container, false);
        }catch (InflateException e){
            // ignorable
        }
        Button runSecant = view.findViewById(R.id.runSecant);
        runSecant.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
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
        graph = view.findViewById(R.id.secantGraph);
        textFunction = view.findViewById(R.id.function);
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
    public void executeHelp(){
        Intent i = new Intent(getContext().getApplicationContext(), popUpSecant.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(boolean error, double errorValue, int ite){
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
            graph.removeAllSeries();

            function.setPrecision(100);
            ArrayList<Secant> listValues = new ArrayList<>();
        Secant titles = new Secant("n", "Xn", "f(Xn)", "f'(Xn)", "f''(Xn)", "Error");
        listValues.add(titles);
            List<String> listValuesTitles = new LinkedList<>();
            listValuesTitles.add("Xn");
            listValuesTitles.add("f(Xn)");
            listValuesTitles.add("f'(Xn)");
            listValuesTitles.add("f''(Xn)");
            listValuesTitles.add("Error");
            TableViewModel.getTitles(listValuesTitles);
            List<List<String>> completeList = new LinkedList<>();
            if (tol >= 0) {
                if (ite > 0) {
                    double fx0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                    if (fx0 != 0) {
                        Double fx1 = (this.function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                        Double error = tol+1;
                        int cont = 0;
                        Double aux0 = x0;
                        Double aux1 = x1;
                        Double den = fx1-fx0;
                        Secant iteZero = new Secant(String.valueOf(cont), String.valueOf(convertirNormal(x0)), String.valueOf(convertirCientifica(fx0)), String.valueOf(convertirCientifica(fx1)), String.valueOf(convertirCientifica(den)), String.valueOf(convertirCientifica(error)));
                        listValues.add(iteZero);
                        List<String> listValuesIteZero = new LinkedList<>();
                        listValuesIteZero.add(String.valueOf(x0));
                        listValuesIteZero.add(String.valueOf(fx0));
                        listValuesIteZero.add(String.valueOf(fx1));
                        listValuesIteZero.add(String.valueOf(den));
                        listValuesIteZero.add(String.valueOf(convertirCientifica(error)));
                        completeList.add(listValuesIteZero);
                        while(fx1 != 0 && den != 0 && error > tol && cont < ite) {
                            ArrayList<String> listValuesIteNext = new ArrayList<String>();
                            Double aux2 = aux1 - (((this.function.with("x", BigDecimal.valueOf(aux1))
                                    .eval().doubleValue()))* (aux1 - aux0) / den);
                            if (errorRel)
                                error = Math.abs(aux2 - aux1) / aux2;
                            else
                                error = Math.abs(aux2 - aux1);
                            aux0 = aux1;
                            fx0 = fx1;
                            aux1 = aux2;
                            fx1  = (this.function.with("x", BigDecimal.valueOf(aux1)).eval()).doubleValue();
                            den = fx1 - fx0;
                            cont = cont + 1;
                            Secant iteNext= new Secant(String.valueOf(cont), String.valueOf(convertirNormal(aux0)), String.valueOf(convertirCientifica(fx0)), String.valueOf(convertirCientifica(fx1)), String.valueOf(convertirCientifica(den)), String.valueOf(convertirCientifica(error)));
                            listValues.add(iteNext);
                            listValuesIteNext.add(String.valueOf(x0));
                            listValuesIteNext.add(String.valueOf(fx0));
                            listValuesIteNext.add(String.valueOf(fx1));
                            listValuesIteNext.add(String.valueOf(den));
                            listValuesIteNext.add(String.valueOf(convertirCientifica(error)));
                            completeList.add(listValuesIteNext);
                        }
                        TableViewModel.getCeldas(completeList);
                        calc= true;

                        if (fx1 == 0) {
                            graphSerie(aux1-0.2, aux1+0.2, function.getExpression(), graph, Color.BLUE);
                            graphPoint(aux1, fx1, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                            Toast.makeText(getContext(),  convertirNormal(aux1) + " is a root", Toast.LENGTH_SHORT).show();
                        } else if (error <= tol) {
                            graphSerie(aux1-0.2, aux1+0.2, function.getExpression(), graph, Color.BLUE);
                            fx1 = (this.function.with("x", BigDecimal.valueOf(aux1)).eval()).doubleValue();
                            graphPoint(aux1, fx1, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                            Toast.makeText(getContext(),  convertirNormal(aux1) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),  "Failedl!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        graphPoint(x0, fx0, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                        Toast.makeText(getContext(),  convertirNormal(x0) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    iter.setError("Wrong iterates");
                    Toast.makeText(getContext(),  "Wrong iterates!", Toast.LENGTH_SHORT).show();
                }
            } else {
                textError.setError("Tolerance must be > 0");

            }
            SecantListAdapter adapter = new SecantListAdapter(getContext(), R.layout.list_adapter_secant, listValues);
            listView.setAdapter(adapter);
        }catch(Exception e){
          //  Toast.makeText(getActivity(), "Unexpected error posibly nan", Toast.LENGTH_SHORT).show();
        }
    }

}
