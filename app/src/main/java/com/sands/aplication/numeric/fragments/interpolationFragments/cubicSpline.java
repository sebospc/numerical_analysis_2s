package com.sands.aplication.numeric.fragments.interpolationFragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sands.aplication.numeric.R;
import com.sands.aplication.numeric.fragments.customPopUps.popUpCubicSpline;
import com.sands.aplication.numeric.utils.systemEquationsUtils;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Objects;

import static com.sands.aplication.numeric.fragments.homeFragment.poolColors;


/**
 * A simple {@link Fragment} subclass.
 */
public class cubicSpline extends baseSpliners {
    private final systemEquationsUtils systemUtils = new systemEquationsUtils();
    private String resultText;

    public cubicSpline() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cubic_spline, container, false);
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
        Button runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
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

                    b.putString("key", resultText); //Your id
                    b.putString("stages", latexText);
                    i.putExtras(b); //Put your id to your next Intent
                    mathExpressions.equations = equations;
                    startActivity(i);
                }
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpCubicSpline.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void execute() {
        boolean errorValues = bootStrap();

        if (errorValues) {
            createInequality();
            boolean checkError = cubicSplineMethod();
            if (checkError) {
                calc = true;
                updateGraph();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean cubicSplineMethod() {
        latexText = "";
        double[][] superMatrix = new double[4 * inequality.length][4 * inequality.length + 1];
        int n = superMatrix.length;
        int j = 0;
        int z = 0;
        int l = 0;
        //normal
        latexText += "$${a_{n}x^3 + b_{n}x^2 + c_{n}x + d_{n}}$$";
        for (int i = 0; i < inequality.length; i++) {
            Pair<Pair<Double, Double>, Pair<Double, Double>> aux = inequality[i];
            int auxj = z - j - l;

            superMatrix[j][z] = Math.pow(aux.first.first, 3);
            superMatrix[j][z + 1] = Math.pow(aux.first.first, 2);
            superMatrix[j][z + 2] = aux.first.first;
            superMatrix[j][z + 3] = 1;
            superMatrix[j][n] = aux.first.second;

            String equation = superMatrix[j][z] + "a_{" + auxj + "} + " +
                    superMatrix[j][z + 1] + "b_{" + auxj + "}+" + aux.first.first + "c_{" + auxj +
                    "}+ d_{" + auxj + "} = " + aux.first.second;
            latexText += "$${" + equation + " \\qquad with \\enspace (" + aux.first.first + "," + aux.first.second + ")\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";

            superMatrix[j + 1][z] = Math.pow(aux.second.first, 3);
            superMatrix[j + 1][z + 1] = Math.pow(aux.second.first, 2);
            superMatrix[j + 1][z + 2] = aux.second.first;
            superMatrix[j + 1][z + 3] = 1;
            superMatrix[j + 1][n] = aux.second.second;
            equation = superMatrix[j + 1][z] + "a_{" + auxj + "}" + " + " + superMatrix[j + 1][z + 1] + "b_{" + auxj +
                    "} + " + aux.second.first + "c_{" + auxj + "} + d_{" + auxj + "} = " + aux.second.second;
            latexText += "$${" + equation + " \\qquad with \\enspace (" + aux.second.first + "," + aux.second.second + ")\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
            z += 4;
            j += 2;
            l += 1;
        }
        int k = j;
        z = 0;
        latexText += "$${\\textbf{First Derivative}}$$ $${\\qquad 3x^2a_{n} + 2xb_{n} + c_{n} = 3x^2 a_{n+1}+ 2xb_{n+1} + c_{n+1}}$$";
        //primera derivada

        for (int i = 0; i < inequality.length - 1; i++) {
            Pair<Pair<Double, Double>, Pair<Double, Double>> aux = inequality[i];

            superMatrix[k][z] = 3 * Math.pow(aux.second.first, 2);
            superMatrix[k][z + 1] = 2 * aux.second.first;
            superMatrix[k][z + 2] = 1;
            superMatrix[k][z + 4] = -3 * Math.pow(aux.second.first, 2);
            superMatrix[k][z + 5] = -2 * aux.second.first;
            superMatrix[k][z + 6] = -1;
            superMatrix[k][n] = 0;
            String equation = superMatrix[k][z] + "a_{" + i + "}" + " + " + superMatrix[k][z + 1] +
                    "b_{" + (i) + "} + c_{" + (i) + "} =" + superMatrix[k][z] + "a_{" + (i + 1) + "}+" + superMatrix[k][z + 1] + "b_{" + (i + 1) + "} + c_{" + (i + 1) + "}";
            latexText += "$${" + equation + " \\qquad with \\enspace x = " + aux.second.first + "\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
            k += 1;
            z += 4;
        }
        z = 0;
        // segunda derivada
        latexText += "$${\\textbf{Second Derivative}}$$ $${\\qquad 6xa_{n} + 2b_{n} = 6xa_{n+1} + 2b_{n+1}}$$ ";
        for (int i = 0; i < inequality.length - 1; i++) {
            Pair<Pair<Double, Double>, Pair<Double, Double>> aux = inequality[i];

            superMatrix[k][z] = 6 * aux.second.first;
            superMatrix[k][z + 1] = 2;
            superMatrix[k][z + 4] = -6 * aux.second.first;
            superMatrix[k][z + 5] = -2;
            superMatrix[k][n] = 0;
            String equation = superMatrix[k][z] + "a_{" + i + "} + 2b_{" + i + "}=" +
                    superMatrix[k][z] + "a_{" + (i + 1) + "}+" + "2b_{" + (i + 1) + "}";
            latexText += "$${" + equation + " \\qquad with \\enspace x = " + aux.first.first + "\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
            k += 1;
            z += 4;

        }
        latexText += "$$\\textbf{Supposition}$$";
        Pair<Pair<Double, Double>, Pair<Double, Double>> aux = inequality[0];
        aux = inequality[0];
        String equation = 6 * aux.first.first + "a_{0}+2b_{0} = 0";
        latexText += "$${" + equation + " \\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
        superMatrix[k][0] = 6 * aux.first.first;
        superMatrix[k][1] = 2;
        aux = inequality[inequality.length - 1];
        superMatrix[k + 1][4 * (inequality.length - 1)] = 6 * aux.second.first;
        superMatrix[k + 1][4 * (inequality.length - 1) + 1] = 2;
        equation = superMatrix[k + 1][4 * (inequality.length - 1)] + "a_{" + (inequality.length - 1) + "}+2b_{" + (inequality.length - 1) + "} = 0";
        latexText += "$${" + equation + " \\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";

        double[] result = systemUtils.manager(superMatrix);
        if (result == null) {
            styleWrongMessage();
            return false;
        }
        resultText = "";
        resultText += "$$\\textbf{Result}$$";
        equations = new LinkedList<>();
        resultText += "$${p(x) = \\begin{cases}";
        StringWriter stw;
        ExprEvaluator util = new ExprEvaluator();
        EvalEngine engine = new EvalEngine(false);
        TeXUtilities texUtil = new TeXUtilities(engine, false);
        j = 0;
        int color = poolColors.remove(0);
        poolColors.add(color);
        for (int i = 0; i < inequality.length; i++) {

            String auxFunc = result[j] + "*(x^3)+" + result[j + 1] + "*x^2+" + result[j + 2] + "*x+" + result[j + 3];
            String auxFuncToLatex = roundOff(result[j]) + "*(x^3)+" + roundOff(result[j + 1]) + "*x^2+" + roundOff(result[j + 2]) + "*x+" + roundOff(result[j + 3]);
            IExpr funcSimplify = util.evaluate(F.ExpandAll(util.evaluate(auxFunc)));
            if (Build.VERSION.SDK_INT > 19) {
                funcSimplify = util.evaluate(F.FullSimplify(funcSimplify));
            }

            aux = inequality[i];
            equations.add(new Pair<>(funcSimplify.toString(), new Pair<>(color, new Pair<>(aux.first.first, aux.second.first))));
            stw = new StringWriter();
            IExpr outToLatex = util.evaluate(F.ExpandAll(util.evaluate(auxFuncToLatex)));
            if (Build.VERSION.SDK_INT > 19) {
                outToLatex = util.evaluate(F.FullSimplify(outToLatex));
            }
            texUtil.toTeX(outToLatex, stw);
            resultText += stw.toString() + " & " + aux.first.first + " \\lt x \\leq " + aux.second.first;
            if (i < inequality.length - 1) resultText += "\\\\";
            j += 4;
        }
        resultText += "\\end{cases}\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
        return true;
    }


}
