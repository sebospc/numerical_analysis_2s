package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpBisection;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpIncrementalSearch;
import com.example.sacrew.numericov4.fragments.home;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.udojava.evalex.Expression;
import java.util.ArrayList;
import java.math.BigDecimal;

import static com.example.sacrew.numericov4.graphMethods.graphPoint;
import static com.example.sacrew.numericov4.graphMethods.graphSerie;

/**
 * A simple {@link Fragment} subclass.
 */
public class bisectionFragment extends Fragment {


    public bisectionFragment() {
        // Required empty public constructor
    }
    private Button runBisection;
    private Button runHelp;
    private GraphView graph;
    private Expression function;
    private View view;
    private TextView xi,xs,iter,textError;
    private AutoCompleteTextView textFunction;
    private ToggleButton errorToggle;
    private int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bisection,container,false);
        runBisection = view.findViewById(R.id.runBisection);
        runBisection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execute();
            }
        });
        runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        graph = view.findViewById(R.id.bisectionGraph);
        textFunction = view.findViewById(R.id.function);
        iter = view.findViewById(R.id.iterations);
        textError = view.findViewById(R.id.error);
        xi = view.findViewById(R.id.xi);
        xs = view.findViewById(R.id.xs);
        errorToggle = view.findViewById(R.id.errorToggle);

        textFunction.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, home.allFunctions));

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp(){
        Intent i = new Intent(getContext().getApplicationContext(), popUpBisection.class);
        startActivity(i);
    }
    public void execute(){
        this.xi.setError(null);
        this.xs.setError(null);
        boolean error = false;
        Double xiValue = 0.0;
        Double xsValue = 0.0;
        int ite = 0;
        Double errorValue= 0.0;

        try{
            this.function = new Expression(textFunction.getText().toString());
            (function.with("x", BigDecimal.valueOf(1)).eval()).doubleValue();

            if(!home.allFunctions.contains(function.getExpression())){
                home.allFunctions.add(function.getExpression());
                textFunction.setAdapter(new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, home.allFunctions));
            }
        }catch (Exception e){
            textFunction.setError("Invalid function");

            error = true;
        }
        try{
            xiValue = Double.parseDouble(xi.getText().toString());
        }catch(Exception e){
            xi.setError("Invalid Xi");
            error = true;
        }
        try{
            xsValue = Double.parseDouble(xs.getText().toString());
        }catch (Exception e){
            xs.setError("Invalid xs");
            error = true;
        }

        try {
            ite = Integer.parseInt(iter.getText().toString());
        }catch (Exception e){
            iter.setError("Wrong iterations");
            error = true;
        }
        try {
            errorValue = new Expression(textError.getText().toString()).eval().doubleValue();
            System.out.println("error value  "+errorValue);
        }catch (Exception e){
            textError.setError("Invalid error value");
        }
        if(!error) {
            if(errorToggle.isChecked()){
                bisectionMethod(xiValue,xsValue,errorValue,ite,true);
            }else{
                bisectionMethod(xiValue,xsValue,errorValue,ite,false);
            }
        }
    }

    public void bisectionMethod(Double xi,Double xs,Double tol,int ite,boolean errorRel) {
        graph.removeAllSeries();
        function.setPrecision(100);
        com.example.sacrew.numericov4.fragments.Tabla tabla = new com.example.sacrew.numericov4.fragments.Tabla(getActivity(), (android.widget.TableLayout) view.findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tablaBisection);
        ArrayList<String> elementos = new java.util.ArrayList<String>();
        for(int i=0; i < 1; i++){
        elementos.add(0, String.valueOf(0));
        elementos.add(1, String.valueOf(xi));
        elementos.add(2, String.valueOf(xs));

        if(tol >= 0){
            if(ite > 0){
                double yi = (this.function.with("x", BigDecimal.valueOf(xi)).eval()).doubleValue();
                if(yi != 0){
                    double ys = (this.function.with("x", BigDecimal.valueOf(xs)).eval()).doubleValue();
                    if(ys != 0){
                        if(yi*ys < 0){

                            double xm = (xi + xs) / 2;
                            elementos.add(3, String.valueOf(xm));
                            double ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();
                            elementos.add(4, String.valueOf(ym));
                            double error = tol + 1;
                            elementos.add(5, "");
                            tabla.agregarFilaTabla(elementos);
                            int cont = 1;

                            double xaux = xm;

                            graphSerie(xi,xs,this.function.getExpression(),graph,Color.BLUE);
                            while((ym != 0) && (error > tol) && (cont < ite)){
                                if(yi*ym < 0){
                                    xs = xm;
                                    ys = ym;
                                }else{
                                    xi = xm;
                                    yi = ym;
                                }
                                elementos.add(0, String.valueOf(cont));
                                elementos.add(1, String.valueOf(xi));
                                elementos.add(2, String.valueOf(xs));
                                xaux = xm;
                                xm = (xi + xs) / 2;
                                elementos.add(3, String.valueOf(xm));
                                ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();
                                elementos.add(4, String.valueOf(ym));
                                graphPoint(xm,ym,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#FA4659",false);
                                if(errorRel){
                                    error = Math.abs(xm - xaux)/xm;
                                    elementos.add(5, String.valueOf(error));
                                }else{
                                    error = Math.abs(xm - xaux);
                                    elementos.add(5, String.valueOf(error));
                                    }
                                cont++;
                                tabla.agregarFilaTabla(elementos);
                                //elementos.add(0,String.valueOf(cont));
                                //tabla.agregarFilaTabla(elementos);
                            }

                            if(ym == 0){
                                graphPoint(xm,ym,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                            }else if(error < tol){
                                graphPoint(xaux,ym,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                                //System.out.println(xaux + " is an aproximate root");
                            }else{
                                //System.out.println("Failed!");
                            }
                        }else{
                            this.xi.setError("Failed the interval");
                            this.xs.setError("Failed the interval");
                            //System.out.println("Failed the interval!");

                        }
                    }else{
                        //System.out.println(xs + " is an aproximate root");
                        graphPoint(xs,ys,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                    }
                }else{
                    //System.out.println(xi + " is an aproximate root");
                    graphPoint(xi,yi,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#0E9577",true);
                }
            }else{
                iter.setError("Wrong iterates");
                //System.out.println("Wrong iterates!");
            }
        }else{
            textError.setError("Tolerance must be < 0");
            //System.out.println("Tolerance < 0");
        }
        }
    }

}


