package com.example.sacrew.numericov4.fragments;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.ImageViewCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sacrew.numericov4.R;

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