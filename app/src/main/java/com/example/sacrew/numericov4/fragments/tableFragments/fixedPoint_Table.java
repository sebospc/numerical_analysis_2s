package com.example.sacrew.numericov4.fragments.tableFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sacrew.numericov4.R;

public class fixedPoint_Table extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fixed_point_fragment_table,container,false);
        return view;
    }

}
