package com.example.sacrew.numericov4.oneVariableMethods;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.sacrew.numericov4.fragments.oneVariableFragments.incrementalSearchFragment;

/**
 * Created by sacrew on 25/03/18.
 */

public class pagerAdapter extends FragmentPagerAdapter {
    public pagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {


            default:return new incrementalSearchFragment();
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
