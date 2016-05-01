package com.example.bsaraci.blitzone.Notifications;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

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

    }

    public void profileFromNotificationsButtonCallback (View view)
    {
        finish();
    }
}
