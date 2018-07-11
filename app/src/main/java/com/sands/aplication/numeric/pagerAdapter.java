package com.sands.aplication.numeric;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by sacrew on 25/03/18.
 */

public class pagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments;

    public pagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int pos) {
        return fragments.get(pos);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
