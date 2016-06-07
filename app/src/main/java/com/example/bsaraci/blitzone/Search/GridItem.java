package com.example.bsaraci.blitzone.Search;

import android.graphics.Bitmap;

import com.example.bsaraci.blitzone.Profile.PhotoChapter;

import java.net.URL;

/**
 * Created by bsaraci on 5/9/2016.
 */
public class GridItem {
    private String url;
    private User user;

    public GridItem(String url, User user) {
        super();
        this.url = url;
        this.user = user;
    }

    public GridItem() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User username) {
        this.user = username;
    }

    public String getUrl() {return url;}

    public void setUrl(String url) {
        this.url = url;
    }

}