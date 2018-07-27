package com.sands.aplication.numeric.fragments.interpolationFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;

import com.sands.aplication.numeric.utils.graphUtils;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.util.Objects;

import static com.sands.aplication.numeric.fragments.homeFragment.poolColors;
import static com.sands.aplication.numeric.fragments.interpolation.interpolationGraph;
import static com.sands.aplication.numeric.fragments.interpolation.vectors;

/**
 * Created by sacrew on 28/05/18.
 */

public abstract class baseInterpolationMethods extends Fragment {
    static List<LineGraphSeries<DataPoint>> constantSerie;
    final graphUtils utilsOfGraph = new graphUtils();
    private final SuperActivityToast.OnButtonClickListener onButtonClickListener =
            new SuperActivityToast.OnButtonClickListener() {

                @Override
                public void onClick(View view, Parcelable token) {
                    SuperActivityToast.cancelAllSuperToasts();
                }
            };
    double[] xn;
    double[] fxn;
    boolean calc = false;
    String latexText = "";
    String function;

    boolean bootStrap() {
        mathExpressions.equations = null;
        int length = ((TableRow) vectors.getChildAt(0)).getChildCount();
        xn = new double[length];
        fxn = new double[length];

        for (int i = 0; i < ((TableRow) vectors.getChildAt(0)).getChildCount(); i++) {
            double x;
            double y;
            try {
                x = Double.parseDouble(((EditText) ((TableRow) vectors.getChildAt(0)).getChildAt(i)).getText().toString());
            } catch (Exception e) {
                ((EditText) ((TableRow) vectors.getChildAt(0)).getChildAt(i)).setError("Invalid value");
                return false;
            }
            try {
                y = Double.parseDouble(((EditText) ((TableRow) vectors.getChildAt(1)).getChildAt(i)).getText().toString());

            } catch (Exception e) {
                ((EditText) ((TableRow) vectors.getChildAt(1)).getChildAt(i)).setError("Invalid value");
                return false;
            }

            xn[i] = x;
            fxn[i] = y;
        }
        return true;
    }

    void updateGraph(String function, Context context, int iters) {

        int color = poolColors.remove(0);
        poolColors.add(color);
        constantSerie = utilsOfGraph.bestGraphPharallel(iters, function, color, context);
        for (LineGraphSeries<DataPoint> v : constantSerie)
            interpolationGraph.addSeries(v);

    }

    void cleanGraph() {
        if (constantSerie != null) {
            for (LineGraphSeries<DataPoint> v : constantSerie)
                interpolationGraph.removeSeries(v);
        }
    }

    double roundOff(double number) {

        long factor = (long) Math.pow(10, 2);
        long tmp = Math.round(number * factor);
        return (double) tmp / factor;
    }

    void styleWrongMessage() {
        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(Objects.requireNonNull(getActivity()), new Style(), Style.TYPE_BUTTON)
                .setIndeterminate(true)
                .setButtonText("x")
                .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                .setProgressBarColor(Color.WHITE)
                .setText("Error division by 0")
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(Color.rgb(244, 67, 54))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }


}
