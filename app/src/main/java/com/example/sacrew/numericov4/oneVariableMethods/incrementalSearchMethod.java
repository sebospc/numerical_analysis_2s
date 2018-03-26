package com.example.sacrew.numericov4.oneVariableMethods;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;

/**
 * Created by stivenramireza on 24/03/18.
 */

public class incrementalSearchMethod {
    private double x0;
    private double delta;
    private int ite;
    private Expression function;
    public incrementalSearchMethod(String function, double x0, double delta, int ite){
        this.x0 = x0;
        this.delta=delta;
        this.ite=ite;
        this.function =  new Expression(function);
    }


    public void execute(GraphView graph, final Activity activity) {
        if(delta != 0){
            if(ite > 0){
                double y0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                if(y0 != 0){
                    double x1 = x0 + delta;
                    double y1 = (this.function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                    int cont = 1;
                    LineGraphSeries<DataPoint> serie = new LineGraphSeries<>();
                    serie.appendData(new DataPoint(x1,y1),false,ite);
                    graph.addSeries(serie);
                    while(((y1*y0) > 0) && (cont < ite)){
                        x0 = x1;
                        y0 = y1;
                        x1 = x0 + delta;
                        y1 = (this.function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                        serie.appendData(new DataPoint(x1,y1),false,ite);
                        cont++;
                    }
                    graph.addSeries(serie);
                    if(y1 == 0){
                        PointsGraphSeries<DataPoint> root = new PointsGraphSeries<>(new DataPoint[] {
                                new DataPoint(x1, y1)
                        });
                        root.setOnDataPointTapListener(new OnDataPointTapListener() {
                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                Toast.makeText(activity, "("+dataPoint.getX()+" , "+dataPoint.getY()+")", Toast.LENGTH_SHORT).show();
                            }
                        });
                        graph.addSeries(root);
                        root.setShape(PointsGraphSeries.Shape.POINT);
                        //System.out.println(x1 + " is a root");
                    }else if(y1*y0 < 0){
                        //System.out.println("[" + x0 + ", " + x1 + "] is an interval");
                    }else{
                       // System.out.println("Failed the interval!");
                    }
                }else{
                    PointsGraphSeries<DataPoint> root = new PointsGraphSeries<>(new DataPoint[] {
                            new DataPoint(x0, y0)
                    });
                    root.setOnDataPointTapListener(new OnDataPointTapListener() {
                        @Override
                        public void onTap(Series series, DataPointInterface dataPoint) {
                            Toast.makeText(activity, "("+dataPoint.getX()+" , "+dataPoint.getY()+")", Toast.LENGTH_SHORT).show();
                        }
                    });
                    graph.addSeries(root);
                    root.setShape(PointsGraphSeries.Shape.POINT);
                    System.out.println(x0 + " is a root");
                }
            }else{
                //System.out.println("Wrong iterates!");
            }
        }else{
            //System.out.println("Delta zero");
        }
    }
}