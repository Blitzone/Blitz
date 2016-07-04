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
    private ArrayList <SingleViewModel> allTopicPhotos;

    public ViewDataProvider(){

    }

    public ArrayList<SingleViewModel> getAllTopicPhotos() {
        return allTopicPhotos;
    }

    public void setAllTopicPhotos(ArrayList<SingleViewModel> allTopicPhotos) {
        this.allTopicPhotos = allTopicPhotos;
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
