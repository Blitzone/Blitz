package com.example.bsaraci.blitzone.Blitzone;

import java.util.ArrayList;

public class ViewDataProvider {

    private String username;
    private String points;
    private int blitz;
    private int blitzClicked;
    private int like;
    private int likeClicked;
    private int dislike;
    private int dislikeClicked;
    private ArrayList <SingleViewModel> allTopicPhotos;

    public ViewDataProvider(){

    }

    public ViewDataProvider(String username, String points, int blitz, int blitzClicked,int like, int likeClicked, int dislike,int dislikeClicked, ArrayList<SingleViewModel> allTopicPhotos) {
        this.setUsername(username);
        this.setPoints(points);
        this.setBlitz(blitz);
        this.setBlitzClicked(blitzClicked);
        this.setLike(like);
        this.setLikeClicked(likeClicked);
        this.setDislike(dislike);
        this.setDislikeClicked(dislikeClicked);
        this.setAllTopicPhotos(allTopicPhotos);
    }

    public ArrayList<SingleViewModel> getAllTopicPhotos() {
        return allTopicPhotos;
    }

    public void setAllTopicPhotos(ArrayList<SingleViewModel> allTopicPhotos) {
        this.allTopicPhotos = allTopicPhotos;
    }

    public String getUsername() {
        return username;
    }

    public String getPoints() {
        return points;
    }

    public int getBlitz() {
        return blitz;
    }

    public int getBlitzClicked () {return blitzClicked;}

    public int getLike() {return like;}

    public int getLikeClicked() {return likeClicked;}

    public int getDislike() {return dislike;}

    public int getDislikeClicked() {return dislikeClicked;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBlitz(int blitz) {
        this.blitz = blitz;
    }

    public void setBlitzClicked(int blitzClicked){this.blitzClicked = blitzClicked;}

    public void setLike(int like) {this.like = like;}

    public void setLikeClicked(int likeClicked) {this.likeClicked = likeClicked;}

    public void setDislike(int dislike) {this.dislike = dislike;}

    public void setDislikeClicked(int dislikeClicked) {this.dislikeClicked = dislikeClicked;}

    public void setPoints(String points) {this.points = points;}

}
