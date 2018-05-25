package com.example.sacrew.numericov4.fragments.systemEquations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.bValuesText;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.matrixAText;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.times;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.xValuesText;

/**
 * Created by sacrew on 23/05/18.
 */

public class baseSystemEquations extends Fragment {
    protected TableLayout matrixResult;
    protected AnimatorSet animatorSet = new AnimatorSet();
    protected List<Animator> animations;
    public baseSystemEquations(){

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void begin(){
        int n = matrixAText.getChildCount();
        double [][] expandedMatrix = new double[n][n+1];
        for(int i=0; i<n; i++){
            for (int j=0; j< n;j++){
                EditText aux = ((EditText) ((TableRow) matrixAText.getChildAt(i)).getChildAt(j));
                try {
                    double x = 0;

                    x = Double.parseDouble((aux.getText())
                            .toString());

                    expandedMatrix[i][j] = x;
                }catch (Exception e){
                    aux.setError("invalid value");
                    return;
                }
            }
            EditText aux = ((EditText)bValuesText.getChildAt(i));
            try{
                double x = Double.parseDouble(aux.getText().toString());
                expandedMatrix[i][n] = x;
            }catch (Exception e){
                aux.setError("invalid value");
                return;
            }
        }
        bootStrap(expandedMatrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bootStrap(double[][] expandedMatrix){
        matrixResult.removeAllViews();
        for (double[] v : expandedMatrix) {
            TableRow aux = new TableRow(getContext());
            for (double val : v) {
                aux.addView(defaultEditText((val + "      ").substring(0, 5)));
            }
            matrixResult.addView(aux);
        }

        elimination(expandedMatrix);


    }



    public static String convertirTexto(String val){
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("#.##");
        return num.format(val);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public TextView defaultEditText(String value) {
        return defaultEditText(value, getResources().getColor(R.color.colorPrimary),100,10);
    }

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public TextView defaultEditText(String value, int color,int weight,int size ) {
        TextView text = new EditText(getContext());
        text.setLayoutParams(new TableRow.LayoutParams(weight, 110));
        text.setEms(2);
        text.setMaxLines(1);
        text.setBackgroundColor(color);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setKeyListener(null);
        text.setText(value);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
                text,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        );
        return text;
    }

    public double[][] swapRows(int k, int higherRow, double[][] expandedMatrix){
        final int length = expandedMatrix.length;
        final int auxK = k;
        final int auxHigherRow = higherRow;
        for(int i = 0; i<= length; i++){
            final int auxi = i;
            double aux = expandedMatrix[k][i];
            expandedMatrix[k][i] = expandedMatrix[higherRow][i];
            expandedMatrix[higherRow][i] = aux;
            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.MAGENTA,
                    getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                        ((TableRow) matrixResult.getChildAt(auxHigherRow)).getChildAt(auxi)
                                .setBackgroundColor((Integer) animator.getAnimatedValue());

                        ((TableRow) matrixResult.getChildAt(auxK)).getChildAt(auxi)
                                 .setBackgroundColor((Integer) animator.getAnimatedValue());
                }
            });
            colorAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    EditText textAux = (EditText) ((TableRow) matrixResult.getChildAt(auxHigherRow)).getChildAt(auxi);
                    String aux2 = textAux.getText().toString();
                    EditText textAux2 = (EditText) ((TableRow) matrixResult.getChildAt(auxK)).getChildAt(auxi);
                    textAux.setText(textAux2.getText().toString());
                    textAux2.setText(aux2);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(colorAnimator);
        }
        return expandedMatrix;
    }

    public void swapColumn(int k, int higherColumn, double[][] expandedMatrix, int [] marks){
        int aux = marks[k];
        marks[k] = marks[higherColumn];
        marks[higherColumn] = aux;
        final int auxHigherColumn = higherColumn;
        final int auxk = k;
        for(int i =0; i < expandedMatrix.length ; i++){
            final int auxi = i;
            double temp = expandedMatrix[i][k];
            expandedMatrix[i][k] = expandedMatrix[i][higherColumn];
            expandedMatrix[i][higherColumn] = temp;
            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.MAGENTA,
                    getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxHigherColumn)
                            .setBackgroundColor((Integer) animator.getAnimatedValue());

                    ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxk)
                            .setBackgroundColor((Integer) animator.getAnimatedValue());
                }
            });
            colorAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    EditText textAux = (EditText) ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxHigherColumn);
                    String aux2 = textAux.getText().toString();
                    EditText textAux2 = (EditText) ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxk);
                    textAux.setText(textAux2.getText().toString());
                    textAux2.setText(aux2);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(colorAnimator);
        }
    }
    public void elimination(double [][] expandedMatrix){

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void substitution(double[][] expandedMatrix){
        for(double val: substitution(expandedMatrix,-1)){
            xValuesText.addView(defaultEditText((val+"            ").substring(0,5)));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void substitution(double[][] expandedMatrix, int [] marks){
        double [] result = substitution(expandedMatrix,-1);
        for(int i = 0; i< result.length; i++){
            double val = result[marks[i]];
            xValuesText.addView(defaultEditText((val+"            ").substring(0,5)));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private double[] substitution(double [][] expandedMatrix,int basura){//regression
        xValuesText.removeAllViews();
        int n = expandedMatrix.length-1;
        double[] values = new double[n+1];
        if(expandedMatrix[n][n] == 0) {
            Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
            return values;
        }
        double x = expandedMatrix[n][n+1]/expandedMatrix[n][n];

        values[values.length-1] = x;
        for(int i = 0 ; i<n+1 ; i++){
            double sumatoria = 0;
            int auxi = n-i;
            for(int p = auxi + 1; p < n+1; p++ ){
                sumatoria = sumatoria + expandedMatrix[auxi][p]*values[p];
            }
            if(expandedMatrix[auxi][auxi] == 0) {
                Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                return values;
            }
            values[auxi] = (expandedMatrix[auxi][n+1]-sumatoria)/expandedMatrix[auxi][auxi];

        }

        return values;
    }
}
