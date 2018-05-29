package com.example.sacrew.numericov4.fragments.interpolationFragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpBisection;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpLagrange;

/**
 * A simple {@link Fragment} subclass.
 */
public class lagrange extends baseInterpolationMethods{


    public lagrange() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_lagrange, container, false);
        Button runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpLagrange.class);
        startActivity(i);
    }


}
