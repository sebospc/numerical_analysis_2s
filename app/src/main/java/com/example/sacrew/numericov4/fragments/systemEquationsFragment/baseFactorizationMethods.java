package com.example.sacrew.numericov4.fragments.systemEquationsFragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;

/**
 * Created by sacrew on 25/05/18.
 */

public abstract class baseFactorizationMethods extends baseSystemEquations {
    double[][] matrixL;
    double[][] matrixU;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public double[] progresiveSubstitution(double[][] expandedMatrix){
        String message = "";
        int n = expandedMatrix.length-1;
        double [] x = new double[n+1];
        if(matrixU[0][0] == 0) {
            message = "Error division 0 in progressive substitution";
            styleWrongMessage(message);
            return x;
        }
        x[0] = matrixU[0][n+1]/matrixU[0][0];
        for(int i = 0; i< n+1;i++) {
            double sumatoria = 0;
            for(int p = 0; p < i ; p++){
                sumatoria = sumatoria + matrixL[i][p]*x[p];
            }
            if(matrixL[i][i] == 0) {
                message = "Error division 0 in progressive substitution";
                styleWrongMessage(message);
                return x;
            }
            x[i] = (matrixL[i][n+1]-sumatoria)/matrixL[i][i];
            matrixU[i][n+1] = x[i];
        }

        return x;
    }

    private final SuperActivityToast.OnButtonClickListener onButtonClickListener =
            new SuperActivityToast.OnButtonClickListener() {

                @Override
                public void onClick(View view, Parcelable token) {
                    SuperToast.create(view.getContext(), null, Style.DURATION_VERY_SHORT)
                            .setColor(Color.TRANSPARENT).show();
                }
            };

    private void styleWrongMessage(String message){
        SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_BUTTON)
                .setButtonText("UNDO")
                .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                .setProgressBarColor(Color.WHITE)
                .setText(message)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(Color.rgb(244,67,54))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }
}
