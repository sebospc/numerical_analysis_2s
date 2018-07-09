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
import com.example.sacrew.numericov4.fragments.customPopUps.popUpLagrange;

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
public class lagrange extends baseInterpolationMethods {

    private List<String> functions;

    public lagrange() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lagrange, container, false);
        Button runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
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

                    b.putString("key", latexText); //Your id
                    b.putString("function", function);
                    i.putExtras(b); //Put your id to your next Intent
                    startActivity(i);
                }
            }
        });
        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpLagrange.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void execute() {
        //header of mathView
        latexText = "$${ L_{k}(x) =  \\prod_{i = 0,i \\neq k}^n \\frac{(x - x_{i})}{(x_{k} - x_{i})} }$$";
        //check if all values are ok
        boolean errorValues = bootStrap();

        if (errorValues) {
            //check is division by zero ocurred
            boolean errorDivision = lagrangeMethod();
            if (errorDivision) {
                EvalEngine engine = new EvalEngine(false);
                ExprEvaluator util = new ExprEvaluator();

                IExpr temp = util.evaluate("0");
                StringWriter stw;
                TeXUtilities texUtil = new TeXUtilities(engine, false);
                //creating and simplfiying numerator and denominators
                for (int i = 0; i < functions.size(); i++) {

                    IExpr functionLSimplified = util.evaluate(functions.get(i));
                    if (Build.VERSION.SDK_INT > 19) {
                        functionLSimplified = util.evaluate(F.FullSimplify(functionLSimplified));
                    }

                    stw = new StringWriter();
                    texUtil.toTeX(functionLSimplified, stw);
                    //creating each L latexText
                    String aux = " $${L_{" + i + "}(x) = " + stw + "\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
                    latexText = latexText + aux;

                    temp = F.Plus(temp, F.Times(util.evaluate("" + fxn[i]), functionLSimplified));

                }

                stw = new StringWriter();
                //simpliying p latexText
                IExpr simplifiedPFunction = util.evaluate(F.ExpandAll(temp));
                if (Build.VERSION.SDK_INT > 19) {
                    simplifiedPFunction = util.evaluate(F.FullSimplify(simplifiedPFunction));
                }

                //creating latex p latexText
                function = simplifiedPFunction.toString();
                texUtil.toTeX(simplifiedPFunction, stw);

                latexText = latexText + "$${ p(x) =  \\sum_{k = 0}^n L_{k}(x)f(x_{k})}$$";
                latexText = latexText + " $${p(x) = " + stw + "\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";

                updateGraph(simplifiedPFunction.toString(), getContext(), (int) Math.ceil((Math.abs(xn[xn.length - 1] - xn[0]) * 10) + 10));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean lagrangeMethod() {
        functions = new LinkedList<>();
        ExprEvaluator util = new ExprEvaluator();
        for (int i = 0; i < xn.length; i++) {
            StringBuilder numerator = new StringBuilder("1");
            StringBuilder denominator = new StringBuilder("1");
            for (int j = 0; j < xn.length; j++) {
                if (i != j) {
                    numerator.append("*").append("(x-(").append(xn[j]).append("))");
                    denominator.append("*(").append(xn[i]).append("-(").append(xn[j]).append("))");
                }
            }
            if ((util.evaluate(denominator.toString())).toString().equals("0.0")) {
                styleWrongMessage();
                return false;
            }
            String aux = "(" + numerator + "/(" + denominator + "))";
            functions.add(aux);
        }
        calc = true;
        return true;
    }


}
