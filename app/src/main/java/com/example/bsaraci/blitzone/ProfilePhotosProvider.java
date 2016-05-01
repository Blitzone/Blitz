package com.example.bsaraci.blitzone;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProfilePhotosProvider {
    private Chapter mChapter;
    private Bitmap mBitpmap;

    public ProfilePhotosProvider(Chapter mChapter, Bitmap mBitpmap) {
        this.mChapter = mChapter;
        this.mBitpmap = mBitpmap;
    }

    public ProfilePhotosProvider(Chapter mChapter)
    {
        this.mChapter = mChapter;
    }

    public Chapter getmChapter() {
        return mChapter;
    }

    public Bitmap getmBitpmap() {
        return mBitpmap;
    }

    public void setmBitpmap(Bitmap mBitpmap) {
        this.mBitpmap = mBitpmap;
    }

    public void setmChapter(Chapter mChapter) {
        this.mChapter = mChapter;
    }
}
