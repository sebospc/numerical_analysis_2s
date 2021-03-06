package com.sands.aplication.numeric.fragments.systemEquationsFragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.sands.aplication.numeric.R;
import com.sands.aplication.numeric.fragments.customPopUps.popUpTotalPivoting;
import com.sands.aplication.numeric.fragments.systemEquationsFragment.showStagesModel.showStages;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.sands.aplication.numeric.fragments.systemEquations.animations;
import static com.sands.aplication.numeric.fragments.systemEquations.animatorSet;
import static com.sands.aplication.numeric.fragments.systemEquations.times;

/**
 * A simple {@link Fragment} subclass.
 */
public class totalPivoting extends baseSystemEquations {
    private ToggleButton pauseOrResume;
    private Button stages;

    public totalPivoting() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total_pivoting, container, false);
        matrixResult = view.findViewById(R.id.matrixResult);
        Button run = view.findViewById(R.id.run);
        Button runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        pauseOrResume = view.findViewById(R.id.pause);
        pauseOrResume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (animatorSet != null) {
                    if (isChecked) {

                        if (Build.VERSION.SDK_INT < 19)
                            stopAnimation();
                        else
                            animatorSet.pause();
                    } else {
                        if (Build.VERSION.SDK_INT < 19)
                            startAnimation();
                        else
                            animatorSet.resume();
                    }
                }
            }
        });
        stages = view.findViewById(R.id.stages);
        stages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), showStages.class);
                showStages.stageContent = contentStages;
                startActivity(i);

            }
        });
        pauseOrResume.setEnabled(false);
        pauseOrResume.setVisibility(View.INVISIBLE);
        stages.setEnabled(false);
        stages.setVisibility(View.INVISIBLE);
        run.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                begin();
                if(calc) {
                    pauseOrResume.setEnabled(true);
                    pauseOrResume.setVisibility(View.VISIBLE);
                    stages.setEnabled(true);
                    stages.setVisibility(View.VISIBLE);
                    animatorSet.playSequentially(animations);
                    animatorSet.start();
                }
            }

        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpTotalPivoting.class);
        startActivity(i);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void elimination(double[][] expandedMatrix) {
        contentStages = new LinearLayout(getContext());
        contentStages.setOrientation(LinearLayout.VERTICAL);
        int[] marks = new int[expandedMatrix.length];
        for (int i = 0; i < marks.length; i++)
            marks[i] = i;

        animatorSet = new AnimatorSet();
        animations = new LinkedList<>();
        calc = true;
        for (int k = 0; k < expandedMatrix.length - 1; k++) {
            List<String> multipliers = new LinkedList<>();
            final int auxk = k;
            expandedMatrix = totalPivot(k, expandedMatrix, marks);

            for (int i = k + 1; i < expandedMatrix.length; i++) {
                if (expandedMatrix[k][k] == 0)
                    styleWrongMessage("Error division by 0");

                final double multiplier = expandedMatrix[i][k] / expandedMatrix[k][k];
                final int auxi = i;
                multipliers.add("multiplier " + (i - k) + " = " + multiplier);

                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW, defaultColor).setDuration(times.getProgress() * 500);
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow) matrixResult.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                        } catch (Exception e) {
                            matrixResult.removeAllViews();
                        }
                    }
                });
                animations.add(colorAnimator);
                for (int j = k; j < expandedMatrix.length + 1; j++) {
                    final int auxj = j;
                    expandedMatrix[i][j] = expandedMatrix[i][j] - multiplier * expandedMatrix[k][j];
                    final double value = Math.abs(expandedMatrix[i][j]) <= Math.pow(10, -13) ? 0.0 : expandedMatrix[i][j];

                    colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), operativeColor,
                            defaultColor).setDuration(times.getProgress() * 500);
                    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            try {
                                ((EditText) ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxj)).setText((value + "         ").substring(0, 6));
                                ((TableRow) matrixResult.getChildAt(auxi)).getChildAt(auxj).setBackgroundColor((Integer) animator.getAnimatedValue());
                                ((TableRow) matrixResult.getChildAt(auxk)).getChildAt(auxj).setBackgroundColor(Color.RED);
                            } catch (Exception e) {
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
                            } catch (Exception e) {
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
            addStage(expandedMatrix, auxk, getContext(), multipliers);
        }


        substitution(expandedMatrix, marks);

    }

}
