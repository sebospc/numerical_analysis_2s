package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.fragments.MainActivityTable;
import com.example.sacrew.numericov4.fragments.tableview.TableViewModel;
import com.example.sacrew.numericov4.utils.FunctionStorage;
import com.example.sacrew.numericov4.utils.graphUtils;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import java.io.File;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.sacrew.numericov4.fragments.oneVariable.graphOneVariable;
import static com.example.sacrew.numericov4.fragments.oneVariable.keyboardUtils;

/**
 * Created by sacrew on 27/05/18.
 */

public abstract class baseOneVariableFragments extends Fragment {
    private static List<PointsGraphSeries<DataPoint>> points;
    private static List<LineGraphSeries<DataPoint>> series;
    private final graphUtils graphUtils = new graphUtils();
    private final SuperActivityToast.OnButtonClickListener onButtonClickListener =
            new SuperActivityToast.OnButtonClickListener() {

                @Override
                public void onClick(View view, Parcelable token) {
                    SuperActivityToast.cancelAllSuperToasts();
                }
            };
    public EditText textFunction;
    public EditText iter, textError;
    public ToggleButton errorToggle;
    public FunctionStorage functionStorage;
    public File temp;
    Expression function;
    boolean calc = false;
    List<List<String>> completeList = new LinkedList<>();
    List<String> listValuesTitles = new LinkedList<>();

    void bootStrap() {
        boolean error;
        int ite = 0;
        Double errorValue = 0.0;
        String originalFunc = textFunction.getText().toString();
        this.function = new Expression(functionRevision(originalFunc));
        error = checkSyntax(originalFunc, textFunction);
        if (error) {
            updateMyFunctions(originalFunc);

        }
        try {
            errorValue = new Expression(textError.getText().toString()).eval().doubleValue();
        } catch (Exception e) {
            textError.setError("Invalid error value");
            error = false;
        }

        try {
            ite = Integer.parseInt(iter.getText().toString());
        } catch (Exception e) {
            iter.setError("Wrong iterations");
            error = false;
        }

        execute(error, errorValue, ite);
    }

    String cientificTransformation(double val) {
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("#.##E0");
        return num.format(val);
    }

    String normalTransformation(double val) {
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("0.00");
        return num.format(val);
    }


    void execute(boolean error, double errorValue, int ite) {

    }

    String functionRevision(String function) {
        return graphUtils.functionRevision(function);
    }

    boolean checkSyntax(String function, EditText text) {
        try {
            new Expression(functionRevision(function)).with("x", "0").eval();
        } catch (Expression.ExpressionException e) {
            text.setError("Invalid function");
            return false;
        } catch (Exception e) {
            return true;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void executeChart(Context context) {
        if (calc) {
            Intent i = new Intent(context, MainActivityTable.class);
            startActivity(i);
            TableViewModel.getTitles(listValuesTitles);
            TableViewModel.getCeldas(completeList);
        }
    }

    void graphSerie(String function, double y, int color) {
        series = graphUtils.graphPharallel((int) (Math.abs(y - (double) 0) / 0.1), function, color);
        for (LineGraphSeries<DataPoint> v : series) graphOneVariable.addSeries(v);
    }

    void graphPoint(double x, double y, int color) {

        PointsGraphSeries<DataPoint> point = graphUtils.graphPoint(x, y, PointsGraphSeries.Shape.POINT, color, false);
        graphOneVariable.addSeries(point);
        points.add(point);
    }

    void cleanGraph() {
        if (series != null)
            for (LineGraphSeries<DataPoint> v : series) graphOneVariable.removeSeries(v);
        if (points != null)
            for (PointsGraphSeries<DataPoint> v : points) graphOneVariable.removeSeries(v);
        points = new LinkedList<>();
        series = new LinkedList<>();
    }


    void styleCorrectMessage(String message) {
        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(Objects.requireNonNull(getActivity()), new Style(), Style.TYPE_BUTTON)
                .setIndeterminate(true)
                .setButtonText("UNDO")
                .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                .setProgressBarColor(Color.WHITE)
                .setText(message)
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

    void registerEditText(EditText edit, Context context, Activity activity) {
        keyboardUtils.registerEdittext(edit, context, activity);
    }

    void updateMyFunctions(String function) {
        if (!functionStorage.functions.contains(function)) {
            functionStorage.functions.add(function);
            keyboardUtils.addFunction(function, getContext(), functionStorage, temp);
            functionStorage.updateStorage(temp);
        }
    }
}
