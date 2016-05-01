package com.example.bsaraci.blitzone;

public class ProfilePhotosProvider {
    private Chapter mChapter;
    private PhotoChapter mBitmap;

    public ProfilePhotosProvider(Chapter mChapter, PhotoChapter mBitmap) {
        this.mChapter = mChapter;
        this.mBitmap = mBitmap;
    }

    public ProfilePhotosProvider(Chapter mChapter)
    {
        this.mChapter = mChapter;
    }

    public ProfilePhotosProvider(PhotoChapter mBitmap)
    {
        this.mBitmap = mBitmap;
    }

    public Chapter getmChapter() {
        return mChapter;
    }

    public PhotoChapter getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(PhotoChapter mBitmap) {
        this.mBitmap = mBitmap;
    }

    public void setmChapter(Chapter mChapter) {
        this.mChapter = mChapter;
    }
}
