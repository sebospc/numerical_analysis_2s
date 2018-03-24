package com.example.sacrew.numericov4;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.sun.org.apache.xpath.internal.axes.IteratorPool;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.function.ToLongBiFunction;
import java.math.*;

/**
 * Created by stivenramireza on 24/03/18.
 */

public class fixedPointMethod implements Runnable {
    private double x0;
    private double tol;
    private int ite;
    private Expression f;
    private Expression g;
    fixedPointMethod(double x0, double tol, int ite, String f, String g){
        this.xi = xi;
        this.xs = xs;
        this.tol=tol;
        this.ite=ite;
        this.f =  new Expression(f);
        this.g = new Expression(g);
    }
    
    @Override
    public void run() {
        if(tol >= 0){
            if(ite > 0){
                double y0 = (this.f.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                if(y0 != 0){
                    int cont = 0;
                    double error = tol + 1;
                    double xa = x0;
                    while((y0 != 0) && (error > tol) && (cont < ite)){
                        double xn = (this.g.with("x", BigDecimal.valueOf(xa)).eval()).doubleValue();
                        y0 = (this.f.with("x", BigDecimal.valueOf(x0)).eval()).doubleValue();
                        error = Math.abs(xn - xa);
                        xa = xn;
                        cont++;
                    }
                    if(y == 0){
                        System.out.println(xa + " is a root");
                    }else if(error <= tol){
                        System.out.println(xa + " is an aproximate root");
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
            System.out.println("Tolerance < 0");
        }
    }
}