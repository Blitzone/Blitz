package com.example.bsaraci.blitzone;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Notifications extends AppCompatActivity
{
    Toolbar notificationsToolbar ;
    TextView toolbarTitle;
    Typeface titleFont;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_main);
        notificationsToolbar = (Toolbar) findViewById(R.id.toolbar_of_notifications);
        toolbarTitle = (TextView)findViewById(R.id.notifications_toolbar_title);
        titleFont= Typeface.createFromAsset(getAssets(), "fonts/AnkePrint.ttf");
        toolbarTitle.setTypeface(titleFont);

    }

    public void profileFromNotificationsButtonCallback (View view)
    {
        finish();
    }
}
