package com.example.sacrew.numericov4.fragments;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.bisectionFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.falsePositionFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.fixedPointFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.incrementalSearchFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.multipleRootsFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.newtonFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.secantFragment;
import com.example.sacrew.numericov4.pagerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class oneVariable extends Fragment {
    private LinearLayout dotLayout;

    private TextView[] dots;
    private int size;
    public oneVariable() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_variable, container, false);
        ViewPager slideView = view.findViewById(R.id.viewPager);
        dotLayout = view.findViewById(R.id.dotLayout);
        dotLayout.setGravity(Gravity.CENTER);

        List<Fragment> fragments = new LinkedList<>();
        fragments.add(new incrementalSearchFragment());
        fragments.add(new bisectionFragment());
        fragments.add(new falsePositionFragment());
        fragments.add(new fixedPointFragment());
        fragments.add(new newtonFragment());
        fragments.add(new secantFragment());
        fragments.add(new multipleRootsFragment());
        size = fragments.size();
        pagerAdapter pager = new pagerAdapter(getChildFragmentManager(),fragments);
        slideView.setAdapter(pager);
        slideView.addOnPageChangeListener(viewListener);
        addDotsIndicator(0);

        return view;
    }

    public void addDotsIndicator(int position){
        dots = new TextView[size];
        dotLayout.removeAllViews();
        for(int i = 0 ;i < dots.length;i++){
            dots[i]= new TextView(this.getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(R.color.Transparent));
            dots[i].setGravity(Gravity.CENTER);

            dotLayout.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
