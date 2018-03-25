package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sacrew.numericov4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class incrementalSearchFragment extends Fragment {


    public incrementalSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_incremental_search,container,false);
        return view;
    }

}
