package com.example.bsaraci.blitzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Notifications extends AppCompatActivity
{
    Toolbar notificationsToolbar ;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_main);
        notificationsToolbar = (Toolbar) findViewById(R.id.toolbar_of_notifications);

    }

    public void profileFromNotificationsButtonCallback (View view)
    {
        finish();
    }
}
