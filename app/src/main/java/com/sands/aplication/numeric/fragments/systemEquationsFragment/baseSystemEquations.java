package com.sands.aplication.numeric.fragments.systemEquationsFragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sands.aplication.numeric.fragments.oneVariable;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.sands.aplication.numeric.fragments.systemEquations.animations;
import static com.sands.aplication.numeric.fragments.systemEquations.animatorSet;
import static com.sands.aplication.numeric.fragments.systemEquations.bValuesText;
import static com.sands.aplication.numeric.fragments.systemEquations.backMAtrix;
import static com.sands.aplication.numeric.fragments.systemEquations.matrixAText;
import static com.sands.aplication.numeric.fragments.systemEquations.matrixBackpack;
import static com.sands.aplication.numeric.fragments.systemEquations.pivoted;
import static com.sands.aplication.numeric.fragments.systemEquations.times;
import static com.sands.aplication.numeric.fragments.systemEquations.xIndex;
import static com.sands.aplication.numeric.fragments.systemEquations.xValuesText;

/**
 * Created by sacrew on 23/05/18.
 */

public abstract class baseSystemEquations extends Fragment {
    final int defaultColor = Color.parseColor("#FF303F9F"); //primary
    //int defaultColor = Color.rgb(3,169,244);
    final int operativeColor = Color.parseColor("#64DD17");
    private final SuperActivityToast.OnButtonClickListener onButtonClickListener =
            new SuperActivityToast.OnButtonClickListener() {

                @Override
                public void onClick(View view, Parcelable token) {
                    SuperActivityToast.cancelAllSuperToasts();
                }
            };
    LinearLayout contentStages;
    TableLayout matrixResult;
    private long playTime;
    private int startPosition;
    boolean calc = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    void begin() {
        if(animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }
        double[][] expandedMatrix = getMatrix();
        if (expandedMatrix != null)
            bootStrap(expandedMatrix);
        else
            calc = false;
    }

    void stopAnimation() {
        if (animatorSet.isRunning()) {
            ArrayList<Animator> animators = animatorSet.getChildAnimations();
            for (int i = 0; i < animators.size(); i++) {
                Animator animator = animators.get(i);
                if (animator.isStarted()) {
                    startPosition = i;
                    playTime = ((ValueAnimator) animator).getCurrentPlayTime();
                    animatorSet.cancel();
                    break;
                }
            }
        }
    }

    void startAnimation() {
        if (!animatorSet.isRunning()) {
            List<Animator> anims = animatorSet.getChildAnimations().subList(startPosition, animatorSet.getChildAnimations().size());
            // Restore the play time of the first animator.
            ((ValueAnimator) anims.get(0)).setCurrentPlayTime(playTime);
            animatorSet = new AnimatorSet();
            animatorSet.playSequentially(anims);
            animatorSet.start();
        }
    }

