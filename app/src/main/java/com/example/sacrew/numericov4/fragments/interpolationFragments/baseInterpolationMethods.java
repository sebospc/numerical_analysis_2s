package com.example.sacrew.numericov4.fragments.interpolationFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.widget.EditText;
import android.widget.TableRow;

import com.example.sacrew.numericov4.graphUtils;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.fragments.interpolation.interpolationGraph;
import static com.example.sacrew.numericov4.fragments.interpolation.poolColors;
import static com.example.sacrew.numericov4.fragments.interpolation.vectors;

/**
 * Created by sacrew on 28/05/18.
 */

public abstract class baseInterpolationMethods extends Fragment {
    public LinkedList<Double> xn;
    public LinkedList<Double> fxn;
    static List<LineGraphSeries<DataPoint>> constantSerie;
    private graphUtils utils = new graphUtils();
    public boolean calc = false;
    public String function = "";
    public void bootStrap(){
        xn = new LinkedList<>();
        fxn = new LinkedList<>();

        for(int i = 0; i < ((TableRow)vectors.getChildAt(0)).getChildCount(); i++){
            double x = Double.parseDouble(((EditText)((TableRow)vectors.getChildAt(0)).getChildAt(i)).getText().toString());
            double y = Double.parseDouble(((EditText)((TableRow)vectors.getChildAt(1)).getChildAt(i)).getText().toString());
            xn.add(x);
            System.out.println("añadiendo xn "+x);
            fxn.add(y);
        }
    }

    public void updateGraph( String function, Context context){

        int color = poolColors.remove(0);
        poolColors.add(color);
        System.out.println("color "+color);
        constantSerie = utils.graphPharallel(400,function,color,context);
        for(LineGraphSeries<DataPoint> v : constantSerie)
            interpolationGraph.addSeries(v);

    }
    public void cleanGraph(){
        if(constantSerie != null){
            for(LineGraphSeries<DataPoint> v : constantSerie)
                interpolationGraph.removeSeries(v);

        }
    }

}
