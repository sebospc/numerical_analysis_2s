package com.example.sacrew.numericov4.oneVariableMethods;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;


import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.math.*;

/**
 * Created by stivenramireza on 24/03/18.
 */

public class bisectionMethod  {
    private double xi;
    private double xs;
    private double tol;
    private int ite;
    private Expression function;
    bisectionMethod(double xi, double xs, double tol, int ite, String function){
        this.xi = xi;
        this.xs = xs;
        this.tol=tol;
        this.ite=ite;
        this.function =  new Expression(function);
    }

    public void run() {
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
                                error = Math.abs(xm - xaux);
                                cont++;
                            }
                            if(ym == 0){
                                System.out.println(xm + " is an aproximate root");
                            }else if(error < tol){
                                System.out.println(xaux + " is an aproximate root");
                            }else{
                                System.out.println("Failed!");
                            }
                        }else{
                            System.out.println("Failed the interval!");
                        }
                    }else{
                        System.out.println(xs + " is an aproximate root");
                    }
                }else{
                    System.out.println(xi + " is an aproximate root");
                }
            }else{
                System.out.println("Wrong iterates!");
            }
        }else{
            System.out.println("Tolerance < 0");
        }
    }
}