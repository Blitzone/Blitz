package com.example.bsaraci.blitzone.Blitzone;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.Search;
import com.example.bsaraci.blitzone.Tabs.SlidingTabLayout;

public class Blitzone extends AppCompatActivity
{

    Toolbar blitzoneToolbar;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private BlitzoneViewPagerAdapter viewPagerAdapter;
    TextView toolbarTitle;
    Typeface titleFont;
    CharSequence titles[]={"Daily","Best"};
    int numbOfTabs =2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blitzone_main);
        blitzoneToolbar = (Toolbar) findViewById(R.id.toolbar_of_blitzone);
        toolbarTitle = (TextView)findViewById(R.id.blitzone_toolbar_title);

        tabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new BlitzoneViewPagerAdapter(getSupportFragmentManager(), titles, numbOfTabs);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.indicator);
            }
        });

        tabLayout.setViewPager(viewPager);
    }


    public void searchFromBlitzoneButtonCallback(View view)
    {
        Intent intent = new Intent(this, Search.class);

        startActivity(intent);
    }

    public void profileFromBlitzoneButtonCallback(View view)
    {
        finish();
    }




}