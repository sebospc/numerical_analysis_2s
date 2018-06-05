package com.example.sacrew.numericov4.fragments.interpolationFragments;

import android.app.Fragment;
import android.util.Pair;
import android.widget.EditText;
import android.widget.TableRow;

import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.fragments.interpolation.vectors;

/**
 * Created by sacrew on 28/05/18.
 */

public abstract class baseInterpolationMethods extends Fragment {
    public LinkedList<Double> xn;
    public LinkedList<Double> fxn;
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

}
