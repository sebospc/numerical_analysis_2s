package com.example.sacrew.numericov4.fragments.systemEquationsFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;

/**
 * Created by sacrew on 25/05/18.
 */

public abstract class baseFactorizationMethods extends baseSystemEquations {
    double[][] matrixL;
    double[][] matrixU;

    @RequiresApi(api = Build.VERSION_CODES.M)
    double[] progresiveSubstitution(double[][] expandedMatrix) {
        String message;
        int n = expandedMatrix.length - 1;
        double[] x = new double[n + 1];
        if (matrixU[0][0] == 0) {
            message = "Error division 0 in progressive substitution";
            styleWrongMessage(message);
            return x;
        }
        x[0] = matrixU[0][n + 1] / matrixU[0][0];
        for (int i = 0; i < n + 1; i++) {
            double sumatoria = 0;
            for (int p = 0; p < i; p++) {
                sumatoria = sumatoria + matrixL[i][p] * x[p];
            }
            if (matrixL[i][i] == 0) {
                message = "Error division 0 in progressive substitution";
                styleWrongMessage(message);
                return x;
            }
            x[i] = (matrixL[i][n + 1] - sumatoria) / matrixL[i][i];
            matrixU[i][n + 1] = x[i];
        }

        return x;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addFactorization(double[][] matrixL, double[][] matrixU, Context context) {
        LinearLayout L = new LinearLayout(context);
        L.setOrientation(LinearLayout.VERTICAL);
        TextView tittleL = new TextView(context);
        tittleL.setText(" L ");
        L.addView(tittleL);
        LinearLayout U = new LinearLayout(context);
        U.setOrientation(LinearLayout.VERTICAL);
        TextView tittleU = new TextView(context);
        tittleU.setText(" U ");
        U.addView(tittleU);

        TableLayout matrixResultL = new TableLayout(context);
        TableLayout matrixResultU = new TableLayout(context);
        for(int i = 0; i < matrixL.length; i++){
            TableRow auxL = new TableRow(context);
            TableRow auxU = new TableRow(context);
            for(int j = 0; j <= matrixL.length; j++){
                if( j != matrixL.length) {
                    auxL.addView(defaultTextView((String.valueOf(matrixL[i][j]) + "       ").substring(0, 6)));
                    auxU.addView(defaultTextView((String.valueOf(matrixU[i][j]) + "       ").substring(0, 6)));
                }else{
                    auxL.addView(defaultTextView((String.valueOf(matrixL[i][j]) + "       ").substring(0, 6), getResources().getColor(R.color.prettyRed),100,10));
                    auxU.addView(defaultTextView((String.valueOf(matrixU[i][j]) + "       ").substring(0, 6),getResources().getColor(R.color.header_line_color),100,10));
                }
            }
            matrixResultL.addView(auxL);
            matrixResultU.addView(auxU);
        }

        L.addView(matrixResultL);
        U.addView(matrixResultU);
        contentStages.addView(L);
        TextView space = new TextView(context);
        space.setText("    ");
        contentStages.addView(space);
        contentStages.addView(U);
    }
}
