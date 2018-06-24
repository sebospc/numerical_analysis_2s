package com.example.sacrew.numericov4.fragments;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {
    private View view;

    public static List<Integer> poolColors = new LinkedList<>();
    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //clear toasts
        SuperActivityToast.cancelAllSuperToasts();
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home, container, false);


        //View viewCredits = inflater.inflate(R.layout.fragment_credits, null);

        Button btnCredits = view.findViewById(R.id.btnCredits);
        btnCredits.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                execute();
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                poolColors.add(Color.parseColor("#FF6D24"));
                poolColors.add(Color.parseColor("#00204A"));
                poolColors.add(Color.parseColor("#248888"));
                poolColors.add(Color.parseColor("#FD2E2E"));
                poolColors.add(Color.parseColor("#096C47"));
                poolColors.add(Color.parseColor("#BB0029"));
                poolColors.add(Color.parseColor("#4A772F"));
                poolColors.add(Color.parseColor("#F54D42"));
                poolColors.add(Color.parseColor("#682666"));
                for(float i = 0; i < 360; i += 360 / 200) {
                    float[] hsv = new float[3];

                    hsv[0] = i;
                    hsv[1] = (float) Math.random();
                    hsv[2] = (float) Math.random();
                    poolColors.add(Color.HSVToColor(hsv));
                }
            }
        }).start();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(){
        //Start your activity here
        Intent i = new Intent(view.getContext(), creditsFragment.class);
        startActivity(i);
    }

}
