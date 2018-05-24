package com.example.sacrew.numericov4.fragments.systemEquations;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;

import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.bValuesText;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.matrixAText;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.times;

/**
 * Created by sacrew on 23/05/18.
 */

public class baseSystemEquations extends Fragment {
    public baseSystemEquations() {

    }

    public void begin() {
        int n = matrixAText.getChildCount();
        double[][] expandedMatrix = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                EditText aux = ((EditText) ((TableRow) matrixAText.getChildAt(i)).getChildAt(j));
                try {
                    double x = 0;

                    x = Double.parseDouble((aux.getText())
                            .toString());
                    //System.out.print(x);
                    expandedMatrix[i][j] = x;
                } catch (Exception e) {
                    aux.setError("invalid value");
                    return;
                }
            }
            EditText aux = ((EditText) bValuesText.getChildAt(i));
            try {
                double x = Double.parseDouble(aux.getText().toString());
                expandedMatrix[i][n] = x;
            } catch (Exception e) {
                aux.setError("invalid value");
                return;
            }
        }
        execute(expandedMatrix);
    }

    public void execute(double[][] expandedMatrix) {

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public TextView defaultEditText(String value) {
        return defaultEditText(value, getResources().getColor(R.color.colorPrimary),100,10);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public TextView defaultEditText(String value, int color,int weight,int size ) {
        TextView text = new EditText(getContext());
        text.setLayoutParams(new TableRow.LayoutParams(weight, 110));
        text.setEms(2);
        text.setMaxLines(1);
        text.setBackgroundColor(color);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setKeyListener(null);
        text.setText(value);
        return text;
    }

}