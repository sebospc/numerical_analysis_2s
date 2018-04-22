package com.example.sacrew.numericov4;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.sacrew.numericov4.fragments.oneVariableFragments.bisectionFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.falsePositionFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.fixedPointFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.incrementalSearchFragment;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.multipleRoots;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.newton;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.secant;

import java.util.List;

/**
 * Created by sacrew on 25/03/18.
 */

public class pagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    public pagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments =  fragments;
    }

    @Override
    public Fragment getItem(int pos) {
        return fragments.get(pos);
        /*switch(pos){
            case 0: return  new incrementalSearchFragment();
            case 1: return new bisectionFragment();
            case 2: return new falsePositionFragment();
            case 3: return new fixedPointFragment();
            case 4: return new newton();
            case 5: return new secant();
            case 6: return new multipleRoots();
            default: return new incrementalSearchFragment();
        }*/
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
