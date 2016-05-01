package com.example.bsaraci.blitzone.Profile;

import android.graphics.Bitmap;


/**
 * Created by bsaraci on 5/1/2016.
 */
public class PhotoChapter {

    private Bitmap bitmap;
    private String url;
    private boolean _hasPhoto = false;
    private boolean _urlUpdated = false;

    public boolean is_urlUpdated() {
        return _urlUpdated;
    }

    public void set_urlUpdated(boolean _urlUpdated) {
        this._urlUpdated = _urlUpdated;
    }

    public PhotoChapter(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public PhotoChapter(String url, Bitmap bitmap) {
        this.url = url;
        this.bitmap = bitmap;
    }

    public PhotoChapter(String url, Bitmap bitmap, boolean _hasPhoto) {
        this.url = url;
        this.bitmap = bitmap;
        this._hasPhoto = _hasPhoto;
    }

    public PhotoChapter() {}

    public void setUrl(String url) {
        this.url = url;
        this.set_urlUpdated(true);
    }

    public void set_hasPhoto(boolean _hasPhoto) {
        this._hasPhoto = _hasPhoto;
    }

    public boolean _hasPhoto() {

        return _hasPhoto;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBitmap() {
        return bitmap;

    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.set_hasPhoto(true);
    }
}

