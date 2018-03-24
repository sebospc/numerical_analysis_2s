package com.example.sacrew.numericov4;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.sun.org.apache.xpath.internal.axes.IteratorPool;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;

/**
 * Created by stivenramireza on 24/03/18.
 */

public class incrementalSearchMethod implements Runnable {
    private double x0;
    private double delta;
    private int ite;
    private Expression function;
    incrementalSearchMethod(double x0, double delta, int ite, String function){
        this.x0 = x0;
        this.delta=delta;
        this.ite=ite;
        this.function =  new Expression(function);
    }
    
    @Override
    public void run() {
        if(delta != 0){
            if(ite > 0){
                double y0 = (this.function.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                if(y0 != 0){
                    double x1 = x0 + delta;
                    double y1 = (this.function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                    int cont = 1;
                    while(((y1*y0) > 0) && (cont < ite)){
                        x0 = x1;
                        y0 = y1;
                        x1 = x0 + delta;
                        y1 = (this.function.with("x", BigDecimal.valueOf(x1)).eval()).doubleValue();
                        cont++;
                    }
                    if(y1 == 0){
                        System.out.println(x1 + " is a root");
                    }else if(y1*y0 < 0){
                        System.out.println("[" + x0 + ", " + x1 + "] is an interval");
                    }else{
                        System.out.println("Failed the interval!");
                    }
                }else{
                    System.out.println(x0 + " is a root");
                }
            }else{
                System.out.println("Wrong iterates!");
            }
        }else{
            System.out.println("Delta zero");
        }
    }
}