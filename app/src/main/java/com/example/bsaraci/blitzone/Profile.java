package com.example.bsaraci.blitzone;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsaraci.blitzone.HLV.HLVAdapter;
import com.example.bsaraci.blitzone.HLV.HorizontalListView;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;

import java.util.ArrayList;
import java.util.Arrays;


public class Profile extends AppCompatActivity
{
    public static final int CAMERA_REQUEST = 10;
    Toolbar profileToolbar ;
    private HorizontalListView hlv;
    private HLVAdapter hlvAdapter;

    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    TextView toolbarTitle;
    Typeface titleFont;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);
        profileToolbar = (Toolbar) findViewById(R.id.toolbar_of_profile);
        toolbarTitle = (TextView)findViewById(R.id.profile_toolbar_title);
        titleFont= Typeface.createFromAsset(getAssets(), "fonts/AnkePrint.ttf");
        toolbarTitle.setTypeface(titleFont);

        alName = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
//        Take your won images for your app and give drawable path as below to arraylist
        alImage = new ArrayList<>(Arrays.asList(R.mipmap.ic_app_icon, R.mipmap.ic_app_icon, R.mipmap.ic_app_icon, R.mipmap.ic_app_icon));

        hlv = (HorizontalListView) findViewById(R.id.hlvProfile);
        hlvAdapter = new HLVAdapter(Profile.this, alName, alImage);
        hlv.setAdapter(hlvAdapter);

        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(Profile.this, "You clicked on : " + alName.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void blitzoneFromProfileButtonCallback (View view)
    {
        finish();
    }

    public void notificationsFromProfileButtonCallback (View view)
    {
        Intent intent = new Intent(this, Notifications.class);

        startActivity(intent);
    }

    public void takePhotoCallback (View view)
    {
        Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    public void disconnectCallback (View view)
    {

        Intent intent = new Intent(this, LogIn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        JWTManager jwtManager = new JWTManager(getApplicationContext());
        jwtManager.delToken();

        startActivity(intent);
    }
}

