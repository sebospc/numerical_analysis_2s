package com.example.sacrew.numericov4.fragments.systemEquationsFragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpBisection;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpInverseMatrix;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;

import static com.example.sacrew.numericov4.fragments.systemEquations.xValuesText;

/**
 * A simple {@link Fragment} subclass.
 */
public class inverseDeterminant extends baseFactorizationMethods {

    private Complex[][] matrixLComplex;
    private Complex[][] matrixUComplex;
    private TableLayout matrixResult;
    private TextView determinant;
    private int actual = -1;
    public inverseDeterminant() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inverse_determinant, container, false);
        matrixResult = view.findViewById(R.id.matrixResult);
        determinant = view.findViewById(R.id.determinant);
        Button runCroult = view.findViewById(R.id.croultButton);

        runCroult.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                actual = 0;
                begin();
            }
        });
        Button runDoolittle = view.findViewById(R.id.doolittleButton);
        runDoolittle.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                actual = 1;
                begin();
            }
        });
        Button runCholesky = view.findViewById(R.id.choleskyButton);
        runCholesky.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                actual = 2;
                begin();
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void bootStrap(double[][] expandedMatrix){
        matrixResult.removeAllViews();
        matrixLComplex = new Complex[expandedMatrix.length][expandedMatrix.length+1];
        matrixUComplex = new Complex[expandedMatrix.length][expandedMatrix.length+1];
        for(int i = 0; i< matrixUComplex.length;i++) {
            for (int j = 0; j <= matrixUComplex.length;j++) {
                matrixUComplex[i][j] = new Complex(0, 0);
                matrixLComplex[i][j] = new Complex(0, 0);
                if(i == j) {
                    if (actual == 0)
                        matrixUComplex[i][i] = new Complex(1, 0);
                    else if(actual == 1)
                        matrixLComplex[i][i] = new Complex(1, 0);
                }
            }
        }
        generalFactorization(expandedMatrix);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void generalFactorization(double[][] expandedMatrix){
        for(int k = 0; k < expandedMatrix.length; k++) {
            Complex suma1 = new Complex(0, 0);
            for (int p = 0; p < k; p++) {
                suma1 = suma1.add(matrixLComplex[k][p].multiply(matrixUComplex[p][k]));
            }
            Complex aux = new Complex(expandedMatrix[k][k], 0);
            if (actual == 0) {
                matrixLComplex[k][k] = aux.subtract(suma1);
            } else if (actual == 1){
                matrixUComplex[k][k] = aux.subtract(suma1);
            } else if( actual == 2){
                matrixLComplex[k][k] = (aux.subtract(suma1)).sqrt();
                matrixUComplex[k][k] = (aux.subtract(suma1)).sqrt();
            }
            for(int i = k+1; i <expandedMatrix.length; i++){
                Complex suma2 = new Complex(0,0);
                for(int p = 0; p < k; p++){
                    suma2 = suma2.add(matrixLComplex[i][p].multiply(matrixUComplex[p][k]));
                }
                matrixLComplex[i][k] = (new Complex(expandedMatrix[i][k],0).subtract(suma2))
                        .divide(matrixUComplex[k][k]);
            }
            for(int j = k+1; j < expandedMatrix.length;j++){
                Complex suma3 = new Complex(0,0);
                for(int p = 0; p < k;p++){
                    suma3 = suma3.add(matrixLComplex[k][p].multiply(matrixUComplex[p][j]));
                }
                matrixUComplex[k][j] = (new Complex(expandedMatrix[k][j]).subtract(suma3))
                        .divide(matrixLComplex[k][k]);
            }

        }
        Complex[][] inverse = new Complex[expandedMatrix.length][expandedMatrix.length];
        Complex determinantL = new Complex(1,0);
        Complex determinantU = new Complex(1,0);
        for(int i = 0; i< expandedMatrix.length ; i++){
            determinantL = determinantL.multiply(matrixLComplex[i][i]);
            determinantU = determinantU.multiply(matrixUComplex[i][i]);

            for(int j = 0; j< expandedMatrix.length;j++){
                if(i == j)
                    matrixLComplex[j][expandedMatrix.length] = new Complex(1,0);
                else
                    matrixLComplex[j][expandedMatrix.length] = new Complex(0,0);
            }
            progresiveSubstitution(matrixLComplex);
            Complex[] val = substitution(matrixUComplex);
            for(int p = 0; p < val.length ;p++){
                inverse[p][i] = val[p];
            }
        }
        determinant.setText("Det(A) = "+formating(determinantL.multiply(determinantU)));
        for(Complex[] aux: inverse){
            TableRow row = new TableRow(getContext());
            for(Complex val: aux){
                row.addView(defaultEditText((formating(val)+"      ").substring(0,6)));
            }
            matrixResult.addView(row);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public Complex[] progresiveSubstitution(Complex[][] matrixLComplex){
        int n = matrixLComplex.length-1;
        Complex [] x = new Complex[n+1];
        if(matrixUComplex[0][0].getReal() == 0 && matrixUComplex[0][0].getImaginary() == 0) {
            Toast.makeText(getContext(), "Error division 0 in progressive substitution 1", Toast.LENGTH_SHORT).show();
            return x;
        }
        x[0] = matrixUComplex[0][n+1].divide(matrixUComplex[0][0]);
        for(int i = 0; i< n+1;i++) {
            Complex sumatoria = new Complex(0,0);
            for(int p = 0; p < i ; p++){
                sumatoria = sumatoria.add(matrixLComplex[i][p].multiply(x[p]));
            }
            if(matrixLComplex[i][i].getReal() == 0 && matrixLComplex[i][i].getImaginary() == 0) {
                Toast.makeText(getContext(), "Error division 0 in progressive substitution 2", Toast.LENGTH_SHORT).show();
                return x;
            }
            x[i] = (matrixLComplex[i][n+1].subtract(sumatoria)).divide(matrixLComplex[i][i]);
            //x[i] = (matrixL[i][n+1]-sumatoria)/matrixL[i][i];
            matrixUComplex[i][n+1] = x[i];
        }

        return x;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Complex[] substitution(Complex[][] expandedMatrix){
        xValuesText.removeAllViews();
        int n = expandedMatrix.length-1;
        Complex[] values = new Complex[n+1];
        if(expandedMatrix[n][n].getReal() == 0 && expandedMatrix[n][n].getImaginary() == 0) {
            Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
            return values;
        }
        Complex x = expandedMatrix[n][n+1].divide(expandedMatrix[n][n]);

        values[values.length-1] = x;
        for(int i = 0 ; i<n+1 ; i++){
            Complex sumatoria = new Complex(0,0);
            int auxi = n-i;
            for(int p = auxi + 1; p < n+1; p++ ){
                sumatoria = sumatoria.add(expandedMatrix[auxi][p].multiply(values[p]))  ;
            }
            if(expandedMatrix[auxi][auxi].getReal() == 0 && expandedMatrix[auxi][auxi].getImaginary() == 0) {
                Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
               return values;
            }
            values[auxi] = (expandedMatrix[auxi][n+1].subtract(sumatoria)).divide(expandedMatrix[auxi][auxi]);

        }
        return  values;
    }

    public String formating(Complex c){


        if(c.getReal() == -0.0){
            c = new Complex(0.0,c.getImaginary());
        }
        if(c.getImaginary() == -0.0){
            c = new Complex(c.getReal(),0.0);
        }
        if(c.getImaginary() == 0)
            return c.getReal()+"";
        else if(c.getReal() == 0)
            return ((c.getImaginary()+"").length() <= 5 )?(c.getImaginary()+"i")
                    : (c.getImaginary()+"").substring(0,5)+"i";
        else return new ComplexFormat().format(c).replaceAll("\\s+","");
    }

}
