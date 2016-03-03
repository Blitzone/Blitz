package com.example.bsaraci.blitzone;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;

public class Blitzone extends AppCompatActivity
{

    Toolbar blitzoneToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BlitzoneViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blitzone_main);
        blitzoneToolbar = (Toolbar) findViewById(R.id.toolbar_of_blitzone);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new BlitzoneViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        /*
        TabLayout.newTab() method creates a tab view, Now a Tab view is not the view
        which is below the tabs, its the tab itself.
         */

        final TabLayout.Tab daily = tabLayout.newTab();
        final TabLayout.Tab blitzone = tabLayout.newTab();
        final TabLayout.Tab best = tabLayout.newTab();

        /*
        Setting Title text for our tabs respectively
         */

        daily.setText("Daily");
        blitzone.setText("Blitzone");
        best.setText("Best");

        /*
        Adding the tab view to our tablayout at appropriate positions
        As I want home at first position I am passing home and 0 as argument to
        the tablayout and like wise for other tabs as well
         */
        tabLayout.addTab(daily, 0);
        tabLayout.addTab(blitzone, 1);
        tabLayout.addTab(best, 2);

        /*
        TabTextColor sets the color for the title of the tabs, passing a ColorStateList here makes
        tab change colors in different situations such as selected, active, inactive etc

        TabIndicatorColor sets the color for the indiactor below the tabs
         */

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));

        /*
        Adding a onPageChangeListener to the viewPager
        1st we add the PageChangeListener and pass a TabLayoutPageChangeListener so that Tabs Selection
        changes when a viewpager page changes.
         */

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }


    public void searchFromBlitzoneButtonCallback(View view)
    {
        Intent intent = new Intent(this, Search.class);

        startActivity(intent);
    }

    public void profileFromBlitzoneButtonCallback(View view)
    {
        Intent intent = new Intent(this, Profile.class);

        startActivity(intent);
    }
}