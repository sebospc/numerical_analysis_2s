package com.sands.aplication.numeric.fragments.customPopUps;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.sands.aplication.numeric.R;

public class popUpGaussSimple extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_gauss_simple);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.97), (int) (height * 0.62));

    }

}
