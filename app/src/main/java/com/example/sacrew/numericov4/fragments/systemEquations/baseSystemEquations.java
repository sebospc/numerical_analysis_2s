package com.example.sacrew.numericov4.fragments.systemEquations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
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

import java.net.ConnectException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.animatorSet;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.bValuesText;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.matrixAText;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.times;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.xValuesText;

/**
 * Created by sacrew on 23/05/18.
 */

public abstract class baseSystemEquations extends Fragment {
    protected TableLayout matrixResult;

    protected List<Animator> animations;
    public baseSystemEquations(){

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void begin(){
        double [][] expandedMatrix = getMatrix();
        if(expandedMatrix!= null)
            bootStrap(expandedMatrix);
    }



    public double[][] getMatrix(){
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
                    return null;
                }
            }
            EditText aux = ((EditText)bValuesText.getChildAt(i));
            try{
                double x = Double.parseDouble(aux.getText().toString());
                expandedMatrix[i][n] = x;
            }catch (Exception e){
                aux.setError("invalid value");
                return null;
            }
        }
        return expandedMatrix;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bootStrap(double[][] expandedMatrix){
        matrixResult.removeAllViews();
        for (double[] v : expandedMatrix) {
            TableRow aux = new TableRow(getContext());
            for (double val : v) {
                aux.addView(defaultEditText((val + "      ").substring(0, 6)));
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
        Context context;
        if(isAdded()){
            context =getContext();

        }else {
         context = ((EditText)((TableRow)matrixAText.getChildAt(0)).getChildAt(0)).getContext();
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }
        TextView text = new EditText(context);
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
        return swapRows(k,higherRow,expandedMatrix,matrixResult);
    }
    public double[][] swapRows(int k, int higherRow, final double[][] expandedMatrix, final TableLayout table){
        final int length = expandedMatrix.length;
        final int auxK = k;
        final int auxHigherRow = higherRow;
        for(int i = 0; i<= length; i++){
            final int auxi = i;
            final double aux = expandedMatrix[k][i];
            expandedMatrix[k][i] = expandedMatrix[higherRow][i];
            expandedMatrix[higherRow][i] = aux;
            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.MAGENTA,
                    getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    if(auxi < ((TableRow)table.getChildAt(0)).getChildCount()) {
                        ((TableRow) table.getChildAt(auxHigherRow)).getChildAt(auxi)
                                .setBackgroundColor((Integer) animator.getAnimatedValue());

                        ((TableRow) table.getChildAt(auxK)).getChildAt(auxi)
                                .setBackgroundColor((Integer) animator.getAnimatedValue());
                    }else {
                         bValuesText.getChildAt(auxHigherRow)
                                .setBackgroundColor((Integer) animator.getAnimatedValue());

                        bValuesText.getChildAt(auxK)
                                .setBackgroundColor((Integer) animator.getAnimatedValue());
                    }
                }
            });
            colorAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if(auxi < ((TableRow)table.getChildAt(0)).getChildCount()) {
                        EditText textAux = (EditText) ((TableRow) table.getChildAt(auxHigherRow)).getChildAt(auxi);
                        String aux2 = textAux.getText().toString();
                        EditText textAux2 = (EditText) ((TableRow) table.getChildAt(auxK)).getChildAt(auxi);
                        textAux.setText(textAux2.getText().toString());
                        textAux2.setText(aux2);
                    }else{
                        EditText textAux = (EditText)  bValuesText.getChildAt(auxHigherRow);
                        String aux2 = textAux.getText().toString();
                        EditText textAux2 = (EditText) bValuesText.getChildAt(auxK);
                        textAux.setText(textAux2.getText().toString());
                        textAux2.setText(aux2);
                    }
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
        swapColumn(k,higherColumn,expandedMatrix,marks,matrixResult);
    }
    public void swapColumn(int k, int higherColumn, double[][] expandedMatrix, int [] marks, final TableLayout table){
        if(marks != null) {
            int aux = marks[k];
            marks[k] = marks[higherColumn];
            marks[higherColumn] = aux;
        }
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
                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxHigherColumn)
                            .setBackgroundColor((Integer) animator.getAnimatedValue());

                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxk)
                            .setBackgroundColor((Integer) animator.getAnimatedValue());
                }
            });
            colorAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    EditText textAux = (EditText) ((TableRow) table.getChildAt(auxi)).getChildAt(auxHigherColumn);
                    String aux2 = textAux.getText().toString();
                    EditText textAux2 = (EditText) ((TableRow) table.getChildAt(auxi)).getChildAt(auxk);
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
            xValuesText.addView(defaultEditText((val+"            ").substring(0,6)));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void substitution(double[][] expandedMatrix, int [] marks){
        double [] result = substitution(expandedMatrix,-1);
        double [] clean = new double[result.length];
        for(int i = 0; i< result.length; i++){
            double val = result[i];
            clean[marks[i]] = val;
        }
        for(double val:clean){
            xValuesText.addView(defaultEditText((val+"            ").substring(0,6)));
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public double[][] totalPivot(int k, double [][] expandedMAtrix, int [] marks) {
        return totalPivot(k,expandedMAtrix,marks,matrixResult);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public double[][] totalPivot(int k, double [][] expandedMAtrix, int [] marks, TableLayout table){
        double mayor = 0.0;
        int higherRow= k;
        int higherColumn = k;
        for(int r = k; r< expandedMAtrix.length; r++){
            for(int s = k; s< expandedMAtrix.length; s++){
                if(Math.abs(expandedMAtrix[r][s]) > mayor){
                    mayor = Math.abs(expandedMAtrix[r][s]);
                    higherRow = r;
                    higherColumn = s;
                }
            }
        }
        if(mayor == 0){
            Toast.makeText(getContext(),  "Error division 0", Toast.LENGTH_SHORT).show();
        }else{
            if(higherRow != k)
                swapRows(k,higherRow,expandedMAtrix,table);
            if(higherColumn != k)
                swapColumn(k,higherColumn,expandedMAtrix,marks,table);
        }
        return expandedMAtrix;
    }
}
