package com.example.sacrew.numericov4.fragments.systemEquations;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;

import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.matrixAText;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.times;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.xValuesText;

/**
 * A simple {@link Fragment} subclass.
 */
public class gaussSimple extends baseSystemEquations {
    private TableLayout matrixResult;
    private AnimatorSet animatorSet = new AnimatorSet();
    private LinearLayout multipliersLayout;



    public gaussSimple() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gauss_simple, container, false);

        matrixResult = view.findViewById(R.id.matrixResult);
        Button run = view.findViewById(R.id.run);
        multipliersLayout = view.findViewById(R.id.multipiers);

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multipliersLayout.removeAllViews();
                animatorSet.removeAllListeners();
                animatorSet.end();
                animatorSet.cancel();

                begin();
            }

        });



        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void execute(double[][] expandedMatrix){
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void elimination(final double [][] expandedMatrix){
        animatorSet = new AnimatorSet();
        multipliersLayout.removeAllViews();
        List<Animator> animations = new LinkedList<>();

        for(int k = 0; k< expandedMatrix.length-1; k++){
            final int auxk = k;
            ValueAnimator stage = ValueAnimator.ofInt(0,1);
            stage.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    multipliersLayout.addView(defaultEditText("stage "+auxk,0, LinearLayout.LayoutParams.MATCH_PARENT,13));
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(stage);
            for (int i = k + 1; i < expandedMatrix.length; i++){
                if(expandedMatrix[k][k] == 0)
                    System.out.println("error division 0 wiuwiuwiu");
                final double multiplier = expandedMatrix[i][k] / expandedMatrix[k][k];

                System.out.println("multiplier");
                final int auxi = i;

                Integer colorFrom = getResources().getColor(R.color.colorPrimary);
                Integer colorTo = Color.YELLOW;
                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),colorTo,colorFrom).setDuration(times.getProgress()*500);
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow) matrixResult.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                        }catch (Exception e){
                            matrixResult.removeAllViews();
                        }
                    }
                });
                colorAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        multipliersLayout.addView(defaultEditText("multiplier"+auxi+" = "+multiplier,0, LinearLayout.LayoutParams.MATCH_PARENT,10));
                        System.out.println("aqui");
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(colorAnimator);
                for(int j = k; j < expandedMatrix.length + 1; j++){
                    final int auxj = j;
                    colorFrom = getResources().getColor(R.color.colorPrimary);
                    colorTo = Color.CYAN;
                    expandedMatrix[i][j] = expandedMatrix[i][j] - multiplier*expandedMatrix[k][j];
                    final double value = expandedMatrix[i][j];

                    colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),colorTo,colorFrom).setDuration(times.getProgress()*500);
                    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            try {
                                ((EditText) ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxj)).setText((value + "      ").substring(0, 5));
                                ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxj).setBackgroundColor((Integer) animator.getAnimatedValue());
                                ((TableRow) matrixResult.getChildAt(auxk)).getChildAt(auxj).setBackgroundColor(Color.RED);
                            }catch(Exception e){
                                matrixResult.removeAllViews();
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
                                ((TableRow) matrixResult.getChildAt(auxk)).getChildAt(auxj)
                                        .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }catch(Exception e){
                                matrixResult.removeAllViews();
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

            }
        }

        animatorSet.playSequentially(animations);
        animatorSet.start();
        substitution(expandedMatrix);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void substitution(double [][] gauss){
        xValuesText.removeAllViews();
        int n = gauss.length-1;
        double[] values = new double[n+1];
        if(gauss[n][n] == 0)
            System.out.println("wiuwiuwiu division 0");
        double x = gauss[n][n+1]/gauss[n][n];

        values[values.length-1] = x;
        for(int i = 0 ; i<n+1 ; i++){
            double sumatoria = 0;
            int auxi = n-i;
            for(int p = auxi + 1; p < n+1; p++ ){
                sumatoria = sumatoria + gauss[auxi][p]*values[p];
            }
            if(gauss[auxi][auxi] == 0)
                System.out.println("division 0 wiuwiwuwiu");
            values[auxi] = (gauss[auxi][n+1]-sumatoria)/gauss[auxi][auxi];

        }

        for(double val: values){
            xValuesText.addView(defaultEditText((val+"            ").substring(0,5)));
            System.out.println(val);
        }

    }




}
