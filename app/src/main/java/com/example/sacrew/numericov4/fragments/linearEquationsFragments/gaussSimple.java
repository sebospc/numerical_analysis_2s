package com.example.sacrew.numericov4.fragments.linearEquationsFragments;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.text.method.SingleLineTransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class gaussSimple extends Fragment {

    private View view;
    private TableLayout matrix;
    private LinearLayout equals;
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
        paintMatrix(10);
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



}
