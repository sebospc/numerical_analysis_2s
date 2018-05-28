package com.example.sacrew.numericov4.fragments.systemEquationsFragment;

import java.util.Arrays;

/**
 * Created by sacrew on 27/05/18.
 */

public class baseIterativeMethods extends baseSystemEquations {
    public double[] minus(double[] x, double[] y){
        double[] aux = new double[x.length];
        for(int i = 0; i < x.length; i++){
            aux[i] = x[i]-y[i];
        }
        return aux;
    }
    public double norma(double[] values){
        double [] aux = new double[values.length];
        for(int i = 0; i < values.length; i++)
            aux[i]= Math.abs(values[i]);
        Arrays.sort(aux);
        System.out.println(aux[values.length-1]);
        return aux[values.length-1];
    }
}
