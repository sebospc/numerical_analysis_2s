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
import com.example.sacrew.numericov4.fragments.linearEquationsFragments.gaussSimple;
import com.example.sacrew.numericov4.pagerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class linearEquations extends Fragment {
    private ViewPager slideView;
    private LinearLayout dotLayout;

    private TextView[] dots;
    private View view;
    private int size;


    public linearEquations() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_linear_equations, container, false);
        slideView = view.findViewById(R.id.viewPager);
        dotLayout = view.findViewById(R.id.dotLayout);
        dotLayout.setGravity(Gravity.CENTER);
        List<android.app.Fragment> fragments = new LinkedList<>();
        fragments.add(new gaussSimple());
        size = fragments.size();
        pagerAdapter pager = new pagerAdapter(getChildFragmentManager(), fragments);
        slideView.setAdapter(pager);
        slideView.addOnPageChangeListener(viewListener);
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
