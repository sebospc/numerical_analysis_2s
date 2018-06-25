package com.example.sacrew.numericov4.fragments.systemEquationsFragment;


import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpSeidel;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.example.sacrew.numericov4.fragments.systemEquations.xValuesText;

/**
 * A simple {@link Fragment} subclass.
 */
public class gaussSeidel extends baseIterativeMethods {

    public EditText error, iters, relaxation;
    public ToggleButton errorToggle;
    @SuppressLint("StaticFieldLeak")
    public LinearLayout initialValuesSeidel;
    private boolean errorDivision = false;

    public gaussSeidel() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gauss_seidel, container, false);
        Button run = view.findViewById(R.id.run);
        Button runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        Button runChart = view.findViewById(R.id.runChart);
        runChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calc)
                    executeChart(getContext());
            }
        });
        Button pivoter = view.findViewById(R.id.pivoting);
        pivoter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                securePivot();
            }
        });
        run.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                begin();
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpSeidel.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void bootStrap(double[][] expandedMatrix) {
        xValuesText.removeAllViews();
        errorDivision = false;
        double[] initial = new double[expandedMatrix.length];
        for (int i = 0; i < initialValuesSeidel.getChildCount(); i++) {
            EditText aux = ((EditText) initialValuesSeidel.getChildAt(i));
            try {
                initial[i] = Double.parseDouble(aux.getText().toString());
            } catch (Exception e) {
                aux.setError("invalid value");
                return;
            }
        }

        boolean works = true;
        int iterations = 0;
        try {
            iterations = Integer.parseInt(iters.getText().toString());
        } catch (Exception e) {
            iters.setError("Invalid iterations");
            works = false;
        }
        double tolerance = 0;
        try {
            tolerance = Double.parseDouble(error.getText().toString());
        } catch (Exception e) {
            error.setError("Invalid tolerance");
            works = false;
        }
        double relax = 0;
        try {
            relax = Double.parseDouble(relaxation.getText().toString());
        } catch (Exception e) {
            relaxation.setError("Invalid relaxation");
            works = false;
        }
        if (works)
            gaussSeidelMethod(iterations, tolerance, relax, initial, expandedMatrix);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void gaussSeidelMethod(int iters, double tolerance, double relax, double[] initials, double[][] expandedMAtrix) {
        int contador = 0;
        double dispersion = tolerance + 1;
        double[] x0 = initials;
        totalInformation = new LinkedList<>();
        lisTitles = new LinkedList<>();
        List<String> aux = new LinkedList<>();
        for (int i = 0; i < initials.length; i++) {
            lisTitles.add("X" + (i + 1));
            aux.add(String.valueOf(x0[i]));
        }
        lisTitles.add("Error");
        aux.add(String.valueOf(dispersion));
        totalInformation.add(aux);
        calc = true;
        while (dispersion > tolerance && contador < iters) {
            aux = new LinkedList<>();
            double[] x1;
            x1 = calcNewSeidel(x0, expandedMAtrix, relax);
            try {
                if (!errorToggle.isChecked())
                    dispersion = rule(minus(x1, x0));
                else
                    dispersion = rule(minus(x1, x0)) / rule(x1);
            } catch (ArithmeticException ignored) {

            }
            for (double v : x1) aux.add(String.valueOf(v));
            aux.add(String.valueOf(dispersion));
            totalInformation.add(aux);
            x0 = x1;
            contador = contador + 1;
            if (errorDivision) {
                styleWrongMessage("Error division by zero");
                return;
            }
        }

        if (dispersion < tolerance) {
            for (double val : x0)
                xValuesText.addView(defaultTextView((val + "      ").substring(0, 6)));
            styleCorrectMessage();
        } else {
            for (double val : x0)
                xValuesText.addView(defaultTextView((val + "      ").substring(0, 6)));
            styleWrongMessage("The method failed in " + contador + " iteration!");
        }
    }

    private double[] calcNewSeidel(double[] x0, double[][] expandedMatrix, double relax) {
        double[] x = new double[x0.length];
        System.arraycopy(x0, 0, x, 0, x0.length);
        int n = expandedMatrix.length;
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    try {
                        sum = sum + expandedMatrix[i][j] * x[j];
                    } catch (ArithmeticException ignored) {

                    }
                }
            }
            double denominator = expandedMatrix[i][i];
            if (denominator == 0) {
                errorDivision = true;
            }
            double value = Double.NaN;
            try {
                value = (relax * ((expandedMatrix[i][n] - sum) / denominator)) + (1 - relax) * x0[i];
            } catch (ArithmeticException ignored) {

            }
            x[i] = value;
        }
        return x;
    }


}
