package com.example.bsaraci.blitzone.Profile;

import com.example.bsaraci.blitzone.Profile.Profile;

public class ProfilePhotosProvider {
    private Chapter chapter;
    private PhotoChapter photoChapter;

    public ProfilePhotosProvider(Chapter chapter, PhotoChapter photoChapter) {
        this.chapter = chapter;
        this.photoChapter = photoChapter;
    }

    public ProfilePhotosProvider(Chapter chapter)
    {
        this.chapter = chapter;
    }

    public ProfilePhotosProvider(PhotoChapter photoChapter)
    {
        this.photoChapter = photoChapter;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public PhotoChapter getPhotoChapter() {
        return photoChapter;
    }

    public void setPhotoChapter(PhotoChapter mPhotoChapter) {
        this.photoChapter = mPhotoChapter;
    }

    public void setChapter(Chapter mChapter) {
        this.chapter = mChapter;
    }
}
