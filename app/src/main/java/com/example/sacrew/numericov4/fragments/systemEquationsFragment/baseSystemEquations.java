package com.example.sacrew.numericov4.fragments.systemEquationsFragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.TextViewCompat;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Locale;

import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;
import static com.example.sacrew.numericov4.fragments.systemEquations.bValuesText;
import static com.example.sacrew.numericov4.fragments.systemEquations.backMAtrix;
import static com.example.sacrew.numericov4.fragments.systemEquations.matrixAText;
import static com.example.sacrew.numericov4.fragments.systemEquations.matrixBackpack;
import static com.example.sacrew.numericov4.fragments.systemEquations.pivoted;
import static com.example.sacrew.numericov4.fragments.systemEquations.times;
import static com.example.sacrew.numericov4.fragments.systemEquations.xIndex;
import static com.example.sacrew.numericov4.fragments.systemEquations.xValuesText;
import static com.example.sacrew.numericov4.fragments.systemEquations.animations;
/**
 * Created by sacrew on 23/05/18.
 */

public abstract class baseSystemEquations extends Fragment {
    protected TableLayout matrixResult;
    //int defaultColor = Color.rgb(3,169,244);
    int defaultColor = Color.parseColor("#FF303F9F"); //primary
    int operativeColor = Color.parseColor("#64DD17");

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
                aux.addView(defaultEditText((val + "      ").substring(0, 6),defaultColor,100,10,true));
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
        return defaultEditText(value, defaultColor,100,10,true);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public TextView defaultEditText(String value,boolean key) {
        return defaultEditText(value, defaultColor,100,10,key);
    }

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public TextView defaultEditText(String value, int color,int weight,int size, boolean key) {
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
        text.setTypeface(null, Typeface.BOLD);
        text.setBackgroundColor(color);
        text.setTextColor(Color.WHITE);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setKeyListener(DigitsKeyListener.getInstance("0123456789.-E"));
        if(key)
            text.setKeyListener(null);
        text.setText(value);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
                text,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        );
        return text;

    }
    public double[][] swapRows(int k, int higherRow, double[][] expandedMatrix){
        return swapRows(k,higherRow,expandedMatrix,matrixResult,defaultColor);
    }

    public double[][] swapRows(int k, int higherRow, final double[][] expandedMatrix, final TableLayout table,int color){
        final int length = expandedMatrix.length;
        final int auxK = k;
        final int auxHigherRow = higherRow;
        for(int i = 0; i<= length; i++){
            final int auxi = i;
            final double aux = expandedMatrix[k][i];
            expandedMatrix[k][i] = expandedMatrix[higherRow][i];
            expandedMatrix[higherRow][i] = aux;
            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.MAGENTA,
                    color).setDuration(times.getProgress()*500);
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
                    try {
                        if (auxi < ((TableRow) table.getChildAt(0)).getChildCount()) {
                            EditText textAux = (EditText) ((TableRow) table.getChildAt(auxHigherRow)).getChildAt(auxi);
                            String aux2 = textAux.getText().toString();
                            EditText textAux2 = (EditText) ((TableRow) table.getChildAt(auxK)).getChildAt(auxi);
                            textAux.setText(textAux2.getText().toString());
                            textAux2.setText(aux2);
                        } else {
                            EditText textAux = (EditText) bValuesText.getChildAt(auxHigherRow);
                            String aux2 = textAux.getText().toString();
                            EditText textAux2 = (EditText) bValuesText.getChildAt(auxK);
                            textAux.setText(textAux2.getText().toString());
                            textAux2.setText(aux2);
                        }
                        if (!animations.isEmpty()) animations.remove(0);
                    }catch (Exception e){
                        bValuesText.removeAllViews();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    if (auxi < ((TableRow) table.getChildAt(0)).getChildCount()) {
                    ((TableRow) table.getChildAt(auxHigherRow)).getChildAt(auxi)
                            .setBackgroundColor(defaultColor);

                    ((TableRow) table.getChildAt(auxK)).getChildAt(auxi)
                            .setBackgroundColor(defaultColor);
                    }else {
                        bValuesText.getChildAt(auxHigherRow)
                                .setBackgroundColor(defaultColor);

                        bValuesText.getChildAt(auxK)
                                .setBackgroundColor(defaultColor);
                    }
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(colorAnimator);
        }
        return expandedMatrix;
    }



    public void swapColumn(int k, int higherColumn, double[][] expandedMatrix, int [] marks, TableLayout table,int color){
        swapColumn(k,higherColumn,expandedMatrix,marks,table,color,false);
    }
    public void swapColumn(int k, int higherColumn, double[][] expandedMatrix, int [] marks, final TableLayout table,int color, boolean acces){
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
                    color).setDuration(times.getProgress()*500);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxHigherColumn)
                            .setBackgroundColor((Integer) animator.getAnimatedValue());

                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxk)
                            .setBackgroundColor((Integer) animator.getAnimatedValue());
                    if(acces && auxi == 0){
                        xIndex.getChildAt(auxHigherColumn).setBackgroundColor((Integer) animator.getAnimatedValue());
                        xIndex.getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                    }
                }
            });
            colorAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    try {
                        EditText textAux = (EditText) ((TableRow) table.getChildAt(auxi)).getChildAt(auxHigherColumn);
                        String aux2 = textAux.getText().toString();
                        EditText textAux2 = (EditText) ((TableRow) table.getChildAt(auxi)).getChildAt(auxk);
                        textAux.setText(textAux2.getText().toString());
                        textAux2.setText(aux2);
                        if(acces && auxi == 0){
                            EditText textXAux = (EditText)xIndex.getChildAt(auxHigherColumn);
                            String tempAcces = textXAux.getText().toString();
                            EditText textXAux2 = (EditText)xIndex.getChildAt(auxk);
                            textXAux.setText(textXAux2.getText().toString());
                            textXAux2.setText(tempAcces);
                            xIndex.getChildAt(auxHigherColumn).setBackgroundColor(Color.parseColor("#429ffd"));
                            xIndex.getChildAt(auxk).setBackgroundColor(Color.parseColor("#429ffd"));
                        }
                        if (!animations.isEmpty()) animations.remove(0);
                    }catch (Exception e){
                        table.removeAllViews();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxHigherColumn)
                            .setBackgroundColor(defaultColor);
                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxk)
                            .setBackgroundColor(defaultColor);
                    if(acces){
                        xIndex.getChildAt(auxHigherColumn).setBackgroundColor(Color.parseColor("#429ffd"));
                        xIndex.getChildAt(auxk).setBackgroundColor(Color.parseColor("#429ffd"));
                    }

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
        String message = "";
        xValuesText.removeAllViews();
        int n = expandedMatrix.length-1;
        double[] values = new double[n+1];
        if(expandedMatrix[n][n] == 0) {
            //Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
            message = "Error division 0";
            styleWrongMessage(message);
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
                //Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                message = "Error division 0";
                styleWrongMessage(message);
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
        return totalPivot(k,expandedMAtrix,marks,table,defaultColor);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public double[][] totalPivot(int k, double [][] expandedMAtrix, int [] marks, TableLayout table,int color){
        return totalPivot(k,expandedMAtrix,marks,table,color,false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public double[][] totalPivot(int k, double [][] expandedMAtrix, int [] marks, TableLayout table,int color,boolean acces){
        double mayor = 0.0;
        String message = "";
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
            // Toast.makeText(getContext(),  "Error division 0", Toast.LENGTH_SHORT).show();
                message = "Error division 0";
                styleWrongMessage(message);
        }else{
            if(higherRow != k)
                swapRows(k,higherRow,expandedMAtrix,table,color);
            if(higherColumn != k)
                swapColumn(k,higherColumn,expandedMAtrix,marks,table,color,acces);
        }
        return expandedMAtrix;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void securePivot(){

        double [][] expandedMatrix = getMatrix();
        if(animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }
        animatorSet = new AnimatorSet();
        animations = new LinkedList<>();

        if(expandedMatrix != null) {
            xIndex.removeAllViews();
            for(int i = 0; i < expandedMatrix.length;i++){
                xIndex.addView(defaultEditText("X"+(i+1),Color.parseColor("#429ffd"),100,10,true));
            }
            xIndex.setVisibility(View.VISIBLE);


            if (!pivoted) {
                int[] marks = new int[expandedMatrix.length];
                for (int i = 0; i < marks.length; i++) {
                    marks[i] = i + 1;
                }
                matrixBackpack = getMatrix();
                backMAtrix.setVisibility(View.VISIBLE);
                for (int i = 0; i < matrixAText.getChildCount(); i++) {
                    for (int j = 0; j < matrixAText.getChildCount(); j++) {
                        ((EditText) ((TableRow) matrixAText.getChildAt(i)).getChildAt(j)).setKeyListener(null);
                    }
                    ((EditText) bValuesText.getChildAt(i)).setKeyListener(null);
                }
                for (int i = 0; i < expandedMatrix.length; i++) {
                    totalPivot(i, expandedMatrix, marks, matrixAText,defaultColor,true);
                }
                animatorSet.playSequentially(animations);
                animatorSet.start();
                pivoted = true;
            }
        }
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
