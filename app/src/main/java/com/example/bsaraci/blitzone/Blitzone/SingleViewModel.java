package com.example.bsaraci.blitzone.Blitzone;

public class SingleViewModel {
    private int image;
    private String chapter;
    private String hour;

    public SingleViewModel(int image, String chapter, String hour) {
        this.image = image;
        this.chapter = chapter;
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
