package com.example.sacrew.numericov4.fragments.linearEquationsFragments;


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

import com.example.sacrew.numericov4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class gaussSimple extends Fragment {

    private View view;
    private TableLayout matrix;
    private LinearLayout equals;
    private Button run,add;
    int n = 2;
    public gaussSimple() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gauss_simple, container, false);
        matrix = (TableLayout) view.findViewById(R.id.matrix);
        equals = view.findViewById(R.id.arrayEquals);
        run = view.findViewById(R.id.run);
        add = view.findViewById(R.id.add);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execute();
            }
        });
        paintMatrix(n);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintMatrix(n + 1);
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void paintMatrix(int n){
        matrix.removeAllViews();
        for(int i = 1; i<=n; i++){
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            for(int j = 1; j<=n; j++) {
                EditText text = new EditText(getContext());
                text.setLayoutParams(new TableRow.LayoutParams(70, 80));
                text.setEms(2);
                text.setMaxLines(1);
                text.setBackground(null);
                text.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                text.setGravity(Gravity.CENTER_HORIZONTAL);
                text.setText("0");
                if(i == j)
                    text.setText("1");

                row.addView(text);
            }
            matrix.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            EditText text = new EditText(getContext());
            text.setLayoutParams(new TableRow.LayoutParams(70, 80));
            text.setEms(2);
            text.setMaxLines(1);
            text.setBackground(null);
            text.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
            text.setGravity(Gravity.CENTER_HORIZONTAL);
            text.setText("0");
            equals.addView(text);
        }
    }

    public void execute(){
        int n = matrix.getChildCount();
        double [][] gauss = new double[n][n+1];
        for(int i=0; i<n; i++){
            for (int j=0; j< n;j++){
                EditText aux = ((EditText) ((TableRow) matrix.getChildAt(i)).getChildAt(j));
                try {
                    double x = 0;

                    x = Double.parseDouble((aux.getText())
                            .toString());
                    //System.out.print(x);
                    gauss[i][j] = x;
                }catch (Exception e){
                    aux.setError("invalid value");
                    return;
                }
            }
            EditText aux = ((EditText)equals.getChildAt(i));
            try{
                double x = Double.parseDouble(aux.getText().toString());
                gauss[i][n] = x;
            }catch (Exception e){
                aux.setError("invalid value");
                return;
            }
        }
        elimination(gauss);
    }

    public void elimination(double [][] gauss){
        for(int k = 0; k< gauss.length; k++){
            for (int i = k + 1; i < gauss.length; i++){
                double multiplier = gauss[i][k] / gauss[k][k];
                for(int j = k; j < gauss.length + 1; j++){
                    gauss[i][j] = gauss[i][j] - multiplier*gauss[k][j];
                }

            }


        }

        for(int k = 0; k< gauss.length; k++){
            for (int i = 0; i < gauss.length+1; i++){
                System.out.print(gauss[k][i]+"    ");
            }
            System.out.println("");
        }
        substitution(gauss);
    }
    public void substitution(double [][] gauss){
        int n = gauss.length-1;
        double[] values = new double[n+1];
        double x = gauss[n][n+1]/gauss[n][n];
        values[values.length-1] = x;
        for(int i = n -1 ; i>=0 ; i--){
            double sumatoria = 0;
            for(int p = i + 1; p < n; p++ ){
                sumatoria = sumatoria + gauss[i][p]*values[p-1];
            }
            values[i] = (gauss[i][n+1]-sumatoria)/gauss[i][i];


        }

        for(double val: values){
            System.out.println(val);
        }

    }

}
