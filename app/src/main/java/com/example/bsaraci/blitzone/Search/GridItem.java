package com.example.bsaraci.blitzone.Search;

/**
 * Created by bsaraci on 5/9/2016.
 */
public class GridItem {
    private int image;
    private String username;

    public GridItem(int image, String username) {
        super();
        this.image = image;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int  getImage() {
        return image;
    }

    public void setImage(int  image) {
        this.image = image;
    }

}