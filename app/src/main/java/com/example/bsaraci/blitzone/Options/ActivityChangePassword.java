package com.example.bsaraci.blitzone.Options;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

/**
 * Created by bsaraci on 5/6/2016.
 */
public class ActivityChangePassword extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);

        Intent intent = getIntent();
        String toolbarTitle = intent.getExtras().getString("toolbarTitle");
        TextView toolbarTextView = (TextView)findViewById(R.id.password_change_toolbar_title);
        toolbarTextView.setText(toolbarTitle);

    }

    public void optionsFromChangePassword(View view){
        finish();
    }
}