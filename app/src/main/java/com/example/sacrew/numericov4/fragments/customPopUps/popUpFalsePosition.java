package com.example.sacrew.numericov4.fragments.customPopUps;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.sacrew.numericov4.R;

public class popUpFalsePosition extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_false_position);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

<<<<<<< HEAD
        getWindow().setLayout((int) (width*0.97), (int) (height*0.70));
=======
        getWindow().setLayout((int) (width * 0.97), (int) (height * 0.55));
>>>>>>> 11ec4ef89cc96e708a9911721d76a11164714eb1

    }

}
