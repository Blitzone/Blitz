package com.example.bsaraci.blitzone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class BlitzoneViewPagerAdapter extends FragmentStatePagerAdapter {

    public BlitzoneViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0: return new DailyTabFragment();
            case 1: return new BlitzoneTabFragment();
            case 2: return new BestTabFragment();
            default : return null;
        }
    }

    @Override
    public int getCount() {
        return 3;           // As there are only 3 Tabs
    }

}
