package com.example.bsaraci.blitzone.Blitzone;


public class SingleViewModel {
    private String chapterPhotoUrl;
    private String chapterName;

    public SingleViewModel(){}

    public SingleViewModel(String chapterPhotoUrl, String chapterName) {
        this.chapterPhotoUrl = chapterPhotoUrl;
        this.chapterName = chapterName;
    }

    public String getChapterPhotoUrl() {
        return chapterPhotoUrl;
    }

    public void setChapterPhotoUrl(String chapterPhotoUrl) {
        this.chapterPhotoUrl = chapterPhotoUrl;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
}
