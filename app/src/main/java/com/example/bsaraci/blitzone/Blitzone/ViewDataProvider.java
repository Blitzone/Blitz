package com.example.bsaraci.blitzone.Blitzone;

import com.example.bsaraci.blitzone.Search.User;

import java.util.ArrayList;

public class ViewDataProvider {

    private User user;
    private int blitz;
    private int blitzClicked;
    private int like;
    private int likeClicked;
    private int dislike;
    private int dislikeClicked;
    private String blitzesText;
    private ArrayList <SingleViewModel> photoChapters;

    public ViewDataProvider(){

    }

    public ArrayList<SingleViewModel> getPhotoChapters() {
        return photoChapters;
    }

    public void setPhotoChapters(ArrayList<SingleViewModel> photoChapters) {
        this.photoChapters = photoChapters;
    }

    public String getBlitzesText() {
        return blitzesText;
    }

    public void setBlitzesText(String blitzesText) {
        this.blitzesText = blitzesText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getBlitz() {
        return blitz;
    }

    public int getBlitzClicked () {return blitzClicked;}

    public int getLike() {return like;}

    public int getLikeClicked() {return likeClicked;}

    public int getDislike() {return dislike;}

    public int getDislikeClicked() {return dislikeClicked;}

    public void setBlitz(int blitz) {
        this.blitz = blitz;
    }

    public void setBlitzClicked(int blitzClicked){this.blitzClicked = blitzClicked;}

    public void setLike(int like) {this.like = like;}

    public void setLikeClicked(int likeClicked) {this.likeClicked = likeClicked;}

    public void setDislike(int dislike) {this.dislike = dislike;}

    public void setDislikeClicked(int dislikeClicked) {this.dislikeClicked = dislikeClicked;}


}
