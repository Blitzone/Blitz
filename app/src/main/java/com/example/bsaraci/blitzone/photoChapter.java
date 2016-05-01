package com.example.bsaraci.blitzone;

import android.graphics.Bitmap;


/**
 * Created by bsaraci on 5/1/2016.
 */
public class PhotoChapter {

    private Bitmap bitmap;

    public PhotoChapter(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public PhotoChapter() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

