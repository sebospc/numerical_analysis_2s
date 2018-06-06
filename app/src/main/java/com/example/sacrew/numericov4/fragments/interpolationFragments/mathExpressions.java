package com.example.sacrew.numericov4.fragments.interpolationFragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sacrew.numericov4.R;

import katex.hourglass.in.mathlib.MathView;


public class mathExpressions extends AppCompatActivity {
    private static MathView mathView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_expressions);
        mathView = findViewById(R.id.math_view);
        Bundle b = getIntent().getExtras();
        String value = ""; // or other values
        if(b != null) {
            value = b.getString("key");
            setFunction(value);
        }
    }

    public void setFunction(String function){
        mathView.getSettings().setUseWideViewPort(true);
        mathView.getSettings().setUseWideViewPort(true);
        mathView.setVerticalScrollBarEnabled(true);
        mathView.setHorizontalScrollBarEnabled(true);
        mathView.setDisplayText("$${"+function+function+"}$$");
        //app:text="$${- x+x\\,\\left\( -1.0+x\\right\)}$$"
        /*System.out.println("function "+function);
        String expr = "$${"+function.replace("\\","\\\\")+"}$$";
        expr = expr.replace("(","\\(");
        expr = expr.replace(")","\\)");
        System.out.println(expr);
        outFunctions.setText(expr);
        outFunctions.setEngine(MathView.Engine.MATHJAX);*/

    }
}
