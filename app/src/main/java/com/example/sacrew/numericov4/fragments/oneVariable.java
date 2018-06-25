package com.example.sacrew.numericov4.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
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
import com.example.sacrew.numericov4.utils.FunctionStorage;
import com.example.sacrew.numericov4.utils.KeyboardUtils;
import com.example.sacrew.numericov4.utils.graphUtils;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class oneVariable extends Fragment {

    public static GraphView graphOneVariable;
    @SuppressLint("StaticFieldLeak")
    public static KeyboardUtils keyboardUtils;
    private final com.example.sacrew.numericov4.utils.graphUtils graphUtils = new graphUtils();
    public FunctionStorage functionStorage;
    public File temp;
    private boolean isup;
    private RelativeLayout hiderB;

    public oneVariable() {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //clear toasts
        SuperActivityToast.cancelAllSuperToasts();

        View view = inflater.inflate(R.layout.fragment_one_variable, container, false);
        keyboardUtils = new KeyboardUtils(view, R.id.keyboardView, getContext());
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
        incrementalSearchFragment inc = new incrementalSearchFragment();
        inc.textFunction = (EditText) basicSection.getChildAt(0);
        keyboardUtils.registerEdittext(inc.textFunction, getContext(), getActivity());
        inc.temp = temp;
        inc.functionStorage = functionStorage;
        fragments.add(inc);
        fragments.add(initFragment(new bisectionFragment(), basicSection));
        fragments.add(initFragment(new falsePositionFragment(), basicSection));
        fragments.add(initFragment(new fixedPointFragment(), basicSection));
        fragments.add(initFragment(new newtonFragment(), basicSection));
        fragments.add(initFragment(new secantFragment(), basicSection));
        fragments.add(initFragment(new multipleRootsFragment(), basicSection));
        pagerAdapter pager = new pagerAdapter(getChildFragmentManager(), fragments);
        slideView.setAdapter(pager);
        LinearLayout subBasicSection = view.findViewById(R.id.subBasicSection);
        int position = slideView.getCurrentItem();
        if (position == 0) {
            subBasicSection.setEnabled(false);
            subBasicSection.setVisibility(View.GONE);
        } else {
            subBasicSection.setEnabled(true);
            subBasicSection.setVisibility(View.VISIBLE);
        }

        slideView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                SuperActivityToast.cancelAllSuperToasts();
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    subBasicSection.setEnabled(false);
                    subBasicSection.setVisibility(View.GONE);
                } else {
                    subBasicSection.setEnabled(true);
                    subBasicSection.setVisibility(View.VISIBLE);
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
        for (String function : functionStorage.functions)
            keyboardUtils.addFunction(function, getContext(), functionStorage, temp);
        return view;
    }

    private void hide() {
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

    private Fragment initFragment(baseOneVariableFragments frag, LinearLayout basicSection) {
        EditText function = basicSection.findViewById(R.id.function);
        keyboardUtils.registerEdittext(function, getContext(), getActivity());
        EditText iterations = basicSection.findViewById(R.id.iterations);
        keyboardUtils.registerEdittext(iterations, getContext(), getActivity());
        EditText error = basicSection.findViewById(R.id.error);
        keyboardUtils.registerEdittext(error, getContext(), getActivity());
        ToggleButton errorToggle = basicSection.findViewById(R.id.errorToggle);
        frag.textFunction = function;
        frag.iter = iterations;
        frag.textError = error;
        frag.errorToggle = errorToggle;
        frag.functionStorage = functionStorage;
        frag.temp = temp;
        return frag;
    }


}
