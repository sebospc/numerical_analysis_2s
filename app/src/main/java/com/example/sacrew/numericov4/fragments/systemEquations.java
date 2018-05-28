package com.example.sacrew.numericov4.fragments;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.cholesky;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.croult;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.doolittle;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.gaussSeidel;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.gaussSimple;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.inverseDeterminant;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.jacobi;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.partialPivoting;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.totalPivoting;
import com.example.sacrew.numericov4.pagerAdapter;


import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.gaussSeidel.initialValuesSeidel;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.jacobi.initialValues;

/**
 * A simple {@link Fragment} subclass.
 */
public class systemEquations extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static TableLayout matrixAText;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout bValuesText,xValuesText;
    @SuppressLint("StaticFieldLeak")
    public  static SeekBar times;
    public static List<Animator> animations;
    public static int count =  4;
    public static AnimatorSet animatorSet = new AnimatorSet();

    int matrixA [][];
    int bValues[],xValues[];
    public systemEquations() {
        // Required empty public constructor
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_system_equations,container,false);


        ImageButton add = view.findViewById(R.id.addRow);
        ImageButton remove = view.findViewById(R.id.deleteRow);
        matrixAText = view.findViewById(R.id.matrixA);
        bValuesText = view.findViewById(R.id.arrayB);
        xValuesText = view.findViewById(R.id.arrayResult);
        times = view.findViewById(R.id.seekBar2);
        matrixA = new int[0][0];
        bValues = new int[0];
        xValues = new int[0];
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                addRow();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                removeRow();
            }
        });

        paintMatrix(4);

        ViewPager slideView = view.findViewById(R.id.pager);
        ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                animatorSet.removeAllListeners();
                animatorSet.end();
                animatorSet.cancel();
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        slideView.addOnPageChangeListener(viewListener);

        List<Fragment> fragments = new LinkedList<>();
        fragments.add(new gaussSimple());
        fragments.add(new partialPivoting());
        fragments.add(new totalPivoting());
        fragments.add(new croult());
        fragments.add(new doolittle());
        fragments.add(new cholesky());
        fragments.add(new inverseDeterminant());
        fragments.add(new jacobi());
        fragments.add(new gaussSeidel());
        pagerAdapter pager = new pagerAdapter(getChildFragmentManager(),fragments);
        slideView.setAdapter(pager);
        times.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(animatorSet.isRunning()){
                    animatorSet.cancel();
                    animatorSet = new AnimatorSet();
                    animatorSet.playSequentially(animations);
                    animatorSet.setDuration(times.getProgress()*500);
                    animatorSet.start();
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addRow(){
        int n = matrixAText.getChildCount();
        TableRow row = new TableRow(getContext());
        for(int i = 0; i<= n; i++){
            if(i == n)
                row.addView(defaultEditText("1"));
            if(i!=n)
                row.addView(defaultEditText("0"));
            if(i!=n)
                ((TableRow )matrixAText.getChildAt(i)).addView(defaultEditText("0"));
        }
        matrixAText.addView(row);
        bValuesText.addView(defaultEditText("0"));
        initialValues.addView(defaultEditText("0"));
        initialValuesSeidel.addView(defaultEditText("0"));
        count = count + 1;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void removeRow(){
        int n = matrixAText.getChildCount();
        if(n > 2) {
            for (int i = 0; i < n-1; i++) {
                EditText text = (EditText) ((TableRow) matrixAText.getChildAt(i)).getChildAt(n-1);
                ((TableRow) matrixAText.getChildAt(i)).removeView(text);
            }
            matrixAText.removeView(matrixAText.getChildAt(n-1));
            bValuesText.removeView(bValuesText.getChildAt(n-1));
            initialValues.removeView(initialValues.getChildAt(n-1));
            initialValuesSeidel.removeView(initialValuesSeidel.getChildAt(n-1));
            count = count - 1;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void paintMatrix(int n){

        for(int i = 1; i<=n; i++){
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            for(int j = 1; j<=n; j++) {

                if(i == j)
                    row.addView(defaultEditText("1"));
                else
                    row.addView(defaultEditText("0"));
            }
            matrixAText.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            bValuesText.addView(defaultEditText("0"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public EditText defaultEditText(String value){
        EditText text = new EditText(getContext());
        text.setLayoutParams(new TableRow.LayoutParams(100, 110));
        text.setEms(2);
        text.setMaxLines(1);
        text.setBackground(null);
        text.setTextColor(Color.WHITE);
        text.setTypeface(null, Typeface.BOLD);
        text.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setInputType(InputType.TYPE_CLASS_PHONE);
        text.setText(value);
        return text;
    }


}
