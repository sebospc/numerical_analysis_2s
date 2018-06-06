package com.example.sacrew.numericov4.fragments.systemEquationsFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.sacrew.numericov4.fragments.MainActivityTable;
import com.example.sacrew.numericov4.fragments.tableview.TableViewModel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sacrew on 27/05/18.
 */

public abstract class baseIterativeMethods extends baseSystemEquations {
    List<List<String>> totalInformation = new LinkedList<>();
    List<String> lisTitles = new LinkedList<>();
    boolean calc = false;
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeChart(Context context) {
        if(calc) {
            Intent i = new Intent(context, MainActivityTable.class);
            startActivity(i);
            TableViewModel.getTitles(lisTitles);
            TableViewModel.getCeldas(totalInformation);
        }
    }
}
