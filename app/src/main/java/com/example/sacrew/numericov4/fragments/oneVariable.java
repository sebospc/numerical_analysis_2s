package com.example.sacrew.numericov4.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;


import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.baseOneVariableFragments;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.bisectionFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.falsePositionFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.fixedPointFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.incrementalSearchFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.multipleRootsFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.newtonFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.secantFragment;
import com.example.sacrew.numericov4.pagerAdapter;
import com.example.sacrew.numericov4.utils.KeyboardUtils;
import com.example.sacrew.numericov4.utils.graphUtils;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class oneVariable extends Fragment {

    private boolean isup ;
    private RelativeLayout  hiderB;
    public static GraphView graphOneVariable;
    @SuppressLint("StaticFieldLeak")
    public static KeyboardUtils keyboardUtils;
    private com.example.sacrew.numericov4.utils.graphUtils graphUtils = new graphUtils();

    public oneVariable() {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //clear toasts
        SuperActivityToast.cancelAllSuperToasts();

        View view = inflater.inflate(R.layout.fragment_one_variable, container, false);
        keyboardUtils = new KeyboardUtils(view,R.id.keyboardView,getContext());
        isup = false;
        ImageButton hider = view.findViewById(R.id.buttonHide);
        hider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        hiderB = view.findViewById(R.id.hiderB);
        graphOneVariable = view.findViewById(R.id.oneVariableGraph);
        ViewPager slideView = view.findViewById(R.id.viewPager);
        final List<LineGraphSeries<DataPoint>> listSeries = graphUtils.graphPharallel(50, "x", 0);
        ImageButton homeGraph = view.findViewById(R.id.homeGraphButton);
        homeGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (LineGraphSeries<DataPoint> inSerie : listSeries)
                    graphOneVariable.removeSeries(inSerie);
                graphOneVariable.getViewport().setYAxisBoundsManual(true);
                graphOneVariable.getViewport().setMinY(-3);
                graphOneVariable.getViewport().setMaxY(3);

                graphOneVariable.getViewport().setXAxisBoundsManual(true);
                graphOneVariable.getViewport().setMinX(-3);
                graphOneVariable.getViewport().setMaxX(3);
                graphOneVariable.getViewport().setScrollable(true);
                graphOneVariable.getViewport().setScrollableY(true);
                graphOneVariable.getViewport().setScalable(true);
                graphOneVariable.getViewport().setScalableY(true);
                for (LineGraphSeries<DataPoint> inSerie : listSeries)
                    graphOneVariable.addSeries(inSerie);
            }
        });
        List<Fragment> fragments = new LinkedList<>();
        LinearLayout basicSection = view.findViewById(R.id.basicSection);

        fragments.add(new incrementalSearchFragment());
        fragments.add(initFragment(new bisectionFragment(),basicSection));
        fragments.add(initFragment(new falsePositionFragment(),basicSection));
        fragments.add(initFragment(new fixedPointFragment(),basicSection));
        fragments.add(initFragment(new newtonFragment(),basicSection));
        fragments.add(initFragment(new secantFragment(),basicSection));
        fragments.add(initFragment(new multipleRootsFragment(),basicSection));
        pagerAdapter pager = new pagerAdapter(getChildFragmentManager(), fragments);
        slideView.setAdapter(pager);

        int position = slideView.getCurrentItem();
        if(position == 0){
            basicSection.setEnabled(false);
            basicSection.setVisibility(View.GONE);
        }else{
            basicSection.setEnabled(true);
            basicSection.setVisibility(View.VISIBLE);
        }

        slideView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    basicSection.setEnabled(false);
                    basicSection.setVisibility(View.GONE);
                }else{
                    basicSection.setEnabled(true);
                    basicSection.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        graphOneVariable.getViewport().setYAxisBoundsManual(true);
        graphOneVariable.getViewport().setMinY(-3);
        graphOneVariable.getViewport().setMaxY(3);

        graphOneVariable.getViewport().setXAxisBoundsManual(true);
        graphOneVariable.getViewport().setMinX(-3);
        graphOneVariable.getViewport().setMaxX(3);
        graphOneVariable.getViewport().setScrollable(true);
        graphOneVariable.getViewport().setScrollableY(true);
        graphOneVariable.getViewport().setScalable(true);
        graphOneVariable.getViewport().setScalableY(true);
        for (LineGraphSeries<DataPoint> inSerie : graphUtils.graphPharallel(50, "x", 0))
            graphOneVariable.addSeries(inSerie);
        return view;
    }

    public void hide() {
        if (isup) { // down
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.01f
            );
            hiderB.setLayoutParams(param);
            TranslateAnimation animate2 = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    hiderB.getHeight(),                 // fromYDelta
                    0); // toYDelta
            animate2.setDuration(200);
            hiderB.startAnimation(animate2);

            isup = false;
        } else { //up
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.5f
            );
            hiderB.setLayoutParams(param);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    hiderB.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(200);
            animate.setFillAfter(true);
            hiderB.startAnimation(animate);

            //v.setBackgroundResource(R.drawable.ic_arrow_downward_black_24dp);
            isup = true;
        }


    }

    public Fragment initFragment(baseOneVariableFragments frag, LinearLayout basicSection){
        EditText function = (EditText) basicSection.getChildAt(0);
        EditText iterations = (EditText) basicSection.getChildAt(1);
        EditText error = (EditText) basicSection.getChildAt(2);
        ToggleButton errorToggle = (ToggleButton) basicSection.getChildAt(3);
        frag.textFunction = function;
        frag.iter = iterations;
        frag.textError = error;
        frag.errorToggle = errorToggle;
        return frag;
    }


}
