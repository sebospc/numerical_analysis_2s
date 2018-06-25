package com.example.sacrew.numericov4.fragments.interpolationFragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpNewtonDifferences;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class newtonInterpolator extends baseInterpolationMethods {

    private List<double[]> derivs;
    private boolean errorDivision = true;

    public newtonInterpolator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newton_interpolator, container, false);
        Button runHelp = view.findViewById(R.id.runHelp);
        Button run = view.findViewById(R.id.run);
        run.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                calc = false;
                cleanGraph();
                execute();

            }
        });
        Button showEquations = view.findViewById(R.id.showEquations);
        showEquations.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (calc) {
                    Intent i = new Intent(getContext(), mathExpressions.class);
                    Bundle b = new Bundle();

                    b.putString("key", "$${" + function + "\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$"); //Your id
                    i.putExtras(b); //Put your id to your next Intent
                    startActivity(i);
                }
            }
        });
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpNewtonDifferences.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void execute() {
        boolean errorValues = bootStrap();
        if (errorValues) {
            derivs = new LinkedList<>();
            newtonInterpolation(fxn, 0, 1, new double[fxn.length - 1]);
            if (errorDivision) {
                StringBuilder auxToLAtexFunc = new StringBuilder(String.valueOf(roundOff(fxn[0])));
                StringBuilder auxPrev = new StringBuilder("");
                for (int i = 1; i < derivs.size(); i++) {
                    auxPrev.append("(x-(").append(String.valueOf(roundOff(xn[i - 1]))).append("))");
                    auxToLAtexFunc.append("+").append(roundOff(derivs.get(i)[0])).append(auxPrev);
                }
                ExprEvaluator util = new ExprEvaluator();

                //to latex
                EvalEngine engine = new EvalEngine(false);


                TeXUtilities texUtil = new TeXUtilities(engine, false);
                StringWriter stw = new StringWriter();
                IExpr optional = util.evaluate(auxToLAtexFunc.toString());
                IExpr fg = optional;
                texUtil.toTeX(fg, stw);
                fg = util.evaluate(F.ExpandAll(optional));
                System.out.println("ddffggtt " + fg.toString());
                stw = new StringWriter();
                texUtil.toTeX(fg, stw);
                if (Build.VERSION.SDK_INT > 19) {
                    fg = util.evaluate(F.FullSimplify(optional));
                    stw = new StringWriter();
                    texUtil.toTeX(fg, stw);
                }

                System.out.println(fg.toString());

                //add new expression type latex
                function = stw.toString();

                //update graph
                updateGraph(fg.toString(), getContext(), (int) Math.ceil(((xn[xn.length - 1] - xn[0]) * 10) + 20));
                //variable to open equations
                calc = true;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void newtonInterpolation(double[] actualValues, int difference, int i, double[] auxFxn) {
        System.out.println("actual values " + actualValues.length + " i: " + i);
        if (actualValues.length == 1) {
            derivs.add(actualValues);
        } else if (i == actualValues.length) {
            System.out.println("STOP auxFxn: " + auxFxn.length);
            derivs.add(actualValues);
            System.out.println("new length " + (auxFxn.length - 1));
            newtonInterpolation(auxFxn, difference + 1, 1, new double[auxFxn.length - 1]);

        } else {
            double denominator = (xn[i + difference] - xn[i - 1]);
            if (denominator == 0) {
                styleWrongMessage();
                errorDivision = false;
            }
            double newFxn = (actualValues[i] - actualValues[i - 1]) / denominator;
            auxFxn[i - 1] = newFxn;
            newtonInterpolation(actualValues, difference, i + 1, auxFxn);
        }
    }


}
