package com.example.sacrew.numericov4.fragments.tableFragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.home;

public class incrementalSearch_Table extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.incremental_search_fragment_table,container,false);
        return view;
    }

}
