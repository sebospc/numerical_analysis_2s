package com.example.sacrew.numericov4;

import android.app.Activity;
import android.graphics.Color;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sacrew on 27/03/18.
 */

public class graphMethods {

    public static void graphSerie(double start, double end, String funcitonExpr, GraphView graph, int color){
        Expression function = new Expression(funcitonExpr);
        LineGraphSeries<DataPoint> serie = new LineGraphSeries<>();
        double x = start;
        function.setPrecision(20);
        double yi = (function.with("x", BigDecimal.valueOf(x)).eval()).doubleValue();
        try{
            (function.with("x", BigDecimal.valueOf(end+0.5)).eval()).doubleValue();
            end = end +0.5;
        }catch (Exception ignored){

        }

        while(x <= end){
            serie.appendData(new DataPoint(x,yi),true,(int)Math.ceil(Math.abs(end-start)/0.1));
            x = x + 0.1;
            yi = (function.with("x", BigDecimal.valueOf(x)).eval()).doubleValue();
        }
        serie.setColor(color);
        graph.addSeries(serie);
    }

    public static void graphPoint(double x, double y, PointsGraphSeries.Shape figure, GraphView graph, final Activity activity,
                                  String color,boolean listener){
        PointsGraphSeries<DataPoint> root = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(x, y)
        });
        if(listener)
        root.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(activity, "("+dataPoint.getX()+" , "+dataPoint.getY()+")", Toast.LENGTH_SHORT).show();
            }
        });
        graph.addSeries(root);
        root.setShape(figure);
        root.setColor(Color.parseColor(color));
    }

    /*public static void graphStraight(double x, double y, double xi, double yi, GraphView graph){
        LineGraphSeries<DataPoint> serie = new LineGraphSeries<>();
        if(x > xi){
            serie.appendData(new DataPoint(xi,yi),true,2);
            serie.appendData(new DataPoint(x,y),true,2);
        }else{
            serie.appendData(new DataPoint(x,y),true,2);
            serie.appendData(new DataPoint(xi,yi),true,2);
        }
        serie.setColor(Color.BLACK);
        graph.addSeries(serie);
    }*/
}
