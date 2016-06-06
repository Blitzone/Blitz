package com.example.bsaraci.blitzone.Search;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by bsaraci on 6/7/2016.
 */
public class User {
    private Bitmap profilePicture;
    private String username;
    private String profilePictureUrl;
    private int blitz;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public int getBlitz() {
        return blitz;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setBlitz(int blitz) {
        this.blitz = blitz;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
