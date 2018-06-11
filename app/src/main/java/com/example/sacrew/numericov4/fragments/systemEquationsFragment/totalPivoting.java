package com.example.sacrew.numericov4.fragments.systemEquationsFragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpBisection;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpTotalPivoting;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.util.LinkedList;

import static com.example.sacrew.numericov4.fragments.systemEquations.animations;
import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;
import static com.example.sacrew.numericov4.fragments.systemEquations.times;

/**
 * A simple {@link Fragment} subclass.
 */
public class totalPivoting extends baseSystemEquations {
    private LinearLayout multipliersLayout;
    ScrollView scrollview;
    String mensaje = "";
    public totalPivoting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_total_pivoting, container, false);
        matrixResult = view.findViewById(R.id.matrixResult);
        Button run = view.findViewById(R.id.run);
        multipliersLayout = view.findViewById(R.id.multipiers);
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
                multipliersLayout.removeAllViews();
                animatorSet.removeAllListeners();
                animatorSet.end();
                animatorSet.cancel();

                begin();
            }

        });
        scrollview = ((ScrollView) view.findViewById(R.id.scrollMultipliers));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpTotalPivoting.class);
        startActivity(i);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void elimination(double [][] expandedMatrix){
        int[] marks = new int[expandedMatrix.length];
        for(int i = 0; i< marks.length; i++)
            marks[i] =i;

        animatorSet = new AnimatorSet();
        multipliersLayout.removeAllViews();
        animations = new LinkedList<>();

        for(int k = 0; k< expandedMatrix.length-1; k++){
            final int auxk = k;
            expandedMatrix = totalPivot(k,expandedMatrix, marks);
            ValueAnimator stage = ValueAnimator.ofInt(0,1);
            stage.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    multipliersLayout.addView(defaultEditText("stage "+auxk,defaultColor, LinearLayout.LayoutParams.MATCH_PARENT,13,true));
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!animations.isEmpty()) animations.remove(0);
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
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
                    //Toast.makeText(getContext(),  "Error division 0", Toast.LENGTH_SHORT).show();
                    mensaje = "Error division 0";
                    styleWrongMessage(mensaje);
                final double multiplier = expandedMatrix[i][k] / expandedMatrix[k][k];
                final int auxi = i;


                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,defaultColor).setDuration(times.getProgress()*500);
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
                        multipliersLayout.addView(defaultEditText("multiplier"+(auxi-auxk)+" = "+multiplier,defaultColor, LinearLayout.LayoutParams.MATCH_PARENT,10,true));
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (!animations.isEmpty()) animations.remove(0);
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxk).setBackgroundColor(defaultColor);
                        ((TableRow) matrixResult.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(defaultColor);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(colorAnimator);
                for(int j = k; j < expandedMatrix.length + 1; j++){
                    final int auxj = j;
                    expandedMatrix[i][j] = expandedMatrix[i][j] - multiplier*expandedMatrix[k][j];
                    final double value = Math.abs(expandedMatrix[i][j]) <= Math.pow(10,-13) ? 0.0: expandedMatrix[i][j];

                    colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),operativeColor,
                            defaultColor).setDuration(times.getProgress()*500);
                    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            try {
                                ((EditText) ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxj)).setText((value + "         ").substring(0,6));
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
                                        .setBackgroundColor(defaultColor);
                                if (!animations.isEmpty()) animations.remove(0);
                            }catch(Exception e){
                                matrixResult.removeAllViews();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxj).setBackgroundColor(defaultColor);


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
        substitution(expandedMatrix, marks);

    }

}
