package com.example.sacrew.numericov4.fragments.interpolationFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sacrew.numericov4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class splineLineal extends baseInterpolationMethods{


    public splineLineal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spline_lineal, container, false);
    }

}