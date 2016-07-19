package com.example.bsaraci.blitzone.Blitzone;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsaraci.blitzone.Profile.Profile;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.Search;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

public class Blitzone extends AppCompatActivity
{

    Toolbar blitzoneToolbar;
    private NavigationTabStrip tabLayout;
    private ViewPager viewPager;
    private BlitzoneViewPagerAdapter viewPagerAdapter;
    TextView toolbarTitle1;
    TextView toolbarTitle2;
    CharSequence titles[]={"Daily","Best"};
    int numbOfTabs =2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blitzone_main);
        blitzoneToolbar = (Toolbar) findViewById(R.id.toolbar_of_blitzone);
        toolbarTitle1 = (TextView)findViewById(R.id.blitzone_toolbar_title);
        toolbarTitle2 = (TextView)findViewById(R.id.blitzone_toolbar_title1);

        tabLayout = (NavigationTabStrip) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new BlitzoneViewPagerAdapter(getSupportFragmentManager(), titles, numbOfTabs);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    toolbarTitle1.setVisibility(View.VISIBLE);
                    toolbarTitle2.setVisibility(View.GONE);
                }
                else if(position==1){
                    toolbarTitle1.setVisibility(View.GONE);
                    toolbarTitle2.setVisibility(View.VISIBLE);                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setTitles("Daily", "Best");
        tabLayout.setViewPager(viewPager);



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