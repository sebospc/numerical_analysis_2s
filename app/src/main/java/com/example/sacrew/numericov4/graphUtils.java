package com.example.sacrew.numericov4;

import android.app.Activity;
import android.content.Context;
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

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.eval.DoubleEvaluator;
import org.matheclipse.parser.client.eval.DoubleVariable;
import org.matheclipse.parser.client.eval.IDouble0Function;
import org.matheclipse.parser.client.eval.IDoubleValue;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class graphUtils {
    private int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public void graphSerie(double start, double end, String funcitonExpr, GraphView graph, int color) {
        Expression function = new Expression(funcitonExpr);
        LineGraphSeries<DataPoint> serie = new LineGraphSeries<>();
        double x = start;
        function.setPrecision(20);
        double yi = (function.with("x", BigDecimal.valueOf(x)).eval()).doubleValue();
        try {
            (function.with("x", BigDecimal.valueOf(end + 0.5)).eval()).doubleValue();
            end = end + 0.5;
        } catch (Exception ignored) {

        }
        if (x > end) {
            Double aux = x;
            x = end;
            end = aux;
        }
        while (x <= end) {
            serie.appendData(new DataPoint(x, yi), true, (int) Math.ceil(Math.abs(end - start) / 0.1));
            x = x + 0.1;
            try {
                yi = (function.with("x", BigDecimal.valueOf(x)).eval()).doubleValue();
            } catch (Exception ignored) {

            }
        }
        serie.setColor(color);
        graph.addSeries(serie);
    }

    public PointsGraphSeries<DataPoint> graphPoint(double x, double y, PointsGraphSeries.Shape figure, GraphView graph, final Activity activity,
                                                   int color, boolean listener) {
        PointsGraphSeries<DataPoint> root = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(x, y)
        });
        if (listener)
            root.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(activity, "(" + dataPoint.getX() + " , " + dataPoint.getY() + ")", Toast.LENGTH_SHORT).show();
                }
            });
        graph.addSeries(root);
        root.setShape(figure);
        root.setColor(color);
        return root;
    }

    public String functionRevision(String function) {
        if (function.toLowerCase().contains("ln"))
            return function.toLowerCase().replace("ln", "log");
        else
            return function;
    }

    public List<LineGraphSeries<DataPoint>> bestGraphPharallel(int iters, String funcitonExpr, int color, Context context) {
        int realIters = iters * 2;
        int perCore = (int) Math.ceil(realIters / NUMBER_OF_CORES) * 2;
        double each = (realIters * 0.1 * -1);
        double end = (each + (perCore * 0.1) - 0.1);
        Thread[] cores = new Thread[NUMBER_OF_CORES];

        bestThreadGraph[] values = new bestThreadGraph[NUMBER_OF_CORES];
        for (int i = 0; i < cores.length; i++) {
            values[i] = new bestThreadGraph(each, end, funcitonExpr, color, perCore);
            cores[i] = new Thread(values[i]);
            cores[i].start();
            each = (each + (perCore * 0.1) - 0.1);
            end = (each + (perCore * 0.1) + 0.1);
        }
        List<LineGraphSeries<DataPoint>> listSeries = new LinkedList<>();
        for (int i = 0; i < cores.length; i++) {
            try {
                cores[i].join();
                listSeries.add(values[i].getSeries());
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        return listSeries;
    }

    public List<LineGraphSeries<DataPoint>> graphPharallel(int iters, String funcitonExpr, int color, Context context) {
        int realIters = iters * 2;
        int perCore = (int) Math.ceil(realIters / NUMBER_OF_CORES) * 2;
        double each = (realIters * 0.1 * -1);
        double end = (each + (perCore * 0.1) - 0.1);
        Thread[] cores = new Thread[NUMBER_OF_CORES];

        threadGraph[] values = new threadGraph[NUMBER_OF_CORES];
        for (int i = 0; i < cores.length; i++) {
            values[i] = new threadGraph(each, end, funcitonExpr, color, perCore);
            cores[i] = new Thread(values[i]);
            cores[i].start();
            each = (each + (perCore * 0.1) - 0.1);
            end = (each + (perCore * 0.1) + 0.1);
        }
        List<LineGraphSeries<DataPoint>> listSeries = new LinkedList<>();
        for (int i = 0; i < cores.length; i++) {
            try {
                cores[i].join();
                listSeries.add(values[i].getSeries());
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        return listSeries;

    }

    private class bestThreadGraph implements Runnable {
        private double x;
        private double end;
        private String function;
        private int color;
        private int perCore;
        private DoubleEvaluator engine = new DoubleEvaluator();
        private LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        bestThreadGraph(double x, double end, String function, int color, int perCore) {
            this.x = x;
            this.end = end;
            this.function = function;
            this.color = color;
            this.perCore = perCore;

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            double y;
            double x = this.x;
            EvalEngine aux = new EvalEngine();
            IDoubleValue vd = new DoubleVariable(3.0);

            try {
                engine.defineVariable("x", vd);
                engine.evaluate(function);

                while (x <= this.end) {

                    vd.setValue(x);
                    y = engine.evaluate();

                    this.series.appendData(new DataPoint(x, y), true, perCore * 2);


                    x = x + 0.1;
                }
            } catch (Exception ignored) {

            }
            series.setColor(color);
            //graphFragment.listSeries.add(series);
        }

        public LineGraphSeries<DataPoint> getSeries() {
            return series;
        }
    }

    private class threadGraph implements Runnable {
        private double x;
        private double end;
        private Expression function;
        private int color;
        private int perCore;
        private LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        threadGraph(double x, double end, String function, int color, int perCore) {
            this.x = x;
            this.end = end;
            this.function = new Expression(function);
            this.color = color;
            this.perCore = perCore;

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {

            this.function.setPrecision(5);
            double y;
            double x = this.x;

            while (x <= this.end) {
                try {
                    y = (this.function.with("x", BigDecimal.valueOf(x)).eval()).doubleValue();
                    this.series.appendData(new DataPoint(x, y), true, perCore * 2);

                } catch (Exception ignored) {

                }
                x = x + 0.1;
            }
            series.setColor(color);
            //graphFragment.listSeries.add(series);
        }

        public LineGraphSeries<DataPoint> getSeries() {
            return series;
        }
    }

}
