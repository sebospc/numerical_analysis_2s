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
import com.example.sacrew.numericov4.pagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class oneVariable extends Fragment {
    private ViewPager slideView;
    private LinearLayout dotLayout;

    private TextView[] dots;
    private View view;
    private String [] titles;
    public oneVariable() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one_variable,container,false);
        slideView = view.findViewById(R.id.viewPager);
        dotLayout = view.findViewById(R.id.dotLayout);
        dotLayout.setGravity(Gravity.CENTER);
        titles = new String[]{"Incremental Search","Bisection","Fake Rule","Fixed Point","Newton"
                              ,"Secant","Multiple Roots"};
        pagerAdapter pager = new pagerAdapter(getChildFragmentManager());
        slideView.setAdapter(pager);
        slideView.addOnPageChangeListener(viewListener);
        addDotsIndicator(0);

        return view;
    }

    public void addDotsIndicator(int position){
        dots = new TextView[titles.length];
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
