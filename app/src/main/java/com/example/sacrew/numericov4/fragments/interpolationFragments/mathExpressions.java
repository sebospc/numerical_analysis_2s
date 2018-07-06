package com.example.sacrew.numericov4.fragments.interpolationFragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;

import org.matheclipse.parser.client.eval.DoubleEvaluator;
import org.matheclipse.parser.client.eval.DoubleVariable;
import org.matheclipse.parser.client.eval.IDoubleValue;


import java.util.List;
import java.util.Objects;

import katex.hourglass.in.mathlib.MathView;


public class mathExpressions extends AppCompatActivity {
    private MathView mathView;
    public static List<Pair<String, Pair<Integer, Pair<Double, Double>>>> equations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_expressions);
        mathView = findViewById(R.id.math_view);
        Bundle b = getIntent().getExtras();
        IDoubleValue vd = new DoubleVariable(3.0);
        DoubleEvaluator engine = new DoubleEvaluator();
        engine.defineVariable("x", vd);
        if (b != null) {

            setText(Objects.requireNonNull(b.getString("key")));
            if(b.getString("function") != null)
                engine.evaluate(b.getString("function"));
        }
        EditText xValue = findViewById(R.id.xValue);
        Button evaluateInFunction = findViewById(R.id.evaluateInFunction);
        TextView showValue = findViewById(R.id.resultFxn);
        if(equations == null) {
            evaluateInFunction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String x = xValue.getText().toString();
                    double value;
                    try{
                        value = Double.parseDouble(x);
                    }catch (Exception e){
                        xValue.setError("invalid value");
                        return;
                    }
                    vd.setValue(value);

                    showValue.setText("f(" + x + ") = " + engine.evaluate() );
                }
            });
        }else{
            evaluateInFunction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String x = xValue.getText().toString();
                    double value;
                    try{
                        value = Double.parseDouble(x);
                    }catch (Exception e){
                        xValue.setError("invalid value");
                        return;
                    }

                    String output = null;
                    for(int i = 0; i < equations.size(); i++){
                        Pair<String, Pair<Integer, Pair<Double, Double>>> aux =  equations.get(i);
                        if(value > aux.second.second.first && value <= aux.second.second.second ){
                            engine.evaluate(aux.first);
                            vd.setValue(value);
                            output = "f(" + x + ") = " + engine.evaluate();
                        }
                    }
                    if(output != null)
                        showValue.setText(output);
                    else
                        xValue.setError("invalid value");


                }
            });
        }

    }

    private void setText(String expression) {
        mathView.getSettings().setUseWideViewPort(true);
        mathView.getSettings().setLoadWithOverviewMode(true);
        if (expression.length() < 30)
            mathView.setTextSize(100);
        else if (expression.length() < 80)
            mathView.setTextSize(80);
        else mathView.setTextSize(60);
        mathView.setDisplayText(expression);
        mathView.getSettings().setJavaScriptEnabled(true);
        mathView.getSettings().setBuiltInZoomControls(true);
        mathView.getSettings().setDisplayZoomControls(true);
    }
}
