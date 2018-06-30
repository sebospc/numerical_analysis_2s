package com.example.sacrew.numericov4.fragments.systemEquationsFragment;


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
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpPartialPivoting;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.showStagesModel.showStages;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.example.sacrew.numericov4.fragments.systemEquations.animations;
import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;
import static com.example.sacrew.numericov4.fragments.systemEquations.times;

/**
 * A simple {@link Fragment} subclass.
 */
public class partialPivoting extends baseSystemEquations {

    private ToggleButton pauseOrResume;
    private Button stages;
    public partialPivoting() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partial_pivoting, container, false);
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
                if(animatorSet != null) {
                    if (isChecked) {
                        animatorSet.pause();
                    } else {
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
                animatorSet.removeAllListeners();
                animatorSet.end();
                animatorSet.cancel();
                begin();
                pauseOrResume.setEnabled(true);
                pauseOrResume.setVisibility(View.VISIBLE);
                stages.setEnabled(true);
                stages.setVisibility(View.VISIBLE);
                animatorSet.playSequentially(animations);
                animatorSet.start();
            }

        });

        times.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (animatorSet.isRunning()) {
                    animatorSet.cancel();
                    animatorSet = new AnimatorSet();
                    animatorSet.playSequentially(animations);
                    animatorSet.setDuration(times.getProgress() * 500);
                    animatorSet.start();
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpPartialPivoting.class);
        startActivity(i);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void elimination(double[][] expandedMatrix) {
        contentStages = new LinearLayout(getContext());
        contentStages.setOrientation(LinearLayout.VERTICAL);
        animatorSet = new AnimatorSet();
        animations = new LinkedList<>();

        for (int k = 0; k < expandedMatrix.length - 1; k++) {
            List<String> multipliers = new LinkedList<>();
            final int auxk = k;
            expandedMatrix = partialPivot(k, expandedMatrix);
            for (int i = k + 1; i < expandedMatrix.length; i++) {
                if (expandedMatrix[k][k] == 0) {
                    styleWrongMessage("Error division 0");
                    return;
                }

                final double multiplier = expandedMatrix[i][k] / expandedMatrix[k][k];
                final int auxi = i;
                multipliers.add("multiplier "+(i-k)+" = "+multiplier);

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
            addStage(expandedMatrix,auxk,getContext(),multipliers);
        }
        substitution(expandedMatrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private double[][] partialPivot(int k, final double[][] expandedMatrix) {
        double mayor = Math.abs(expandedMatrix[k][k]);
        int filaMayor = k;

        for (int s = k + 1; s < expandedMatrix.length; s++) {
            if (Math.abs(expandedMatrix[s][k]) > mayor) {
                mayor = Math.abs(expandedMatrix[s][k]);
                filaMayor = s;
            }
        }

        if (mayor == 0) {
            //Toast.makeText(getContext(),  "Error division 0", Toast.LENGTH_SHORT).show();
            styleWrongMessage("Error division 0");
        } else if (filaMayor != k) {
            return swapRows(k, filaMayor, expandedMatrix);
        }
        return expandedMatrix;
    }


}
