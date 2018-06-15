package com.example.sacrew.numericov4.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import static com.example.sacrew.numericov4.R.layout.fragment_credits;

/**
 * A simple {@link Fragment} subclass.
 */
public class creditsFragment extends Activity {
    private ImageView imageView;
    private View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fragment_credits);


    }


}