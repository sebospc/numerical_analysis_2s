package com.example.sacrew.numericov4.fragments.systemEquationsFragment.showStagesModel;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;

import java.util.LinkedList;

import static com.example.sacrew.numericov4.fragments.systemEquations.animations;
import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;

public class showStages extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout stageContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        animatorSet.removeAllListeners();
        animatorSet.end();
        animatorSet.cancel();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_stages);
        LinearLayout content = findViewById(R.id.stageContents);
        content.removeAllViews();
        if(stageContent != null) {
            if(stageContent.getParent() != null)
                ((ViewGroup)stageContent.getParent()).removeAllViews();
            content.addView(stageContent);
        }

    }


}
