package com.example.sacrew.numericov4.fragments.systemEquationsFragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpBisection;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpCholesky;

/**
 * A simple {@link Fragment} subclass.
 */
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;

import java.util.LinkedList;

import static com.example.sacrew.numericov4.fragments.systemEquations.animations;
import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;
import static com.example.sacrew.numericov4.fragments.systemEquations.bValuesText;
import static com.example.sacrew.numericov4.fragments.systemEquations.matrixAText;
import static com.example.sacrew.numericov4.fragments.systemEquations.times;
import static com.example.sacrew.numericov4.fragments.systemEquations.xValuesText;

public class cholesky extends baseFactorizationMethods{
    private TableLayout matrixLText;
    private TableLayout matrixUText;
    private Complex[][] matrixLCholesky;
    private Complex[][] matrixUCholesky;
    private TextView suma;
    private ComplexFormat formater;

    public cholesky() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cholesky, container, false);
        Complex prueba = new Complex(-4,0);

        Complex aux = prueba.sqrt();
        formater = new ComplexFormat();


        //System.out.println("toString "+ d.format(aux).replaceAll("\\s+","") +" real "+aux.getReal()+" imaginary "+aux.getImaginary());


        matrixLText = view.findViewById(R.id.matrixL);
        matrixUText = view.findViewById(R.id.matrixU);
        Button run = view.findViewById(R.id.run);
        Button pivoter = view.findViewById(R.id.pivoting);
        suma = view.findViewById(R.id.suma);
        Button runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        run.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                animatorSet.removeAllListeners();
                animatorSet.end();
                animatorSet.cancel();

                begin();
            }

        });
        pivoter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                securePivot();
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpCholesky.class);
        startActivity(i);
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
        else return formater.format(c).replaceAll("\\s+","");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bootStrap(double[][] expandedMatrix){

        matrixLText.removeAllViews();
        matrixUText.removeAllViews();
        matrixLCholesky = new Complex[expandedMatrix.length][expandedMatrix.length+1];
        matrixUCholesky = new Complex[expandedMatrix.length][expandedMatrix.length+1];
        for(int i = 0; i< matrixLCholesky.length; i++){
            TableRow rowU = new TableRow(getContext());
            TableRow rowL = new TableRow(getContext());
            for(int j = 0; j<= matrixLCholesky.length; j++){
                matrixLCholesky[i][j] = new Complex(0,0);
                matrixUCholesky[i][j] = new Complex(0,0);
                rowU.addView(defaultEditText(formating(matrixUCholesky[i][j])));
                rowL.addView(defaultEditText(formating(matrixLCholesky[i][j])));
            }
            matrixLText.addView(rowL);
            matrixUText.addView(rowU);
        }
        choleskyMethod(expandedMatrix);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void choleskyMethod(double[][] expandedMatrix){
        animatorSet = new AnimatorSet();
        animations = new LinkedList<>();
        for(int k = 0; k < expandedMatrix.length; k++){
            Complex suma1 = new Complex(0,0);

            ValueAnimator zero= ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                    getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
            zero.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    suma.setText("suma = 0");
                }
            });
            zero.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!animations.isEmpty()) animations.remove(0);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(zero);
            final int auxk = k;
            for(int p = 0; p<k ; p++){
                suma1 = suma1.add(matrixLCholesky[k][p].multiply(matrixUCholesky[p][k]));
                //suma1 = suma1 + matrixL[k][p]*matrixU[p][k];

                final int auxp = p;
                final Complex auxSuma = suma1;
                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                        getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            ((TableRow)matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow)matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            suma.setText("suma = "+formating(auxSuma));
                        }catch (Exception e){
                            matrixLText.removeAllViews();
                            matrixUText.removeAllViews();
                        }
                    }
                });
                colorAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (!animations.isEmpty()) animations.remove(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        ((TableRow)matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        ((TableRow)matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(colorAnimator);
            }
            matrixLCholesky[k][k] = ((new Complex(expandedMatrix[k][k])).subtract(suma1)).sqrt();
            matrixUCholesky[k][k] = ((new Complex(expandedMatrix[k][k])).subtract(suma1)).sqrt();
            final String temp = formating(matrixLCholesky[k][k]);
            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                    getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    try {
                        TextView cell = (TextView) ((TableRow)matrixUText.getChildAt(auxk)).getChildAt(auxk);
                        cell.setBackgroundColor(Color.CYAN);
                        cell.setText((temp+"      ").substring(0,6));
                        TextView cell2 = (TextView) ((TableRow)matrixLText.getChildAt(auxk)).getChildAt(auxk);
                        cell2.setBackgroundColor(Color.CYAN);
                        cell2.setText((temp+"      ").substring(0,6));
                        ((TableRow)matrixAText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                        suma.setBackgroundColor(Color.YELLOW);
                    }catch (Exception e){
                        matrixLText.removeAllViews();
                        matrixUText.removeAllViews();
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
                        ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxk)
                                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxk)
                                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        if (!animations.isEmpty()) animations.remove(0);
                    }catch(Exception e){
                        matrixUText.removeAllViews();
                        matrixLText.removeAllViews();
                    }
                    suma.setBackgroundColor(0);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    ((TableRow)matrixAText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(colorAnimator);
            for(int i = k+1;i< expandedMatrix.length;i++){
                Complex suma2 = new Complex(0,0);
                //double suma2 = 0;

                final int auxi = i;
                for(int p = 0; p < k; p++){
                    final int auxp = p;
                    suma2 = suma2.add(matrixLCholesky[i][p].multiply(matrixUCholesky[p][k]));
                    //suma2 = suma2 + matrixL[i][p]*matrixU[p][k];
                    final String auxSuma = formating(suma2);
                    ValueAnimator animatronix = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                            getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
                    animatronix.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            try {
                                ((TableRow)matrixLText.getChildAt(auxi)).getChildAt(auxp).setBackgroundColor((Integer) animator.getAnimatedValue());
                                ((TableRow)matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                                suma.setText("suma = "+auxSuma);
                            }catch (Exception e){
                                matrixLText.removeAllViews();
                                matrixUText.removeAllViews();
                            }
                        }
                    });
                    animatronix.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!animations.isEmpty()) animations.remove(0);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            ((TableRow)matrixLText.getChildAt(auxi)).getChildAt(auxp).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            ((TableRow)matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    animations.add(animatronix);
                }
                if(matrixUCholesky[k][k].getReal() == 0 && matrixUCholesky[k][k].getImaginary() == 0) {
                    Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                    System.out.println(matrixUCholesky[k][k].toString());
                    return;
                }
                matrixLCholesky[i][k] = (new Complex(expandedMatrix[i][k],0).subtract(suma2))
                        .divide(matrixUCholesky[k][k]);
                //matrixL[i][k] = (expandedMatrix[i][k]-suma2)/matrixU[k][k];
                final String temp1 = formating(matrixLCholesky[i][k]);
                //final double temp1 = matrixL[i][k];
                ValueAnimator animatronix2 = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                        getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
                animatronix2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            TextView cell = (TextView) ((TableRow)matrixLText.getChildAt(auxi)).getChildAt(auxk);
                            cell.setBackgroundColor(Color.CYAN);
                            cell.setText((temp1+"      ").substring(0,6));
                            ((TableRow)matrixAText.getChildAt(auxi)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow)matrixUText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            suma.setBackgroundColor(Color.YELLOW);
                        }catch (Exception e){
                            matrixLText.removeAllViews();
                            matrixUText.removeAllViews();
                        }
                    }
                });
                animatronix2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        try {
                            ((TableRow) matrixLText.getChildAt(auxi)).getChildAt(auxk)
                                    .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            if (!animations.isEmpty()) animations.remove(0);
                        }catch (Exception e){
                            matrixLText.removeAllViews();
                        }
                        suma.setBackgroundColor(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        ((TableRow)matrixAText.getChildAt(auxi)).getChildAt(auxk).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        ((TableRow)matrixUText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(animatronix2);
            }
            for(int j = k+1; j < expandedMatrix.length; j++){
                Complex suma3 = new Complex(0,0);
                //double suma3 = 0;
                final int auxj = j;
                for(int p = 0; p < k; p++){
                    final int auxp = p;
                    suma3 = suma3.add(matrixLCholesky[k][p].multiply(matrixUCholesky[p][j]));
                    //suma3 = suma3 + matrixL[k][p]*matrixU[p][j];
                    final String auxSuma = formating(suma3);
                    ValueAnimator animatronix = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                            getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
                    animatronix.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            try {
                                ((TableRow)matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor((Integer) animator.getAnimatedValue());
                                ((TableRow)matrixUText.getChildAt(auxp)).getChildAt(auxj).setBackgroundColor((Integer) animator.getAnimatedValue());
                                suma.setText("suma = "+auxSuma);
                            }catch (Exception e){
                                matrixLText.removeAllViews();
                                matrixUText.removeAllViews();
                            }
                        }
                    });
                    animatronix.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!animations.isEmpty()) animations.remove(0);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            ((TableRow)matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            ((TableRow)matrixUText.getChildAt(auxp)).getChildAt(auxj).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    animations.add(animatronix);
                }
                if(matrixLCholesky[k][k].getReal() == 0 && matrixLCholesky[k][k].getImaginary() == 0) {
                    Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                    System.out.println(matrixLCholesky[k][k].toString());
                    return;
                }
                matrixUCholesky[k][j] = (new Complex(expandedMatrix[k][j]).subtract(suma3))
                        .divide(matrixLCholesky[k][k]);
                //matrixU[k][j] = (expandedMatrix[k][j] - suma3)/matrixL[k][k];
                final String temp2 = formating(matrixUCholesky[k][j]);
                //final double temp2 = matrixU[k][j];
                ValueAnimator animatronix2 = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                        getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
                animatronix2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            ((TableRow)matrixLText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow)matrixAText.getChildAt(auxk)).getChildAt(auxj).setBackgroundColor((Integer) animator.getAnimatedValue());
                            TextView cell =(TextView)((TableRow)matrixUText.getChildAt(auxk)).getChildAt(auxj);
                            cell.setBackgroundColor(Color.CYAN);
                            cell.setText((temp2+"     ").substring(0,6));
                            suma.setBackgroundColor(Color.YELLOW);
                        }catch (Exception e){
                            matrixLText.removeAllViews();
                            matrixUText.removeAllViews();
                        }
                    }
                });
                animatronix2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        try {
                            ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxj)
                                    .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            if (!animations.isEmpty()) animations.remove(0);
                        }catch (Exception e){
                            matrixUText.removeAllViews();
                        }
                        suma.setBackgroundColor(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        ((TableRow)matrixLText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        ((TableRow)matrixAText.getChildAt(auxk)).getChildAt(auxj).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(animatronix2);
            }
            matrixLCholesky[k][matrixLCholesky.length] = new Complex(expandedMatrix[k][expandedMatrix.length]);

            ValueAnimator animatronco = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                    getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
            animatronco.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    try {
                        String aux = ((EditText) bValuesText.getChildAt(auxk)).getText().toString();
                        ((EditText) ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(matrixLText.getChildCount())).setText(aux);
                    }catch (Exception e){
                        matrixLText.removeAllViews();
                    }
                }
            });
            animatronco.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!animations.isEmpty()) animations.remove(0);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(animatronco);
        }

        Complex [] x = progresiveSubstitution(matrixLCholesky);
        for(int i = 0; i< x.length; i++){
            final int auxi = i;
            final String val = formating(x[i]);
            ValueAnimator animatronco = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                    getResources().getColor(R.color.colorPrimary)).setDuration(times.getProgress()*500);
            animatronco.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    try {
                        ((EditText) ((TableRow) matrixUText.getChildAt(auxi)).getChildAt(matrixUText.getChildCount())).setText((val+"     ").substring(0,5));
                    }catch (Exception e){
                        matrixUText.removeAllViews();
                    }
                }
            });
            animatronco.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!animations.isEmpty()) animations.remove(0);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(animatronco);
        }
        substitution(matrixUCholesky);
        animatorSet.playSequentially(animations);
        animatorSet.start();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public Complex[] progresiveSubstitution(Complex[][] matrixLCholesky){
        int n = matrixLCholesky.length-1;
        Complex [] x = new Complex[n+1];
        if(matrixUCholesky[0][0].getReal() == 0 && matrixUCholesky[0][0].getImaginary() == 0) {
            Toast.makeText(getContext(), "Error division 0 in progressive substitution", Toast.LENGTH_SHORT).show();
            return x;
        }
        x[0] = matrixUCholesky[0][n+1].divide(matrixUCholesky[0][0]);
        for(int i = 0; i< n+1;i++) {
            Complex sumatoria = new Complex(0,0);
            for(int p = 0; p < i ; p++){
                sumatoria = sumatoria.add(matrixLCholesky[i][p].multiply(x[p]));
            }
            if(matrixLCholesky[i][i].getReal() == 0 && matrixLCholesky[i][i].getImaginary() == 0) {
                Toast.makeText(getContext(), "Error division 0 in progressive substitution", Toast.LENGTH_SHORT).show();
                return x;
            }
            x[i] = (matrixLCholesky[i][n+1].subtract(sumatoria)).divide(matrixLCholesky[i][i]);
            //x[i] = (matrixL[i][n+1]-sumatoria)/matrixL[i][i];
            matrixUCholesky[i][n+1] = x[i];
        }

        return x;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void substitution(Complex[][] expandedMatrix){
        xValuesText.removeAllViews();
        int n = expandedMatrix.length-1;
        Complex[] values = new Complex[n+1];
        if(expandedMatrix[n][n].getReal() == 0 && expandedMatrix[n][n].getImaginary() == 0) {
            Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
            return;
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
                return;
            }
            values[auxi] = (expandedMatrix[auxi][n+1].subtract(sumatoria)).divide(expandedMatrix[auxi][auxi]);

        }

        for(Complex val:values){
            xValuesText.addView(defaultEditText((formating(val)+"            ").substring(0,6)));
        }

    }

}
