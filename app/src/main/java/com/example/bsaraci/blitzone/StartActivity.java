package com.example.bsaraci.blitzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by bsaraci on 4/3/2016.
 */
public class StartActivity extends AppCompatActivity {
    private boolean loggedIn=false;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(loggedIn){
            Intent intent = new Intent(this, Blitzone.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
        }

    }
}
