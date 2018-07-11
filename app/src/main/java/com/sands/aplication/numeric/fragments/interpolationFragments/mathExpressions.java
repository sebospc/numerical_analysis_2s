package com.sands.aplication.numeric.fragments.interpolationFragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sands.aplication.numeric.R;

import org.matheclipse.parser.client.eval.DoubleEvaluator;
import org.matheclipse.parser.client.eval.DoubleVariable;
import org.matheclipse.parser.client.eval.IDoubleValue;

import java.util.List;
import java.util.Objects;

import katex.hourglass.in.mathlib.MathView;


public class mathExpressions extends AppCompatActivity {
    public static List<Pair<String, Pair<Integer, Pair<Double, Double>>>> equations;
    private MathView stages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_expressions);
        MathView mathView = findViewById(R.id.math_view);
        Bundle b = getIntent().getExtras();
        IDoubleValue vd = new DoubleVariable(3.0);
        DoubleEvaluator engine = new DoubleEvaluator();
        engine.defineVariable("x", vd);
        ToggleButton buttonStages = findViewById(R.id.buttonStage);
        if (b != null) {

            setText(Objects.requireNonNull(b.getString("key")), mathView);
            if (b.getString("function") != null)
                engine.evaluate(b.getString("function"));
            if (b.getString("stages") != null) {
                stages = findViewById(R.id.stages);
                setText(Objects.requireNonNull(b.getString("stages")), stages);
                stages.setVisibility(View.INVISIBLE);
                stages.setEnabled(false);
                buttonStages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            stages.setVisibility(View.VISIBLE);
                            stages.setEnabled(true);
                        } else {
                            stages.setVisibility(View.INVISIBLE);
                            stages.setEnabled(false);
                        }
                    }
                });
            } else {
                buttonStages.setVisibility(View.INVISIBLE);
                buttonStages.setEnabled(false);
            }
        }
        EditText xValue = findViewById(R.id.xValue);
        Button evaluateInFunction = findViewById(R.id.evaluateInFunction);
        TextView showValue = findViewById(R.id.resultFxn);
        if (equations == null) {
            evaluateInFunction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String x = xValue.getText().toString();
                    double value;
                    try {
                        value = Double.parseDouble(x);
                    } catch (Exception e) {
                        xValue.setError("invalid value");
                        return;
                    }
                    vd.setValue(value);

                    showValue.setText("f(" + x + ") = " + engine.evaluate());
                }
            });
        } else {
            evaluateInFunction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String x = xValue.getText().toString();
                    double value;
                    try {
                        value = Double.parseDouble(x);
                    } catch (Exception e) {
                        xValue.setError("invalid value");
                        return;
                    }

                    String output = null;
                    for (int i = 0; i < equations.size(); i++) {
                        Pair<String, Pair<Integer, Pair<Double, Double>>> aux = equations.get(i);
                        if (value > aux.second.second.first && value <= aux.second.second.second) {

                            engine.evaluate(aux.first);
                            vd.setValue(value);
                            output = "f(" + x + ") = " + engine.evaluate();
                        }
                    }
                    if (output != null)
                        showValue.setText(output);
                    else
                        xValue.setError("invalid value");


                }
            });
        }

    }

    private void setText(String expression, MathView math) {
        math.getSettings().setUseWideViewPort(true);
        math.getSettings().setLoadWithOverviewMode(true);
        if (expression.length() < 30)
            math.setTextSize(100);
        else if (expression.length() < 80)
            math.setTextSize(80);
        else math.setTextSize(60);
        math.setDisplayText(expression);
        math.getSettings().setJavaScriptEnabled(true);
        math.getSettings().setBuiltInZoomControls(true);
        math.getSettings().setDisplayZoomControls(true);
    }
}
