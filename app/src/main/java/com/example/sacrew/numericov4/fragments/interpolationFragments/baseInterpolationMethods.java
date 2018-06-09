package com.example.sacrew.numericov4.fragments.interpolationFragments;

import android.app.Fragment;
import android.content.Context;
import android.widget.EditText;
import android.widget.TableRow;

import com.example.sacrew.numericov4.utils.graphUtils;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import static com.example.sacrew.numericov4.fragments.interpolation.interpolationGraph;
import static com.example.sacrew.numericov4.fragments.interpolation.poolColors;
import static com.example.sacrew.numericov4.fragments.interpolation.vectors;

/**
 * Created by sacrew on 28/05/18.
 */

public abstract class baseInterpolationMethods extends Fragment {
    protected double[] xn;
    protected double[] fxn;
    static List<LineGraphSeries<DataPoint>> constantSerie;
    protected graphUtils utilsOfGraph = new graphUtils();
    protected boolean calc = false;
    protected String function = "";
    protected Boolean bootStrap(){
        int length = ((TableRow)vectors.getChildAt(0)).getChildCount();
        xn = new double[length];
        fxn = new double[length];

        for(int i = 0; i < ((TableRow)vectors.getChildAt(0)).getChildCount(); i++){
            double x;
            double y;
            try {
                x = Double.parseDouble(((EditText) ((TableRow) vectors.getChildAt(0)).getChildAt(i)).getText().toString());
            }catch (Exception e){
                ((EditText) ((TableRow) vectors.getChildAt(0)).getChildAt(i)).setError("Invalid value");
                return false;
            }
            try{
                y = Double.parseDouble(((EditText)((TableRow)vectors.getChildAt(1)).getChildAt(i)).getText().toString());

            }catch(Exception e){
                ((EditText)((TableRow)vectors.getChildAt(1)).getChildAt(i)).setError("Invalid value");
                return false;
            }

            xn[i] = x;
            fxn[i] = y;
        }
        return true;
    }

    protected void updateGraph( String function, Context context,int iters){

        int color = poolColors.remove(0);
        poolColors.add(color);
        constantSerie = utilsOfGraph.bestGraphPharallel(iters,function,color,context);
        for(LineGraphSeries<DataPoint> v : constantSerie)
            interpolationGraph.addSeries(v);

    }
    protected void cleanGraph(){
        if(constantSerie != null){
            for(LineGraphSeries<DataPoint> v : constantSerie)
                interpolationGraph.removeSeries(v);
        }
    }

}
