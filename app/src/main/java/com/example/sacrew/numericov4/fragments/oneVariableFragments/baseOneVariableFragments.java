package com.example.sacrew.numericov4.fragments.oneVariableFragments;

import android.app.Activity;
import android.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.sacrew.numericov4.fragments.graphFragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import com.example.sacrew.numericov4.graphUtils;

/**
 * Created by sacrew on 27/05/18.
 */

public class baseOneVariableFragments extends Fragment {
    AutoCompleteTextView textFunction;
    EditText iter, textError;
    Expression function;
    graphUtils graphUtils = new graphUtils();

    public void bootStrap() {
        boolean error = true;
        int ite = 0;
        Double errorValue = 0.0;
        String originalFunc = textFunction.getText().toString();
        this.function = new Expression(functionRevision(originalFunc));
        error = checkSyntax(originalFunc, textFunction);
        if (!graphFragment.allFunctions.contains(originalFunc) && error) {
            graphFragment.allFunctions.add(originalFunc);
            textFunction.setAdapter(new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
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

    public String convertirCientifica(double val) {
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("#.##E0");
        return num.format(val);
    }

    public String convertirNormal(double val) {
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("#.##");
        return num.format(val);
    }

    public void execute(boolean error, double errorValue, int ite) {

    }

    public String functionRevision(String function) {
        return graphUtils.functionRevision(function);
    }

    public void graphSerie(double start, double end, String funcitonExpr, GraphView graph, int color) {
        graphUtils.graphSerie(start, end, funcitonExpr, graph, color);
    }

    public void graphPoint(double x, double y, PointsGraphSeries.Shape figure, GraphView graph, final Activity activity,
                           String color, boolean listener) {
        graphUtils.graphPoint(x, y, figure, graph, activity, color, listener);
    }

    public boolean checkSyntax(String function, EditText text) {
        try {
            new Expression(functionRevision(function)).with("x", "0").eval();
        } catch (NumberFormatException e) {
            System.out.println("number");
            return true;
        } catch (Expression.ExpressionException e) {
            System.out.println("expression exception" + e.getMessage());
            text.setError("Invalid function");

            return false;
        }

        return true;
    }
}
