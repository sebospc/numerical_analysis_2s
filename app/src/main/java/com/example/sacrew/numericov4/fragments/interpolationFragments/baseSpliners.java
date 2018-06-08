package com.example.sacrew.numericov4.fragments.interpolationFragments;


import android.util.Pair;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import static com.example.sacrew.numericov4.fragments.interpolation.interpolationGraph;


public class baseSpliners extends baseInterpolationMethods {
    // first pair <= x <= second pair
    // each pair have their f(x) value :)
    Pair[] inequality;
    List<Pair<String,Pair<Integer,Pair<Double,Double>>>> equations;

    public void createInequality() {
        inequality = new Pair[xn.length - 1];
        for (int i = 0; i < xn.length - 1; i++) {
            inequality[i] = new Pair<Pair<Double, Double>, Pair<Double, Double>>(new Pair<>(xn[i], fxn[i]), new Pair<>(xn[i + 1], fxn[i + 1]));
            System.out.println(inequality[i]);
        }

    }
    public void updateGraph(){
        constantSerie = utilsOfGraph.graphPharallelByFunctions(equations);
        for(LineGraphSeries<DataPoint> v : constantSerie)
            interpolationGraph.addSeries(v);
    }
}
