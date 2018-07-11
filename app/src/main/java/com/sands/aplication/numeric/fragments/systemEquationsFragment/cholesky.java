package com.sands.aplication.numeric.fragments.systemEquationsFragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sands.aplication.numeric.R;
import com.sands.aplication.numeric.fragments.customPopUps.popUpCholesky;
import com.sands.aplication.numeric.fragments.systemEquationsFragment.showStagesModel.showStages;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;

import java.util.LinkedList;
import java.util.Objects;

import static com.sands.aplication.numeric.fragments.systemEquations.animations;
import static com.sands.aplication.numeric.fragments.systemEquations.animatorSet;
import static com.sands.aplication.numeric.fragments.systemEquations.matrixAText;
import static com.sands.aplication.numeric.fragments.systemEquations.times;
import static com.sands.aplication.numeric.fragments.systemEquations.xValuesText;


public class cholesky extends baseFactorizationMethods {
    private TableLayout matrixLText;
    private TableLayout matrixUText;
    private Complex[][] matrixLCholesky;
    private Complex[][] matrixUCholesky;
    private TextView suma;
    private ComplexFormat formater;
    private ToggleButton pauseOrResume;
    private Button stages;

    public cholesky() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cholesky, container, false);

        formater = new ComplexFormat();


        matrixLText = view.findViewById(R.id.matrixL);
        matrixUText = view.findViewById(R.id.matrixU);
        Button run = view.findViewById(R.id.run);
        Button pivoter = view.findViewById(R.id.pivoting);
        suma = view.findViewById(R.id.sum);
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
        pauseOrResume.setEnabled(false);
        pauseOrResume.setVisibility(View.INVISIBLE);
        stages.setEnabled(false);
        stages.setVisibility(View.INVISIBLE);
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
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpCholesky.class);
        startActivity(i);
    }

    private String formating(Complex c) {
        if (c.getReal() == -0.0) {
            c = new Complex(0.0, c.getImaginary());
        }
        if (c.getImaginary() == -0.0) {
            c = new Complex(c.getReal(), 0.0);
        }
        if (c.getImaginary() == 0)
            return c.getReal() + "";
        else if (c.getReal() == 0)
            return ((c.getImaginary() + "").length() <= 5) ? (c.getImaginary() + "i")
                    : (c.getImaginary() + "").substring(0, 5) + "i";
        else return formater.format(c).replaceAll("\\s+", "");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bootStrap(double[][] expandedMatrix) {

        matrixLText.removeAllViews();
        matrixUText.removeAllViews();
        matrixLCholesky = new Complex[expandedMatrix.length][expandedMatrix.length + 1];
        matrixUCholesky = new Complex[expandedMatrix.length][expandedMatrix.length + 1];
        calc = true;
        for (int i = 0; i < matrixLCholesky.length; i++) {
            TableRow rowU = new TableRow(getContext());
            TableRow rowL = new TableRow(getContext());
            for (int j = 0; j <= matrixLCholesky.length; j++) {
                matrixLCholesky[i][j] = new Complex(0, 0);
                matrixUCholesky[i][j] = new Complex(0, 0);
                if (j < matrixLCholesky.length) {
                    rowU.addView(defaultTextView(formating(matrixUCholesky[i][j])));
                    rowL.addView(defaultTextView(formating(matrixLCholesky[i][j])));
                }
            }
            matrixLText.addView(rowL);
            matrixUText.addView(rowU);
        }
        choleskyMethod(expandedMatrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void choleskyMethod(double[][] expandedMatrix) {
        contentStages = new LinearLayout(getContext());
        animatorSet = new AnimatorSet();
        animations = new LinkedList<>();
        for (int k = 0; k < expandedMatrix.length; k++) {
            Complex suma1 = new Complex(0, 0);

            ValueAnimator zero = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                    defaultColor).setDuration(times.getProgress() * 500);
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
            for (int p = 0; p < k; p++) {
                suma1 = suma1.add(matrixLCholesky[k][p].multiply(matrixUCholesky[p][k]));
                //suma1 = suma1 + matrixL[k][p]*matrixU[p][k];

                final int auxp = p;
                final Complex auxSuma = suma1;
                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                        defaultColor).setDuration(times.getProgress() * 500);
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            suma.setText("suma = " + formating(auxSuma));
                        } catch (Exception e) {
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
                        ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor(defaultColor);
                        ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor(defaultColor);
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
                    defaultColor).setDuration(times.getProgress() * 500);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    try {
                        TextView cell = (TextView) ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxk);
                        cell.setBackgroundColor(operativeColor);
                        cell.setText((temp + "      ").substring(0, 6));
                        TextView cell2 = (TextView) ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxk);
                        cell2.setBackgroundColor(operativeColor);
                        cell2.setText((temp + "      ").substring(0, 6));
                        ((TableRow) matrixAText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                        suma.setBackgroundColor(Color.YELLOW);
                    } catch (Exception e) {
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
                                .setBackgroundColor(defaultColor);
                        ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxk)
                                .setBackgroundColor(defaultColor);
                        if (!animations.isEmpty()) animations.remove(0);
                    } catch (Exception e) {
                        matrixUText.removeAllViews();
                        matrixLText.removeAllViews();
                    }
                    suma.setBackgroundColor(0);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    ((TableRow) matrixAText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(defaultColor);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(colorAnimator);
            for (int i = k + 1; i < expandedMatrix.length; i++) {
                Complex suma2 = new Complex(0, 0);
                //double suma2 = 0;

                final int auxi = i;
                for (int p = 0; p < k; p++) {
                    final int auxp = p;
                    suma2 = suma2.add(matrixLCholesky[i][p].multiply(matrixUCholesky[p][k]));
                    //suma2 = suma2 + matrixL[i][p]*matrixU[p][k];
                    final String auxSuma = formating(suma2);
                    ValueAnimator animatronix = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                            defaultColor).setDuration(times.getProgress() * 500);
                    animatronix.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            try {
                                ((TableRow) matrixLText.getChildAt(auxi)).getChildAt(auxp).setBackgroundColor((Integer) animator.getAnimatedValue());
                                ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                                suma.setText("suma = " + auxSuma);
                            } catch (Exception e) {
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
                            ((TableRow) matrixLText.getChildAt(auxi)).getChildAt(auxp).setBackgroundColor(defaultColor);
                            ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor(defaultColor);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    animations.add(animatronix);
                }
                if (matrixUCholesky[k][k].getReal() == 0 && matrixUCholesky[k][k].getImaginary() == 0) {
                    styleWrongMessage("Error division 0");
                    System.out.println(matrixUCholesky[k][k].toString());
                    return;
                }
                matrixLCholesky[i][k] = (new Complex(expandedMatrix[i][k], 0).subtract(suma2))
                        .divide(matrixUCholesky[k][k]);
                final String temp1 = formating(matrixLCholesky[i][k]);
                ValueAnimator animatronix2 = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                        defaultColor).setDuration(times.getProgress() * 500);
                animatronix2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            TextView cell = (TextView) ((TableRow) matrixLText.getChildAt(auxi)).getChildAt(auxk);
                            cell.setBackgroundColor(operativeColor);
                            cell.setText((temp1 + "      ").substring(0, 6));
                            ((TableRow) matrixAText.getChildAt(auxi)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            suma.setBackgroundColor(Color.YELLOW);
                        } catch (Exception e) {
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
                                    .setBackgroundColor(defaultColor);
                            if (!animations.isEmpty()) animations.remove(0);
                        } catch (Exception e) {
                            matrixLText.removeAllViews();
                        }
                        suma.setBackgroundColor(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        ((TableRow) matrixAText.getChildAt(auxi)).getChildAt(auxk).setBackgroundColor(defaultColor);
                        ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(defaultColor);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(animatronix2);
            }
            for (int j = k + 1; j < expandedMatrix.length; j++) {
                Complex suma3 = new Complex(0, 0);
                //double suma3 = 0;
                final int auxj = j;
                for (int p = 0; p < k; p++) {
                    final int auxp = p;
                    suma3 = suma3.add(matrixLCholesky[k][p].multiply(matrixUCholesky[p][j]));
                    //suma3 = suma3 + matrixL[k][p]*matrixU[p][j];
                    final String auxSuma = formating(suma3);
                    ValueAnimator animatronix = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                            defaultColor).setDuration(times.getProgress() * 500);
                    animatronix.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            try {
                                ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor((Integer) animator.getAnimatedValue());
                                ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxj).setBackgroundColor((Integer) animator.getAnimatedValue());
                                suma.setText("suma = " + auxSuma);
                            } catch (Exception e) {
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
                            ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor(defaultColor);
                            ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxj).setBackgroundColor(defaultColor);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    animations.add(animatronix);
                }
                if (matrixLCholesky[k][k].getReal() == 0 && matrixLCholesky[k][k].getImaginary() == 0) {
                    // Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                    styleWrongMessage("Error division 0");
                    System.out.println(matrixLCholesky[k][k].toString());
                    return;
                }
                matrixUCholesky[k][j] = (new Complex(expandedMatrix[k][j]).subtract(suma3))
                        .divide(matrixLCholesky[k][k]);
                //matrixU[k][j] = (expandedMatrix[k][j] - suma3)/matrixL[k][k];
                final String temp2 = formating(matrixUCholesky[k][j]);
                //final double temp2 = matrixU[k][j];
                ValueAnimator animatronix2 = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                        defaultColor).setDuration(times.getProgress() * 500);
                animatronix2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow) matrixAText.getChildAt(auxk)).getChildAt(auxj).setBackgroundColor((Integer) animator.getAnimatedValue());
                            TextView cell = (TextView) ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxj);
                            cell.setBackgroundColor(operativeColor);
                            cell.setText((temp2 + "     ").substring(0, 6));
                            suma.setBackgroundColor(Color.YELLOW);
                        } catch (Exception e) {
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
                                    .setBackgroundColor(defaultColor);
                            if (!animations.isEmpty()) animations.remove(0);
                        } catch (Exception e) {
                            matrixUText.removeAllViews();
                        }
                        suma.setBackgroundColor(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(defaultColor);
                        ((TableRow) matrixAText.getChildAt(auxk)).getChildAt(auxj).setBackgroundColor(defaultColor);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(animatronix2);
            }
            matrixLCholesky[k][matrixLCholesky.length] = new Complex(expandedMatrix[k][expandedMatrix.length]);
        }
        progresiveSubstitution(matrixLCholesky);
        addFactorizationCholesky(matrixLCholesky, matrixUCholesky, getContext());
        substitution(matrixUCholesky);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void progresiveSubstitution(Complex[][] matrixLCholesky) {
        int n = matrixLCholesky.length - 1;
        Complex[] x = new Complex[n + 1];
        if (matrixUCholesky[0][0].getReal() == 0 && matrixUCholesky[0][0].getImaginary() == 0) {
            styleWrongMessage("Error division 0 in progressive substitution");
            return;
        }
        x[0] = matrixUCholesky[0][n + 1].divide(matrixUCholesky[0][0]);
        for (int i = 0; i < n + 1; i++) {
            Complex sumatoria = new Complex(0, 0);
            for (int p = 0; p < i; p++) {
                sumatoria = sumatoria.add(matrixLCholesky[i][p].multiply(x[p]));
            }
            if (matrixLCholesky[i][i].getReal() == 0 && matrixLCholesky[i][i].getImaginary() == 0) {
                styleWrongMessage("Error division 0 in progressive substitution");
                return;
            }
            x[i] = (matrixLCholesky[i][n + 1].subtract(sumatoria)).divide(matrixLCholesky[i][i]);
            matrixUCholesky[i][n + 1] = x[i];
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void substitution(Complex[][] expandedMatrix) {
        xValuesText.removeAllViews();
        int n = expandedMatrix.length - 1;
        Complex[] values = new Complex[n + 1];
        if (expandedMatrix[n][n].getReal() == 0 && expandedMatrix[n][n].getImaginary() == 0) {
            styleWrongMessage("Error division 0");
            return;
        }
        Complex x = expandedMatrix[n][n + 1].divide(expandedMatrix[n][n]);

        values[values.length - 1] = x;
        for (int i = 0; i < n + 1; i++) {
            Complex sumatoria = new Complex(0, 0);
            int auxi = n - i;
            for (int p = auxi + 1; p < n + 1; p++) {
                sumatoria = sumatoria.add(expandedMatrix[auxi][p].multiply(values[p]));
            }
            if (expandedMatrix[auxi][auxi].getReal() == 0 && expandedMatrix[auxi][auxi].getImaginary() == 0) {
                styleWrongMessage("Error division 0");
                return;
            }
            values[auxi] = (expandedMatrix[auxi][n + 1].subtract(sumatoria)).divide(expandedMatrix[auxi][auxi]);

        }

        for (Complex val : values) {
            xValuesText.addView(defaultTextView((formating(val) + "            ").substring(0, 6)));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addFactorizationCholesky(Complex[][] matrixL, Complex[][] matrixU, Context context) {
        LinearLayout L = new LinearLayout(context);
        L.setOrientation(LinearLayout.VERTICAL);
        TextView tittleL = new TextView(context);
        tittleL.setText(" L ");
        L.addView(tittleL);
        LinearLayout U = new LinearLayout(context);
        U.setOrientation(LinearLayout.VERTICAL);
        TextView tittleU = new TextView(context);
        tittleU.setText(" U ");
        U.addView(tittleU);

        TableLayout matrixResultL = new TableLayout(context);
        TableLayout matrixResultU = new TableLayout(context);
        for (int i = 0; i < matrixL.length; i++) {
            TableRow auxL = new TableRow(context);
            TableRow auxU = new TableRow(context);
            for (int j = 0; j <= matrixL.length; j++) {
                if (j < matrixL.length) {
                    auxL.addView(defaultTextView((formating(matrixL[i][j]) + "       ").substring(0, 6)));
                    auxU.addView(defaultTextView((formating(matrixU[i][j]) + "       ").substring(0, 6)));
                } else {
                    auxL.addView(defaultTextView((formating(matrixU[i][j]) + "       ").substring(0, 6), getResources().getColor(R.color.prettyRed)));
                    auxU.addView(defaultTextView((formating(matrixU[i][j]) + "       ").substring(0, 6), getResources().getColor(R.color.header_line_color)));
                }
            }
            matrixResultL.addView(auxL);
            matrixResultU.addView(auxU);
        }
        L.addView(matrixResultL);
        U.addView(matrixResultU);
        contentStages.addView(L);
        TextView space = new TextView(context);
        space.setText("    ");
        contentStages.addView(space);
        contentStages.addView(U);
    }
}
