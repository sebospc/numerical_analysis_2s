package com.example.sacrew.numericov4.fragments.systemEquationsFragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;

import java.util.LinkedList;


import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;
import static com.example.sacrew.numericov4.fragments.systemEquations.times;
import static com.example.sacrew.numericov4.fragments.systemEquations.animations;

/**
 * A simple {@link Fragment} subclass.
 */
public class gaussSimple extends baseSystemEquations {

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
    public void elimination(final double [][] expandedMatrix){
        animatorSet = new AnimatorSet();
        multipliersLayout.removeAllViews();
        animations = new LinkedList<>();

        for(int k = 0; k< expandedMatrix.length-1; k++){
            final int auxk = k;
            ValueAnimator stage = ValueAnimator.ofInt(0,1);
            stage.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    multipliersLayout.addView(defaultEditText("stage "+auxk,0, LinearLayout.LayoutParams.MATCH_PARENT,13,true));
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if(!animations.isEmpty())animations.remove(0);
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
                if(expandedMatrix[k][k] == 0) {
                    Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                final double multiplier = expandedMatrix[i][k] / expandedMatrix[k][k];
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
                        multipliersLayout.addView(defaultEditText("multiplier"+(auxi-auxk)+" = "+multiplier,0, LinearLayout.LayoutParams.MATCH_PARENT,10,true));
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if(!animations.isEmpty())animations.remove(0);
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
                    final double value = Math.abs(expandedMatrix[i][j]) <= Math.pow(10,-13) ? 0.0: expandedMatrix[i][j];

                    colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),colorTo,colorFrom).setDuration(times.getProgress()*500);
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
                                        .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                if(!animations.isEmpty())animations.remove(0);
                            }catch(Exception e){
                                matrixResult.removeAllViews();
                            }

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxj)
                                    .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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

        animatorSet.addListener(new AnimatorListenerAdapter() {
        });
        animatorSet.start();
        substitution(expandedMatrix);
    }





}
