package com.example.bsaraci.blitzone;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class profileHorizontalPhotosProvider {
    private String mChapter;
    private Bitmap mBitpmap;

    public profileHorizontalPhotosProvider(String mChapter, Bitmap mBitpmap) {
        this.mChapter = mChapter;
        this.mBitpmap = mBitpmap;
    }

    public String getmChapter() {
        return mChapter;
    }

    public Bitmap getmBitpmap() {
        return mBitpmap;
    }

    public void setmBitpmap(Bitmap mBitpmap) {
        this.mBitpmap = mBitpmap;
    }

    public void setmChapter(String mChapter) {
        this.mChapter = mChapter;
    }
}
