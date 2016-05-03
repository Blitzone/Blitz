package com.example.bsaraci.blitzone.Profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

/**
 * Created by bsaraci on 5/2/2016.
 */
public class FullSizeImage extends AppCompatActivity {
    ImageView imageView;
    TextView chapterFullSize;
    TextView usernameFullsize;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_size_image_main);
        initView();
    }

    private void initView (){
        imageView = (ImageView)findViewById(R.id.fullSizeImage);
        chapterFullSize =(TextView)findViewById(R.id.chapterFullSize);
        usernameFullsize =(TextView)findViewById(R.id.usernameFullSize);
        byte[] byteArray = getIntent().getByteArrayExtra("bitmapArray");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String chapterName = getIntent().getExtras().getString("chapterName");
        String username = getIntent().getExtras().getString("username");
        imageView.setImageBitmap(bmp);
        chapterFullSize.setText(chapterName);
        usernameFullsize.setText(username);
    }

    public void profileFromFullSize (View view)
    {
        finish();
    }
}
