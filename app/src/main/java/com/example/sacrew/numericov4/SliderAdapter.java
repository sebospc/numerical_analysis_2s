package com.example.sacrew.numericov4;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sacrew on 22/03/18.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context ){
        this.context = context;
    }

    public String[] slide_handlings = {
            "EAT",
            "Sleep",
            "CODE"
    };
    public String [] slide_descs ={
            "asdasd",
            "dddddd",
            "ssssss"
    };

    @Override
    public int getCount() {
        return slide_handlings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view  = layoutInflater.inflate(R.layout.slide_one_variable,container,false);
        TextView desc = view.findViewById(R.id.slide_desc);
        desc.setText(slide_descs[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
