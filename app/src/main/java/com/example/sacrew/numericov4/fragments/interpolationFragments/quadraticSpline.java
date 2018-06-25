package com.example.sacrew.numericov4.fragments.interpolationFragments;


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

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpQuadraticSpline;
import com.example.sacrew.numericov4.utils.systemEquationsUtils;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Objects;

import static com.example.sacrew.numericov4.fragments.homeFragment.poolColors;


/**
 * A simple {@link Fragment} subclass.
 */
public class quadraticSpline extends baseSpliners {
    private final systemEquationsUtils systemUtils = new systemEquationsUtils();

    public quadraticSpline() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuadratic_spline, container, false);
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

                    b.putString("key", function); //Your id
                    i.putExtras(b); //Put your id to your next Intent
                    startActivity(i);
                }
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpQuadraticSpline.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void execute() {
        boolean errorValues = bootStrap();

        if (errorValues) {
            createInequality();
            boolean checkError = quadraticSplineMethod();
            if (checkError) {
                calc = true;
                updateGraph();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean quadraticSplineMethod() {
        function = "";
        double[][] supermatrix = new double[3 * inequality.length][3 * inequality.length + 1];

        int n = supermatrix.length;
        int j = 0;
        int z = 0;

        function += "$${x^2a_{n} + xb_{n} + c_{n}}$$";

        //normal
        for (int i = 0; i < inequality.length; i++) {
            Pair<Pair<Double, Double>, Pair<Double, Double>> aux = inequality[i];
            supermatrix[j][z] = Math.pow(aux.first.first, 2);
            supermatrix[j][z + 1] = aux.first.first;
            supermatrix[j][z + 2] = 1;
            supermatrix[j][n] = aux.first.second;

            String auxj = String.valueOf(z - j);
            String equation = supermatrix[j][z] + "a_{" + auxj + "}+" + aux.first.first + "b_{" + auxj + "}+c_{" + auxj + "} = " + aux.first.second;

            function += "$${" + equation + " \\qquad with \\enspace (" + aux.first.first + "," + aux.first.second + ")\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
            supermatrix[j + 1][z] = Math.pow(aux.second.first, 2);
            supermatrix[j + 1][z + 1] = aux.second.first;
            supermatrix[j + 1][z + 2] = 1;
            supermatrix[j + 1][n] = aux.second.second;
            equation = supermatrix[j + 1][z] + "a_{" + auxj + "}+" + aux.second.first + "b_{" + auxj + "}+c_{" + auxj + "} = " + aux.second.second;
            function += "$${" + equation + " \\qquad with \\enspace (" + aux.second.first + "," + aux.second.second + ")\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
            z += 3;
            j += 2;
        }
        //first derivative
        int k = j;
        z = 0;

        function += "$${\\text{first derivative}}$$ $${2xa_{n} + b_{n} = 2xa_{n+1} + b_{n+1}}$$";
        for (int i = 0; i < inequality.length - 1; i++) {
            Pair<Pair<Double, Double>, Pair<Double, Double>> aux = inequality[i];
            Pair<Pair<Double, Double>, Pair<Double, Double>> aux2 = inequality[i + 1];
            supermatrix[k][z] = 2 * aux.second.first;
            supermatrix[k][z + 1] = 1;
            supermatrix[k][z + 3] = -2 * aux2.first.first;
            supermatrix[k][z + 4] = -1;
            supermatrix[k][n] = 0;

            String equation = supermatrix[k][z] + "a_{" + i + "}+b_{" + i + "}";
            String equation2 = 2 * aux2.first.first + "a_{" + (i + 1) + "}+b_{" + (i + 1) + "}";
            function += "$${" + equation + " = " + equation2 + "\\qquad with \\enspace x = " + aux.first.first + " \\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
            k += 1;
            z += 3;
            //suponemos que la segunda derivada es 0 entonces a1 = 0
        }
        function += "$${\\text{second derivative}}$$ $${a_{1} = 0\\qquad \\qquad}$$";
        supermatrix[k][0] = 1;
        double[] result = systemUtils.manager(supermatrix);
        if (result == null) {
            styleWrongMessage();
            return false;
        }
        function += "$${result}$$";
        j = 0;
        equations = new LinkedList<>();
        function += "$${p(x) = \\begin{cases}";
        StringWriter stw;
        ExprEvaluator util = new ExprEvaluator();
        EvalEngine engine = new EvalEngine(false);
        TeXUtilities texUtil = new TeXUtilities(engine, false);
        for (int i = 0; i < inequality.length; i++) {

            String auxFunc = result[j] + "*(x^2)+" + result[j + 1] + "*x+" + result[j + 2];
            String auxToLatex = roundOff(result[j]) + "*(x^2)+" + roundOff(result[j + 1]) + "*x+" + roundOff(result[j + 2]);
            IExpr funcSimplify = util.evaluate(F.ExpandAll(util.evaluate(auxFunc)));
            if (Build.VERSION.SDK_INT > 19) {
                funcSimplify = util.evaluate(F.FullSimplify(funcSimplify));
            }
            int color = poolColors.remove(0);
            poolColors.add(color);
            Pair<Pair<Double, Double>, Pair<Double, Double>> aux = inequality[i];
            System.out.println("iter  " + i + " values " + new Pair<>(funcSimplify, new Pair<>(color, new Pair<>(aux.first.first, aux.second.first))));
            equations.add(new Pair<>(funcSimplify.toString(), new Pair<>(color, new Pair<>(aux.first.first, aux.second.first))));

            stw = new StringWriter();
            IExpr outToLatex = util.evaluate(F.ExpandAll(util.evaluate(auxToLatex)));
            if (Build.VERSION.SDK_INT > 19) {
                outToLatex = util.evaluate(F.FullSimplify(outToLatex));
            }
            texUtil.toTeX(outToLatex, stw);
            function += stw.toString() + " & " + aux.first.first + " \\leq " + aux.second.first;
            if (i < inequality.length - 1) function += "\\\\";
            j += 3;
        }
        function += "\\end{cases}\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";

        return true;
    }

}

