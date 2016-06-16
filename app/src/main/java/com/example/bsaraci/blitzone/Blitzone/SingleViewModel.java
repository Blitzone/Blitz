package com.example.bsaraci.blitzone.Blitzone;

import android.graphics.Bitmap;

public class SingleViewModel {
    private Bitmap image;
    private String chapter;

    public SingleViewModel(Bitmap image, String chapter) {
        this.image = image;
        this.chapter = chapter;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
