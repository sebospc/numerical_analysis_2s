package com.example.sacrew.numericov4.fragments.systemEquations;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;

import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.xValuesText;

/**
 * A simple {@link Fragment} subclass.
 */
public class gaussSimple extends baseSystemEquations {
    public gaussSimple() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gauss_simple, container, false);
        Button run = view.findViewById(R.id.run);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                begin();
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void execute(double[][] expandedMatrix){
        elimination(expandedMatrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void elimination(double [][] expandedMatrix){
        for(int k = 0; k< expandedMatrix.length-1; k++){
            for (int i = k + 1; i < expandedMatrix.length; i++){
                if(expandedMatrix[k][k] == 0)
                    System.out.println("error division 0 wiuwiuwiu");
                double multiplier = expandedMatrix[i][k] / expandedMatrix[k][k];
                for(int j = k; j < expandedMatrix.length + 1; j++){
                    expandedMatrix[i][j] = expandedMatrix[i][j] - multiplier*expandedMatrix[k][j];
                }
            }
        }
        substitution(expandedMatrix);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void substitution(double [][] gauss){
        xValuesText.removeAllViews();
        int n = gauss.length-1;
        double[] values = new double[n+1];
        if(gauss[n][n] == 0)
            System.out.println("wiuwiuwiu division 0");
        double x = gauss[n][n+1]/gauss[n][n];

        values[values.length-1] = x;
        for(int i = 0 ; i<n+1 ; i++){
            double sumatoria = 0;
            int auxi = n-i;
            for(int p = auxi + 1; p < n+1; p++ ){
                sumatoria = sumatoria + gauss[auxi][p]*values[p];
            }
            if(gauss[auxi][auxi] == 0)
                System.out.println("division 0 wiuwiwuwiu");
            values[auxi] = (gauss[auxi][n+1]-sumatoria)/gauss[auxi][auxi];

        }

        for(double val: values){
            xValuesText.addView(defaultEditText(val+""));
            System.out.println(val);
        }

    }

}
