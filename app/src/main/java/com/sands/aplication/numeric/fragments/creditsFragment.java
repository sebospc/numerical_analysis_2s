package com.sands.aplication.numeric.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.sands.aplication.numeric.R;

import static com.sands.aplication.numeric.R.layout.fragment_credits;

/**
 * A simple {@link Fragment} subclass.
 */
public class creditsFragment extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fragment_credits);
        ((TextView) findViewById(R.id.superToastLib)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.evalexLib)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.graphLib)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.tableViewLib)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.mathViewLib)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.symjaLib)).setMovementMethod(LinkMovementMethod.getInstance());
    }


}