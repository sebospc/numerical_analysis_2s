package com.sands.aplication.numeric.fragments.systemEquationsFragment;


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

import com.sands.aplication.numeric.R;
import com.sands.aplication.numeric.fragments.customPopUps.popUpJacobi;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.sands.aplication.numeric.fragments.systemEquations.xValuesText;

/**
 * A simple {@link Fragment} subclass.
 */
public class jacobi extends baseIterativeMethods {
    private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public EditText error, iters, relaxation;
    public ToggleButton errorToggle;
    @SuppressLint("StaticFieldLeak")
    public LinearLayout initialValues;
    private boolean errorDivision = false;

    public jacobi() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_jacobi, container, false);
        Button run = view.findViewById(R.id.run);

        Button pivoter = view.findViewById(R.id.pivoting);
        Button runChart = view.findViewById(R.id.runChart);
        Button runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        runChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calc)
                    executeChart(getContext());
            }
        });
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
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpJacobi.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void bootStrap(double[][] expandedMatrix) {
        xValuesText.removeAllViews();
        errorDivision = false;
        double[] initial = new double[expandedMatrix.length];
        for (int i = 0; i < initialValues.getChildCount(); i++) {
            EditText aux = ((EditText) initialValues.getChildAt(i));
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
            jacobiMethod(iterations, tolerance, relax, initial, expandedMatrix);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void jacobiMethod(int iters, double tolerance, double relax, double[] initials, double[][] expandedMAtrix) {
        int cont = 0;
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
        while (dispersion > tolerance && cont < iters) {

            aux = new LinkedList<>();
            double[] x1;
            x1 = calcNewJacobi(x0, expandedMAtrix, relax);
            try {
                if (!errorToggle.isChecked())
                    dispersion = rule(minus(x1, x0));
                else {
                    dispersion = rule(minus(x1, x0)) / rule(x1);
                }
            } catch (ArithmeticException ignored) {

            }

            for (double v : x1) aux.add(String.valueOf(v));
            aux.add(String.valueOf(cientificTransformation(dispersion)));
            totalInformation.add(aux);
            x0 = x1;
            cont = cont + 1;
            if (errorDivision) {
                styleWrongMessage("Error division by 0");
                return;
            }
        }
        calc = true;
        if (dispersion < tolerance) {
            for (double val : x0)
                xValuesText.addView(defaultTextView((val + "      ").substring(0, 6)));
            styleCorrectMessage();
        } else {
            for (double val : x0)
                xValuesText.addView(defaultTextView((val + "      ").substring(0, 6)));
            styleWrongMessage("The method failed in " + cont + " iterations!");
        }
    }

    private double[] calcNewJacobi(double[] x0, double[][] expandedMatrix, double relax) {
        double[] x = new double[x0.length];
        int n = expandedMatrix.length;
        int maxThreads = NUMBER_OF_CORES;
        if (n < NUMBER_OF_CORES)
            maxThreads = n;
        Thread[] cores = new Thread[maxThreads];
        jacobiThread[] values = new jacobiThread[maxThreads];
        int k = 0;

        for (int i = 0; i < n; i++) {
            if (k == maxThreads) k = 0;
            if (cores[k] == null) {
                values[i] = new jacobiThread(expandedMatrix, x0, relax, i);
                cores[i] = new Thread(values[i]);
                cores[i].start();
            } else {
                try {
                    cores[i].join();
                    if (values[i].error) styleWrongMessage("Error division by 0");
                    x[i] = values[i].value;
                    values[i] = new jacobiThread(expandedMatrix, x0, relax, i);
                    cores[i] = new Thread(values[i]);
                    cores[i].start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            k += 1;
        }

        for (int j = 0; j < maxThreads; j++) {
            if (cores[j] != null) {
                try {
                    cores[j].join();
                    if (values[j].error) errorDivision = true;
                    x[j] = values[j].value;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        return x;
    }

    private class jacobiThread implements Runnable {
        final double[] x0;
        final double relax;
        final int i;
        private final double[][] expandedMatrix;
        double value = Double.NaN;
        boolean error = false;

        jacobiThread(double[][] expandedMatrix, double[] x0, double relax, int i) {
            this.expandedMatrix = expandedMatrix;
            this.x0 = x0;
            this.relax = relax;
            this.i = i;
        }

        @Override
        public void run() {
            int n = expandedMatrix.length;
            double sum = 0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    try {
                        sum += expandedMatrix[i][j] * x0[j];
                    } catch (ArithmeticException ignored) {

                    }
                }

            }
            double denominator = expandedMatrix[i][i];
            if (denominator == 0) {
                error = true;
                return;
            }
            value = (relax * ((expandedMatrix[i][n] - sum) / denominator)) + (1 - relax) * x0[i];

        }
    }
}
