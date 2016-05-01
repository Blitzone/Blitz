package com.example.bsaraci.blitzone.Profile;

import com.example.bsaraci.blitzone.Profile.Profile;

public class ProfilePhotosProvider {
    private Profile.Chapter chapter;
    private Profile.PhotoChapter photoChapter;

    public ProfilePhotosProvider(Profile.Chapter chapter, Profile.PhotoChapter photoChapter) {
        this.chapter = chapter;
        this.photoChapter = photoChapter;
    }

    public ProfilePhotosProvider(Profile.Chapter chapter)
    {
        this.chapter = chapter;
    }

    public ProfilePhotosProvider(Profile.PhotoChapter photoChapter)
    {
        this.photoChapter = photoChapter;
    }

    public Profile.Chapter getChapter() {
        return chapter;
    }

    public Profile.PhotoChapter getPhotoChapter() {
        return photoChapter;
    }

    public void setPhotoChapter(Profile.PhotoChapter mPhotoChapter) {
        this.photoChapter = mPhotoChapter;
    }

    public void setChapter(Profile.Chapter mChapter) {
        this.chapter = mChapter;
    }
}
