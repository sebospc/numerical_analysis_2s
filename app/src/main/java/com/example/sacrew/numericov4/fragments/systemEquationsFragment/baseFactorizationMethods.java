package com.example.sacrew.numericov4.fragments.systemEquationsFragment;

import android.os.Build;
import android.support.annotation.RequiresApi;

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


}
