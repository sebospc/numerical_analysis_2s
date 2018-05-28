package com.example.sacrew.numericov4.fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.interpolationFragments.lagrange;
import com.example.sacrew.numericov4.fragments.interpolationFragments.newtonInterpolator;
import com.example.sacrew.numericov4.fragments.interpolationFragments.splineCuadratico;
import com.example.sacrew.numericov4.fragments.interpolationFragments.splineCubico;
import com.example.sacrew.numericov4.fragments.interpolationFragments.splineLineal;
import com.example.sacrew.numericov4.pagerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class interpolation extends Fragment {

    private TableLayout vectors;
    private int count = 2;
    public interpolation() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interpolation, container, false);

        vectors = view.findViewById(R.id.vectors);
        ViewPager slideView = view.findViewById(R.id.pager);
        List<Fragment> fragments = new LinkedList<>();
        fragments.add(new newtonInterpolator());
        fragments.add(new lagrange());
        fragments.add(new splineLineal());
        fragments.add(new splineCuadratico());
        fragments.add(new splineCubico());
        pagerAdapter pager = new pagerAdapter(getChildFragmentManager(),fragments);
        slideView.setAdapter(pager);
        initialize(count);
        ImageButton add = view.findViewById(R.id.addRow);
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                addRow();
            }
        });
        ImageButton remove= view.findViewById(R.id.deleteRow);
        remove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                removeRow();
            }
        });

        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addRow(){
        ((TableRow)vectors.getChildAt(0)).addView(defaultEditText("0"));
        ((TableRow)vectors.getChildAt(1)).addView(defaultEditText("0"));
        count = count + 1;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void removeRow(){
        if(count > 2) {
            int last = vectors.getChildCount() - 1;
            ((TableRow) vectors.getChildAt(0)).removeViewAt(last);
            ((TableRow) vectors.getChildAt(1)).removeViewAt(last);
            count = count -1;
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initialize(int count){
        TableRow aux = new TableRow(getContext());
        aux.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        TableRow aux2 = new TableRow(getContext());
        aux2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        for(int i = 0; i< count;i++){
            aux.addView(defaultEditText("0"));
            aux2.addView(defaultEditText("0"));
        }
        vectors.addView(aux, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        vectors.addView(aux2, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

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
