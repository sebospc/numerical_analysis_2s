package com.example.sacrew.numericov4;


import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.sacrew.numericov4.fragments.home;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;

/**
 * Created by sacrew on 23/03/18.
 */

public class graphParallel implements Runnable {
    private double x;
    private int end;
    private Expression function;
    private int color;
    public graphParallel(double x, int end, String function,int color){
        this.x = x;
        this.end=end;
        this.function =  new Expression(function);
        this.color=color;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {

        this.function.setPrecision(20);
        double y;
        double x = this.x;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for(int i = 0;i<=end;i++) {

            try {
                y = (this.function.with("x", BigDecimal.valueOf(x)).eval()).doubleValue();
                series.appendData(new DataPoint(x, y), true, end);
                x = x + 0.1;
            }catch(Exception e){
                System.out.println("ojo negativo "+x);
                if(x < 0)
                    x = x *-1;
            }
        }
        series.setColor(color);
        home.listSeries.add(series);


    }
}

