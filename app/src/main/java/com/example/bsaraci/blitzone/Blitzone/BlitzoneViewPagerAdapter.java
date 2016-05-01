package com.example.bsaraci.blitzone.Blitzone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class BlitzoneViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    public BlitzoneViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            DailyTabFragment dailyTabFragment = new DailyTabFragment();
            return dailyTabFragment;
        }

        else{
            Blitzone.BestTabFragment bestTabFragment = new Blitzone.BestTabFragment();
            return bestTabFragment;
        }
    }

    public CharSequence getPageTitle(int position){
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
