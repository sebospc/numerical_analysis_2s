package com.example.sacrew.numericov4.fragments.interpolationFragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class lagrange extends baseInterpolationMethods{

    private List<String> functions;
    public lagrange() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_lagrange, container, false);
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
                if(calc) {
                    Intent i = new Intent(getContext(), mathExpressions.class);
                    Bundle b = new Bundle();

                    b.putString("key",function); //Your id
                    i.putExtras(b); //Put your id to your next Intent
                    startActivity(i);
                }
            }
        });
        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpLagrange.class);
        startActivity(i);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(){
        //header of mathView
        function= "$${ L_{k}(x) =  \\prod_{i = 0,i \\neq k}^n \\frac{(x - x_{i})}{(x_{k} - x_{i})} }$$";
        //check if all values are ok
        boolean errorValues = bootStrap();

        if(errorValues) {
            //check is division by zero ocurred
            boolean errorDivision = lagrangeMethod();
            if(errorDivision) {
                EvalEngine engine = new EvalEngine(false);
                ExprEvaluator util = new ExprEvaluator();

                IExpr temp = util.evaluate("0");
                StringWriter stw;
                TeXUtilities texUtil = new TeXUtilities(engine, false);
                //creating and simplfiying numerator and denominators
                for (int i = 0; i < functions.size(); i++) {

                    IExpr functionLSimplified = util.evaluate(F.FullSimplify(util.evaluate(functions.get(i))));
                    stw = new StringWriter();
                    texUtil.toTeX(functionLSimplified, stw);
                    //creating each L function
                    String aux = " $${L_{" + i + "}(x) = " + stw + "\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";
                    function = function + aux;

                    temp = F.Plus(temp, F.Times(util.evaluate("" + fxn[i]), functionLSimplified));

                }

                stw = new StringWriter();
                //simpliying p function
                IExpr expanded = util.evaluate(F.ExpandAll(temp));
                IExpr simplifiedPFunction = util.evaluate(F.FullSimplify(expanded));
                //creating latex p function
                texUtil.toTeX(simplifiedPFunction, stw);

                function = function+ "$${ p(x) =  \\sum_{k = 0}^n L_{k}(x)f(x_{k})}$$";
                function = function + " $${p(x) = " + stw + "\\qquad \\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad\\qquad}$$";

                updateGraph(simplifiedPFunction.toString(), getContext(), (int) Math.ceil(((xn[xn.length - 1] - xn[0]) * 10) + 20));
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean lagrangeMethod(){
        functions = new LinkedList<>();
        ExprEvaluator util = new ExprEvaluator();
        for(int i = 0; i < xn.length; i++){
            StringBuilder numerator = new StringBuilder("1");
            StringBuilder denominator = new StringBuilder("1");
            for(int j = 0; j < xn.length; j++){
                if(i!=j){
                    numerator.append("*").append("(x-(").append(xn[j]).append("))");
                    denominator.append("*(").append(xn[i]).append("-(").append(xn[j]).append("))");
                }
            }
            if ((util.evaluate(denominator.toString())).toString().equals("0.0")){
                Toast.makeText(getContext(), "Error division by 0", Toast.LENGTH_SHORT).show();
                return false;
            }
            String aux = "("+numerator+"/("+denominator+"))";
            functions.add(aux);
        }
        calc = true;
        return true;
    }


}
