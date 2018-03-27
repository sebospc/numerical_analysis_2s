package com.example.sacrew.numericov4.oneVariableMethods;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.sacrew.numericov4.fragments.oneVariableFragments.bisectionFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.fakeRuleFragment;
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
        switch(pos){
            case 0: return new incrementalSearchFragment();
            case 1: return new bisectionFragment();
            case 2: return new fakeRuleFragment();
            default: return new incrementalSearchFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
