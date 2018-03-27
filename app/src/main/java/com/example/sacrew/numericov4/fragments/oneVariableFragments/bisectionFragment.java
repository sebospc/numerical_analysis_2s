package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 */
public class bisectionFragment extends Fragment {


    public bisectionFragment() {
        // Required empty public constructor
    }
    private Button runBisection;
    private GraphView graph;
    private Expression function;
    private View view;
    private TextView xi,xs,iter,textFunction,textError;
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
        graph = view.findViewById(R.id.bisectionGraph);
        textFunction = view.findViewById(R.id.function);
        iter = view.findViewById(R.id.iterations);
        textError = view.findViewById(R.id.error);
        xi = view.findViewById(R.id.xi);
        xs = view.findViewById(R.id.xs);
        errorToggle = view.findViewById(R.id.errorToggle);

        return view;
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
        if(tol >= 0){
            if(ite > 0){
                double yi = (this.function.with("x", BigDecimal.valueOf(xi)).eval()).doubleValue();
                if(yi != 0){
                    double ys = (this.function.with("x", BigDecimal.valueOf(xs)).eval()).doubleValue();
                    if(ys != 0){
                        if(yi*ys < 0){

                            double xm = (xi + xs) / 2;
                            double ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();
                            double error = tol + 1;

                            int cont = 1;
                            double xaux = xm;
                            graphPharalelism(xi,xs);
                            while((ym != 0) && (error > tol) && (cont < ite)){
                                if(yi*ym < 0){
                                    xs = xm;
                                    ys = ym;
                                }else{
                                    xi = xm;
                                    yi = ym;
                                }
                                xaux = xm;
                                xm = (xi + xs) / 2;
                                ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();

                                if(errorRel)
                                    error = Math.abs(xm - xaux)/xm;
                                else
                                    error = Math.abs(xm - xaux);
                                cont++;
                            }

                            if(ym == 0){
                                PointsGraphSeries<DataPoint> root = new PointsGraphSeries<>(new DataPoint[] {
                                        new DataPoint(xm, ym)
                                });
                                root.setOnDataPointTapListener(new OnDataPointTapListener() {
                                    @Override
                                    public void onTap(Series series, DataPointInterface dataPoint) {
                                        Toast.makeText(getActivity(), "("+dataPoint.getX()+" , "+dataPoint.getY()+")", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                graph.addSeries(root);
                                root.setShape(PointsGraphSeries.Shape.POINT);
                                //System.out.println(xm + " is an aproximate root");
                                root.setColor(Color.GREEN);
                            }else if(error < tol){
                                PointsGraphSeries<DataPoint> root = new PointsGraphSeries<>(new DataPoint[] {
                                        new DataPoint(xaux, ym)
                                });
                                root.setOnDataPointTapListener(new OnDataPointTapListener() {
                                    @Override
                                    public void onTap(Series series, DataPointInterface dataPoint) {
                                        Toast.makeText(getActivity(), "("+dataPoint.getX()+" , "+dataPoint.getY()+")", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                graph.addSeries(root);
                                root.setShape(PointsGraphSeries.Shape.POINT);
                                root.setColor(Color.GREEN);
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
                        PointsGraphSeries<DataPoint> root = new PointsGraphSeries<>(new DataPoint[] {
                                new DataPoint(xs, ys)
                        });
                        root.setOnDataPointTapListener(new OnDataPointTapListener() {
                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                Toast.makeText(getActivity(), "("+dataPoint.getX()+" , "+dataPoint.getY()+")", Toast.LENGTH_SHORT).show();
                            }
                        });
                        graph.addSeries(root);
                        root.setShape(PointsGraphSeries.Shape.POINT);
                        root.setColor(Color.GREEN);
                    }
                }else{
                    //System.out.println(xi + " is an aproximate root");
                    PointsGraphSeries<DataPoint> root = new PointsGraphSeries<>(new DataPoint[] {
                            new DataPoint(xi, yi)
                    });
                    root.setOnDataPointTapListener(new OnDataPointTapListener() {
                        @Override
                        public void onTap(Series series, DataPointInterface dataPoint) {
                            Toast.makeText(getActivity(), "("+dataPoint.getX()+" , "+dataPoint.getY()+")", Toast.LENGTH_SHORT).show();
                        }
                    });
                    graph.addSeries(root);
                    root.setShape(PointsGraphSeries.Shape.POINT);
                    root.setColor(Color.GREEN);
                }
            }else{
                iter.setError("Wrong iterates");
                //System.out.println("Wrong iterates!");
            }
        }else{
            textError.setError("Toleranc must be < 0");
            //System.out.println("Tolerance < 0");
        }
    }


    public void graphPharalelism(double start,double end){
        LineGraphSeries<DataPoint> serie = new LineGraphSeries<>();
        double x = start;
        function.setPrecision(20);
        double yi = (this.function.with("x", BigDecimal.valueOf(x)).eval()).doubleValue();
        while(x <= end){

            serie.appendData(new DataPoint(x,yi),true,(int)Math.ceil(Math.abs(end-start)/0.1));
            x = x + 0.1;
            yi = (this.function.with("x", BigDecimal.valueOf(x)).eval()).doubleValue();
        }
        graph.addSeries(serie);


    }

}


