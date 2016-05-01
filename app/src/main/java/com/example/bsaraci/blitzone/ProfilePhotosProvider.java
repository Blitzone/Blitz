package com.example.bsaraci.blitzone;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProfilePhotosProvider {
    private Chapter mChapter;
    private PhotoChapter mBitpmap;

    public ProfilePhotosProvider(Chapter mChapter, PhotoChapter mBitpmap) {
        this.mChapter = mChapter;
        this.mBitpmap = mBitpmap;
    }

    public ProfilePhotosProvider(Chapter mChapter)
    {
        this.mChapter = mChapter;
    }

    public ProfilePhotosProvider(PhotoChapter mBitpmap)
    {
        this.mBitpmap = mBitpmap;
    }

    public Chapter getmChapter() {
        return mChapter;
    }

    public PhotoChapter getmBitpmap() {
        return mBitpmap;
    }

    public void setmBitpmap(PhotoChapter mBitpmap) {
        this.mBitpmap = mBitpmap;
    }

    public void setmChapter(Chapter mChapter) {
        this.mChapter = mChapter;
    }
}
