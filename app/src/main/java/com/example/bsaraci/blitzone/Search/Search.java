package com.example.bsaraci.blitzone.Search;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

public class Search extends AppCompatActivity
{
    Toolbar searchToolbar ;
    TextView toolbarTitle;
    Typeface titleFont;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        searchToolbar = (Toolbar) findViewById(R.id.toolbar_of_search);
        toolbarTitle = (TextView)findViewById(R.id.search_toolbar_title);

    }

    public void blitzoneFromSearchButtonCallback (View view)
    {
        finish();
    }
}
