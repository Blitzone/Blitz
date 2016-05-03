package com.example.bsaraci.blitzone.Profile;

import android.graphics.Bitmap;


/**
 * Created by bsaraci on 5/1/2016.
 */
public class PhotoChapter {

    private Integer chapterId;
    private String chapterName;
    private Bitmap photo;
    private String url;

    public PhotoChapter(Integer chapterId, Bitmap photo)
    {
        this.chapterId = chapterId;
        this.photo = photo;
    }

    public PhotoChapter(Integer chapterId)
    {
        this.chapterId = chapterId;
    }

    public PhotoChapter(Integer chapterId, String chapterName)
    {
        this.chapterId = chapterId;
        this.chapterName = chapterName;
    }

    public PhotoChapter(Bitmap photo) {
        this.photo = photo;
    }

    public PhotoChapter(String url, Bitmap photo) {
        this.url = url;
        this.photo = photo;
    }

    public PhotoChapter() {}

    public void setUrl(String url) {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public Bitmap getPhoto() {
        return photo;

    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
}

