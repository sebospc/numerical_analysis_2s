package com.example.sacrew.numericov4;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;
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
}