package com.example.bsaraci.blitzone.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

/**
 * Created by bsaraci on 5/2/2016.
 */
public class FullSizeImage extends AppCompatActivity {
    ImageView imageView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageView = (ImageView)findViewById(R.id.fullSizeImage);
        setContentView(R.layout.full_size_image_main);

        Intent intent= getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {
            Bitmap bitmap =(Bitmap) b.get("bitmap");
            imageView.setImageBitmap(bitmap);
        }
}
}
