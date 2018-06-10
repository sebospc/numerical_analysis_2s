package com.example.sacrew.numericov4.fragments.systemEquationsFragment;


import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpBisection;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpSeidel;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.fragments.systemEquations.animations;
import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;
import static com.example.sacrew.numericov4.fragments.systemEquations.count;
import static com.example.sacrew.numericov4.fragments.systemEquations.matrixAText;
import static com.example.sacrew.numericov4.fragments.systemEquations.xValuesText;

/**
 * A simple {@link Fragment} subclass.
 */
public class gaussSeidel extends baseIterativeMethods {

    private EditText error,iters,relaxation;
    private ToggleButton errorToggle;
    String mensaje = "";

    @SuppressLint("StaticFieldLeak")
    public static LinearLayout initialValuesSeidel;
    public gaussSeidel() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gauss_seidel, container, false);
        error = view.findViewById(R.id.error);
        iters = view.findViewById(R.id.iterations);
        relaxation = view.findViewById(R.id.relaxation);
        Button run = view.findViewById(R.id.run);
        errorToggle = view.findViewById(R.id.errorToggle);
        initialValuesSeidel = view.findViewById(R.id.initialValues);
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
                if(calc)
                    executeChart(getContext());
            }
        });
        for(int i = 0; i < count; i++) {
            initialValuesSeidel.addView(defaultEditText("0",false));
        }
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
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpSeidel.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void bootStrap(double[][] expandedMatrix){
        xValuesText.removeAllViews();
        double [] initial = new double[expandedMatrix.length];
        for(int i = 0;i<initialValuesSeidel.getChildCount();i++){
            EditText aux = ((EditText)initialValuesSeidel.getChildAt(i));
            try{
                initial[i] = Double.parseDouble(aux.getText().toString());
            }catch (Exception e){
                aux.setError("invalid value");
                return;
            }
        }

        boolean works = true;
        int iterations = 0;
        try {
            iterations=Integer.parseInt(iters.getText().toString());
        }catch (Exception e){
            iters.setError("Invalid iterations");
            works = false;
        }
        double tolerance = 0;
        try{
            tolerance = Double.parseDouble(error.getText().toString());
        }catch (Exception e){
            error.setError("Invalid tolerance");
            works = false;
        }
        double relax = 0;
        try {
            relax = Double.parseDouble(relaxation.getText().toString());
        }catch (Exception e){
            relaxation.setError("Invalid relaxation");
        }
        if(works)
            gaussSeidelMethod(iterations,tolerance,relax,initial,expandedMatrix);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void gaussSeidelMethod(int iters, double tolerance, double relax , double[] initials, double[][] expandedMAtrix){
        int contador = 0;
        double dispersion = tolerance + 1;
        double [] x0 = initials;
        totalInformation = new LinkedList<>();
        lisTitles = new LinkedList<>();
        List<String> aux = new LinkedList<>();
        for(int i = 0; i < initials.length; i++){
            lisTitles.add("X"+(i+1));
            aux.add(String.valueOf(x0[i]));
        }
        lisTitles.add("Norma");
        aux.add(String.valueOf(dispersion));
        totalInformation.add(aux);
        while(dispersion > tolerance && contador < iters){
            aux = new LinkedList<>();
            double [] x1 ;
            x1 = calcNewSeidel(x0,expandedMAtrix,relax);
            if(errorToggle.isChecked())
                dispersion = norma(minus(x1,x0));
            else
                dispersion = norma(minus(x1,x0))/norma(x1);
            for(double v:x1)aux.add(String.valueOf(v));
            aux.add(String.valueOf(dispersion));
            totalInformation.add(aux);
            x0 = x1;
            contador = contador + 1;
        }
        calc = true;
        if(dispersion < tolerance){
            for(double val: x0)
                xValuesText.addView(defaultEditText((val+"      ").substring(0,6)));
        }else{
            for(double val: x0)
                xValuesText.addView(defaultEditText((val+"      ").substring(0,6)));
            //Toast.makeText(getContext(),  "Failed", Toast.LENGTH_SHORT).show();
            mensaje = "The method failed!";
            styleWrongMessage(mensaje);
        }
    }

    public double[] calcNewSeidel(double[] x0, double[][] expandedMatrix, double relax){
        double[] x = new double[x0.length];
        System.arraycopy(x0, 0, x, 0, x0.length);
        int n = expandedMatrix.length;
        for(int i = 0; i < n ; i++){
            double suma = 0;
            for(int j = 0; j < n ; j++){
                if( j!= i)
                    suma = suma + expandedMatrix[i][j]*x[j];
            }
            double value = (relax*((expandedMatrix[i][n] - suma)/expandedMatrix[i][i]))+(1-relax)*x0[i];
            x[i] = value;
        }
        return x;
    }

    private final SuperActivityToast.OnButtonClickListener onButtonClickListener =
            new SuperActivityToast.OnButtonClickListener() {

                @Override
                public void onClick(View view, Parcelable token) {
                    SuperToast.create(view.getContext(), null, Style.DURATION_VERY_SHORT)
                            .setColor(Color.TRANSPARENT).show();
                }
            };

    private void styleWrongMessage(String message){
        SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_BUTTON)
                .setButtonText("UNDO")
                .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                .setProgressBarColor(Color.WHITE)
                .setText(message)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(Color.rgb(244,67,54))
                .setAnimations(Style.ANIMATIONS_POP).show();
    }

}
