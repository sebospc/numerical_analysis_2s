package com.sands.aplication.numeric.fragments;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.sands.aplication.numeric.R;
import com.sands.aplication.numeric.fragments.oneVariableFragments.baseOneVariableFragments;
import com.sands.aplication.numeric.fragments.oneVariableFragments.bisectionFragment;
import com.sands.aplication.numeric.fragments.oneVariableFragments.falsePositionFragment;
import com.sands.aplication.numeric.fragments.oneVariableFragments.fixedPointFragment;
import com.sands.aplication.numeric.fragments.oneVariableFragments.incrementalSearchFragment;
import com.sands.aplication.numeric.fragments.oneVariableFragments.multipleRootsFragment;
import com.sands.aplication.numeric.fragments.oneVariableFragments.newtonFragment;
import com.sands.aplication.numeric.fragments.oneVariableFragments.secantFragment;
import com.sands.aplication.numeric.pagerAdapter;
import com.sands.aplication.numeric.utils.FunctionStorage;
import com.sands.aplication.numeric.utils.KeyboardUtils;
import com.sands.aplication.numeric.utils.graphUtils;
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
    private final com.sands.aplication.numeric.utils.graphUtils graphUtils = new graphUtils();
    public FunctionStorage functionStorage;
    public File temp;
    private boolean isup;
    private RelativeLayout hiderB;

    public oneVariable() {
        // Required empty public constructor
    }


    @SuppressLint("WrongConstant")
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
        Button hider = view.findViewById(R.id.buttonHide);
        Button btnNext = view.findViewById(R.id.btnNext);
        Button btnPrev = view.findViewById(R.id.btnPrev);
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
        if (slideView.getCurrentItem() == 0) {
            subBasicSection.setEnabled(false);
            subBasicSection.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.INVISIBLE);
        } else if(slideView.getCurrentItem() == 6){
            subBasicSection.setEnabled(true);
            subBasicSection.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
        } else {
            subBasicSection.setEnabled(true);
            subBasicSection.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    slideView.setCurrentItem(slideView.getCurrentItem()+1);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    slideView.setCurrentItem(slideView.getCurrentItem()-1);
            }
        });

        slideView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SuperActivityToast.cancelAllSuperToasts();
                if (position == 0) {
                    subBasicSection.setEnabled(false);
                    subBasicSection.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.INVISIBLE);
                }else  if(position == 6){
                    subBasicSection.setEnabled(true);
                    subBasicSection.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                }else {
                    subBasicSection.setEnabled(true);
                    subBasicSection.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
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
