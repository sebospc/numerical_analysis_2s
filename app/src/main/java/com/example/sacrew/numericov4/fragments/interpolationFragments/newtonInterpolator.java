package com.example.sacrew.numericov4.fragments.interpolationFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpNewtonDifferences;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;


import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class newtonInterpolator extends baseInterpolationMethods {

    private List<double []> derivs;
    String mensaje = "";
    private boolean errorDivision = true;

    public newtonInterpolator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                if(calc) {
                    Intent i = new Intent(getContext(), mathExpressions.class);
                    Bundle b = new Bundle();

                    b.putString("key","$${"+function+"\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$"); //Your id
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
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpNewtonDifferences.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute() {
        boolean errorValues = bootStrap();
        if(errorValues) {
            derivs = new LinkedList<>();
            newtonInterpolation(fxn, 0, 1, new double[fxn.length - 1]);
            if (errorDivision) {

                StringBuilder uglyFunction = new StringBuilder(String.valueOf(fxn[0]));
                StringBuilder auxToLAtexFunc = new StringBuilder(String.valueOf(roundOff(fxn[0])));
                StringBuilder prev = new StringBuilder("");
                StringBuilder auxPrev = new StringBuilder("");
                for (int i = 1; i < derivs.size(); i++) {
                    prev.append("(x-(").append(String.valueOf(xn[i - 1])).append("))");
                    auxPrev.append("(x-(").append(String.valueOf(roundOff(xn[i - 1]))).append("))");
                    uglyFunction.append("+").append(String.valueOf(derivs.get(i)[0])).append(prev);
                    auxToLAtexFunc.append("+").append(roundOff(derivs.get(i)[0])).append(auxPrev);
                }
                ExprEvaluator util = new ExprEvaluator();

                //to latex
                EvalEngine engine = new EvalEngine(false);


                TeXUtilities texUtil = new TeXUtilities(engine, false);
                StringWriter stw = new StringWriter();
                texUtil.toTeX(engine.evaluate(F.Simplify(util.evaluate(auxToLAtexFunc.toString()))), stw);
                //add new expression type latex
                function = stw.toString();

                //update graph
                updateGraph(engine.evaluate(F.Simplify(util.evaluate(uglyFunction.toString()))).toString(), getContext(), (int) Math.ceil(((xn[xn.length - 1] - xn[0]) * 10) + 20));
                //variable to open equations
                calc = true;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void newtonInterpolation(double[] actualValues, int difference, int i, double[] auxFxn) {
        System.out.println("actual values "+actualValues.length+" i: "+i);
        if (actualValues.length == 1) {
            derivs.add(actualValues);
        } else if (i == actualValues.length) {
            System.out.println("STOP auxFxn: "+auxFxn.length);
            derivs.add(actualValues);
            System.out.println("new length " + (auxFxn.length-1));
            newtonInterpolation(auxFxn, difference + 1, 1, new double[auxFxn.length-1]);

        } else {
            double denominator = (xn[i + difference] - xn[i - 1]);
            if (denominator == 0) {
                //Toast.makeText(getContext(), "Error division by 0", Toast.LENGTH_SHORT).show();
                mensaje = "Error division by 0";
                styleWrongMessage(mensaje);
                errorDivision = false;
            }
            double newFxn = (actualValues[i] - actualValues[i - 1]) / denominator;
            auxFxn[i-1] = newFxn;
            newtonInterpolation(actualValues, difference, i + 1, auxFxn);
        }
    }


}
