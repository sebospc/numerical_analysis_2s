package com.sands.aplication.numeric.fragments.systemEquationsFragment.showStagesModel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sands.aplication.numeric.R;

import static com.sands.aplication.numeric.fragments.systemEquations.animatorSet;

public class showStages extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout stageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_stages);
        LinearLayout content = findViewById(R.id.stageContents);
        content.removeAllViews();
        if (stageContent != null) {
            if (stageContent.getParent() != null)
                ((ViewGroup) stageContent.getParent()).removeAllViews();
            content.addView(stageContent);
        }

    }


}
