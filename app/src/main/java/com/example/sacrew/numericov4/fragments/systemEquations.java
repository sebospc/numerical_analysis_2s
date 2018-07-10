package com.example.sacrew.numericov4.fragments;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.Croult;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.cholesky;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.doolittle;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.gaussSeidel;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.gaussSimple;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.inverseDeterminant;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.jacobi;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.partialPivoting;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment.totalPivoting;
import com.example.sacrew.numericov4.pagerAdapter;
import com.example.sacrew.numericov4.utils.KeyboardUtils;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class systemEquations extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static TableLayout matrixAText;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout bValuesText, xValuesText;
    @SuppressLint("StaticFieldLeak")
    public static SeekBar times;
    public static List<Animator> animations;
    public static AnimatorSet animatorSet = new AnimatorSet();
    @SuppressLint("StaticFieldLeak")
    public static ImageButton backMAtrix;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout xIndex;
    public static String[][] matrixBackpack;
    public static Boolean pivoted = false;
    public KeyboardUtils keyboardUtils;
    private int count;
    private LinearLayout initialValues;
    private LinearLayout defaultInfo;
    int position;

    public systemEquations() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        count = 4;
        //clear toasts
        SuperActivityToast.cancelAllSuperToasts();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_system_equations, container, false);

        //KeyboardUtils keyboardUtils = new KeyboardUtils(view,R.id.keyboardView);
        keyboardUtils = new KeyboardUtils(view, R.id.keyboardView, getContext());
        defaultInfo = view.findViewById(R.id.defaultInfo);
        initialValues = view.findViewById(R.id.initialValues);
        ImageButton add = view.findViewById(R.id.addRow);
        ImageButton remove = view.findViewById(R.id.deleteRow);
        Button btnNext = view.findViewById(R.id.btnNext);
        Button btnPrev = view.findViewById(R.id.btnPrev);
        pivoted = false;
        xIndex = view.findViewById(R.id.arrayXindex);
        xIndex.setVisibility(View.GONE);
        backMAtrix = view.findViewById(R.id.backButton);
        backMAtrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animatorSet != null) {
                    animatorSet.removeAllListeners();
                    animatorSet.end();
                    animatorSet.cancel();
                }
                matrixAText.removeAllViews();
                bValuesText.removeAllViews();
                for (String[] aMatrixBackpack : matrixBackpack) {
                    TableRow row = new TableRow(getContext());
                    for (int j = 0; j < matrixBackpack.length; j++) {
                        row.addView(defaultEditText(aMatrixBackpack[j] + ""));
                    }
                    matrixAText.addView(row);
                    bValuesText.addView(defaultEditText(aMatrixBackpack[matrixBackpack.length] + ""));
                }

                backMAtrix.setVisibility(View.GONE);
                xIndex.setVisibility(View.GONE);
                pivoted = false;
            }
        });
        backMAtrix.setVisibility(View.GONE);
        matrixAText = view.findViewById(R.id.matrixA);
        bValuesText = view.findViewById(R.id.arrayB);
        xValuesText = view.findViewById(R.id.arrayResult);

        times = view.findViewById(R.id.seekBar2);
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (!pivoted)
                    addRow();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (!pivoted)
                    removeRow();
            }
        });
        view.post(new Runnable() {
            @Override
            public void run() {
                Space space = view.findViewById(R.id.spaceSystem);
                ViewGroup.LayoutParams params = space.getLayoutParams();
                params.width = times.getMeasuredWidth() - times.getMeasuredWidth() / 6;
                space.setLayoutParams(params);
            }
        });
        paintMatrix();

        ViewPager slideView = view.findViewById(R.id.pager);
        ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SuperActivityToast.cancelAllSuperToasts();
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
        fragments.add(new Croult());
        fragments.add(new doolittle());
        fragments.add(new cholesky());
        fragments.add(new inverseDeterminant());
        EditText relaxation = view.findViewById(R.id.relaxation);
        EditText iters = view.findViewById(R.id.iterations);
        EditText error = view.findViewById(R.id.error);
        ToggleButton toggleError = view.findViewById(R.id.errorToggle);
        jacobi jacob = new jacobi();
        jacob.relaxation = relaxation;
        jacob.iters = iters;
        jacob.error = error;
        jacob.errorToggle = toggleError;
        jacob.initialValues = initialValues;

        fragments.add(jacob);
        gaussSeidel seidel = new gaussSeidel();
        seidel.relaxation = relaxation;
        seidel.iters = iters;
        seidel.error = error;
        seidel.errorToggle = toggleError;
        seidel.initialValuesSeidel = initialValues;
        fragments.add(seidel);
        pagerAdapter pager = new pagerAdapter(getChildFragmentManager(), fragments);
        slideView.setAdapter(pager);
        position = slideView.getCurrentItem();
        if(position == 0){
            defaultInfo.setEnabled(false);
            defaultInfo.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.INVISIBLE);
        }else if (position < 7 && position != 0) {
            defaultInfo.setEnabled(false);
            defaultInfo.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
        } else if(position == 8){
            defaultInfo.setEnabled(true);
            defaultInfo.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
        }else{
            defaultInfo.setEnabled(true);
            defaultInfo.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideView.setCurrentItem(position++);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideView.setCurrentItem(position--);
            }
        });
        slideView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    defaultInfo.setEnabled(false);
                    defaultInfo.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.INVISIBLE);
                }else if (position < 7 && position != 0) {
                    defaultInfo.setEnabled(false);
                    defaultInfo.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                } else if(position == 8){
                    defaultInfo.setEnabled(true);
                    defaultInfo.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                }else{
                    defaultInfo.setEnabled(true);
                    defaultInfo.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        times.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (animatorSet != null) {
                    animatorSet.cancel();
                    animatorSet = new AnimatorSet();
                    animatorSet.playSequentially(animations);
                    animatorSet.setDuration(times.getProgress() * 500);
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
    private void addRow() {
        int n = matrixAText.getChildCount();
        TableRow row = new TableRow(getContext());
        for (int i = 0; i <= n; i++) {
            if (i == n)
                row.addView(defaultEditText("1"));
            if (i != n)
                row.addView(defaultEditText("0"));
            if (i != n)
                ((TableRow) matrixAText.getChildAt(i)).addView(defaultEditText("0"));
        }
        matrixAText.addView(row);
        bValuesText.addView(defaultEditText("0"));
        initialValues.addView(defaultEditText("0"));
        count = count + 1;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void removeRow() {
        int n = matrixAText.getChildCount();
        if (n > 2) {
            for (int i = 0; i < n - 1; i++) {
                EditText text = (EditText) ((TableRow) matrixAText.getChildAt(i)).getChildAt(n - 1);
                ((TableRow) matrixAText.getChildAt(i)).removeView(text);
            }
            matrixAText.removeView(matrixAText.getChildAt(n - 1));
            bValuesText.removeView(bValuesText.getChildAt(n - 1));
            initialValues.removeView(initialValues.getChildAt(n - 1));
            count = count - 1;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void paintMatrix() {

        for (int i = 1; i <= 4; i++) {
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 1; j <= 4; j++) {

                if (i == j)
                    row.addView(defaultEditText("1"));
                else
                    row.addView(defaultEditText("0"));

            }
            matrixAText.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            bValuesText.addView(defaultEditText("0"));
            initialValues.addView(defaultEditText("0"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private EditText defaultEditText(String value) {
        EditText text = new EditText(getContext());
        text.setLayoutParams(new TableRow.LayoutParams(100, 110));
        text.setEms(2);
        text.setMaxLines(1);
        text.setBackground(null);
        text.setTextColor(Color.WHITE);
        text.setTypeface(null, Typeface.BOLD);
        text.setBackgroundColor(Color.parseColor("#FF303F9F"));
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setText(value);
        keyboardUtils.registerEdittext(text, getContext(), getActivity());
        return text;
    }

}
