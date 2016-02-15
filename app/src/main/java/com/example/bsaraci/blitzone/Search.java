package com.example.bsaraci.blitzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Search extends AppCompatActivity
{
    Toolbar searchToolbar ;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
       searchToolbar = (Toolbar) findViewById(R.id.toolbar_of_search);

    }

    public void blitzoneFromSearchButtonCallback (View view)
    {
        finish();
    }
}