    private double[][] getMatrix() {
        int n = matrixAText.getChildCount();
        double[][] expandedMatrix = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                EditText aux = ((EditText) ((TableRow) matrixAText.getChildAt(i)).getChildAt(j));
                try {
                    double x;

                    x = Double.parseDouble((aux.getText())
                            .toString());

                    expandedMatrix[i][j] = x;
                } catch (Exception e) {
                    aux.setError("invalid value");
                    return null;
                }
            }
            EditText aux = ((EditText) bValuesText.getChildAt(i));
            try {
                double x = Double.parseDouble(aux.getText().toString());
                expandedMatrix[i][n] = x;
            } catch (Exception e) {
                aux.setError("invalid value");
                return null;
            }
        }
        return expandedMatrix;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void bootStrap(double[][] expandedMatrix) {
        matrixResult.removeAllViews();
        for (double[] v : expandedMatrix) {
            TableRow aux = new TableRow(getContext());
            for (double val : v) {
                aux.addView(defaultTextView((val + "      ").substring(0, 6), defaultColor));
            }
            matrixResult.addView(aux);
        }
        elimination(expandedMatrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    TextView defaultTextView(String value) {
        return defaultTextView(value, defaultColor);
    }

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    TextView defaultTextView(String value, int color) {
        Context context;
        if (isAdded()) {
            context = getContext();

        } else {
            context = ((TableRow) matrixAText.getChildAt(0)).getChildAt(0).getContext();
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }
        TextView text = new EditText(context);
        text.setLayoutParams(new TableRow.LayoutParams(100, 110));
        text.setEms(2);
        text.setMaxLines(1);
        text.setTypeface(null, Typeface.BOLD);
        text.setBackgroundColor(color);
        text.setTextColor(Color.WHITE);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setKeyListener(DigitsKeyListener.getInstance("0123456789.-E"));
        text.setKeyListener(null);
        text.setText(value);

        TextViewCompat.setAutoSizeTextTypeWithDefaults(
                text, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        );
        return text;

    }

    double[][] swapRows(int k, int higherRow, double[][] expandedMatrix) {
        return swapRows(k, higherRow, expandedMatrix, matrixResult, defaultColor);
    }

    private double[][] swapRows(int k, int higherRow, final double[][] expandedMatrix, final TableLayout table, int color) {
        final int length = expandedMatrix.length;
        final int auxK = k;
        final int auxHigherRow = higherRow;
        for (int i = 0; i <= length; i++) {
            final int auxi = i;
            final double aux = expandedMatrix[k][i];
            expandedMatrix[k][i] = expandedMatrix[higherRow][i];
            expandedMatrix[higherRow][i] = aux;
            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.MAGENTA,
                    color).setDuration(times.getProgress() * 500);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    if (auxi < ((TableRow) table.getChildAt(0)).getChildCount()) {
                        ((TableRow) table.getChildAt(auxHigherRow)).getChildAt(auxi)
                                .setBackgroundColor((Integer) animator.getAnimatedValue());

                        ((TableRow) table.getChildAt(auxK)).getChildAt(auxi)
                                .setBackgroundColor((Integer) animator.getAnimatedValue());
                    } else {
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
                    } catch (Exception e) {
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
                    } else {
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

    private void swapColumn(int k, int higherColumn, double[][] expandedMatrix, int[] marks, final TableLayout table, int color, boolean acces) {
        if (marks != null) {
            int aux = marks[k];
            marks[k] = marks[higherColumn];
            marks[higherColumn] = aux;
        }
        final int auxHigherColumn = higherColumn;
        final int auxk = k;
        for (int i = 0; i < expandedMatrix.length; i++) {
            final int auxi = i;
            double temp = expandedMatrix[i][k];
            expandedMatrix[i][k] = expandedMatrix[i][higherColumn];
            expandedMatrix[i][higherColumn] = temp;
            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.MAGENTA,
                    color).setDuration(times.getProgress() * 500);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxHigherColumn)
                            .setBackgroundColor((Integer) animator.getAnimatedValue());

                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxk)
                            .setBackgroundColor((Integer) animator.getAnimatedValue());
                    if (acces && auxi == 0) {
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
                        if (acces && auxi == 0) {
                            EditText textXAux = (EditText) xIndex.getChildAt(auxHigherColumn);
                            String tempAcces = textXAux.getText().toString();
                            EditText textXAux2 = (EditText) xIndex.getChildAt(auxk);
                            textXAux.setText(textXAux2.getText().toString());
                            textXAux2.setText(tempAcces);
                            xIndex.getChildAt(auxHigherColumn).setBackgroundColor(Color.parseColor("#429ffd"));
                            xIndex.getChildAt(auxk).setBackgroundColor(Color.parseColor("#429ffd"));
                        }
                        if (!animations.isEmpty()) animations.remove(0);
                    } catch (Exception e) {
                        table.removeAllViews();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxHigherColumn)
                            .setBackgroundColor(defaultColor);
                    ((TableRow) table.getChildAt(auxi)).getChildAt(auxk)
                            .setBackgroundColor(defaultColor);
                    if (acces) {
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

    void elimination(double[][] expandedMatrix) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void substitution(double[][] expandedMatrix) {
        for (double val : substitution(expandedMatrix, -1)) {
            xValuesText.addView(defaultTextView((val + "            ").substring(0, 6)));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void substitution(double[][] expandedMatrix, int[] marks) {
        double[] result = substitution(expandedMatrix, -1);
        double[] clean = new double[result.length];
        for (int i = 0; i < result.length; i++) {
            double val = result[i];
            clean[marks[i]] = val;
        }
        for (double val : clean) {
            xValuesText.addView(defaultTextView((val + "            ").substring(0, 6)));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private double[] substitution(double[][] expandedMatrix, int usseless) {//regression
        xValuesText.removeAllViews();
        int n = expandedMatrix.length - 1;
        double[] values = new double[n + 1];
        if (expandedMatrix[n][n] == 0) {
            //Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
            styleWrongMessage("Error division 0");
            return values;
        }
        double x = expandedMatrix[n][n + 1] / expandedMatrix[n][n];

        values[values.length - 1] = x;
        for (int i = 0; i < n + 1; i++) {
            double sumatoria = 0;
            int auxi = n - i;
            for (int p = auxi + 1; p < n + 1; p++) {
                sumatoria = sumatoria + expandedMatrix[auxi][p] * values[p];
            }
            if (expandedMatrix[auxi][auxi] == 0) {
                //Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                styleWrongMessage("Error division 0");
                return values;
            }
            values[auxi] = (expandedMatrix[auxi][n + 1] - sumatoria) / expandedMatrix[auxi][auxi];

        }

        return values;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    double[][] totalPivot(int k, double[][] expandedMAtrix, int[] marks) {
        return totalPivot(k, expandedMAtrix, marks, matrixResult);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private double[][] totalPivot(int k, double[][] expandedMAtrix, int[] marks, TableLayout table) {
        return totalPivot(k, expandedMAtrix, marks, table, defaultColor);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private double[][] totalPivot(int k, double[][] expandedMAtrix, int[] marks, TableLayout table, int color) {
        return totalPivot(k, expandedMAtrix, marks, table, color, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private double[][] totalPivot(int k, double[][] expandedMAtrix, int[] marks, TableLayout table, int color, boolean acces) {
        double mayor = 0.0;
        String message;
        int higherRow = k;
        int higherColumn = k;
        for (int r = k; r < expandedMAtrix.length; r++) {
            for (int s = k; s < expandedMAtrix.length; s++) {
                if (Math.abs(expandedMAtrix[r][s]) > mayor) {
                    mayor = Math.abs(expandedMAtrix[r][s]);
                    higherRow = r;
                    higherColumn = s;
                }
            }
        }
        if (mayor == 0) {
            // Toast.makeText(getContext(),  "Error division 0", Toast.LENGTH_SHORT).show();
            message = "Error division 0";
            styleWrongMessage(message);
        } else {
            if (higherRow != k)
                swapRows(k, higherRow, expandedMAtrix, table, color);
            if (higherColumn != k)
                swapColumn(k, higherColumn, expandedMAtrix, marks, table, color, acces);
        }
        return expandedMAtrix;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void securePivot() {

        double[][] expandedMatrix = getMatrix();
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }
        animatorSet = new AnimatorSet();
        animations = new LinkedList<>();
        if (!pivoted) {
            if (expandedMatrix != null) {
                xIndex.removeAllViews();
                for (int i = 0; i < expandedMatrix.length; i++) {
                    xIndex.addView(defaultTextView("X" + (i + 1), Color.parseColor("#429ffd")));
                }
                xIndex.setVisibility(View.VISIBLE);


                int[] marks = new int[expandedMatrix.length];
                for (int i = 0; i < marks.length; i++) {
                    marks[i] = i + 1;
                }
                matrixBackpack = new String[expandedMatrix.length][expandedMatrix.length + 1];
                int n = matrixAText.getChildCount();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        EditText aux = ((EditText) ((TableRow) matrixAText.getChildAt(i)).getChildAt(j));
                        matrixBackpack[i][j] = aux.getText().toString();
                    }
                    EditText aux = ((EditText) bValuesText.getChildAt(i));
                    matrixBackpack[i][n] = aux.getText().toString();
                }

                backMAtrix.setVisibility(View.VISIBLE);
                for (int i = 0; i < matrixAText.getChildCount(); i++) {
                    for (int j = 0; j < matrixAText.getChildCount(); j++) {
                        EditText aux = ((EditText) ((TableRow) matrixAText.getChildAt(i)).getChildAt(j));
                        aux.setKeyListener(null);
                        aux.setInputType(InputType.TYPE_NULL);
                        oneVariable.keyboardUtils.closeInternalKeyboard();
                        aux.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        aux.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {

                            }
                        });
                        aux.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return false;
                            }
                        });
                    }

                    EditText aux = ((EditText) bValuesText.getChildAt(i));
                    aux.setKeyListener(null);
                    aux.setInputType(InputType.TYPE_NULL);
                    aux.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    aux.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {

                        }
                    });
                    aux.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                }
                for (int i = 0; i < expandedMatrix.length; i++) {
                    totalPivot(i, expandedMatrix, marks, matrixAText, defaultColor, true);
                }
                animatorSet.playSequentially(animations);
                animatorSet.start();
                pivoted = true;
            }
        }
    }

    void styleCorrectMessage() {
        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(Objects.requireNonNull(getActivity()), new Style(), Style.TYPE_BUTTON)
                .setIndeterminate(true)
                .setButtonText("UNDO")
                .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                .setProgressBarColor(Color.WHITE)
                .setText("The method converges")
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(Color.rgb(76, 175, 80))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

    void styleWrongMessage(String message) {
        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(Objects.requireNonNull(getActivity()), new Style(), Style.TYPE_BUTTON)
                .setIndeterminate(true)
                .setButtonText("UNDO")
                .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                .setProgressBarColor(Color.WHITE)
                .setText(message)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(Color.rgb(244, 67, 54))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void addStage(double[][] matrix, int stage, Context context, List<String> multipliers) {
        TextView tittle = new TextView(context);
        TableLayout matrixResult = new TableLayout(context);
        tittle.setText("Stage " + (stage + 1));
        for (double[] v : matrix) {
            TableRow aux = new TableRow(context);
            for (double val : v) {
                aux.addView(defaultTextView((String.valueOf(val <= Math.pow(10, -13) ? 0.0 : val) + "         ").substring(0, 6)));
            }
            matrixResult.addView(aux);
        }
        LinearLayout multipliersLayout = new LinearLayout(context);
        multipliersLayout.setOrientation(LinearLayout.VERTICAL);
        for (String m : multipliers) {
            TextView aux = new TextView(context);
            aux.setText(m);

            multipliersLayout.addView(aux);
        }

        LinearLayout matrixAndMultiplier = new LinearLayout(context);
        matrixAndMultiplier.addView(matrixResult);
        TextView space = new TextView(context);
        space.setText("    ");
        matrixAndMultiplier.addView(space);
        matrixAndMultiplier.addView(multipliersLayout);

        contentStages.addView(tittle);
        contentStages.addView(matrixAndMultiplier);
    }

}
