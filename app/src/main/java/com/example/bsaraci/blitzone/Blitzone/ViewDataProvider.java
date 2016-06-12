package com.example.bsaraci.blitzone.Blitzone;

public class ViewDataProvider {

    private int profilePicture;
    private String username;
    private String points;
    private int blitz;
    private int blitzClicked;
    private int like;
    private int likeClicked;
    private int dislike;
    private int dislikeClicked;
    private String challengeOfTheDay;
    private String hour;

    public ViewDataProvider(int profilePicture, String username, String points, int blitz, int blitzClicked,int like, int likeClicked, int dislike,int dislikeClicked, String challengeOfTheDay, String hour) {
        this.setProfilePicture(profilePicture);
        this.setUsername(username);
        this.setPoints(points);
        this.setBlitz(blitz);
        this.setBlitzClicked(blitzClicked);
        this.setChallengeOfTheDay(challengeOfTheDay);
        this.setHour(hour);
    }

    public int getProfilePicture() {
        return profilePicture;
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

    public String getChallengeOfTheDay() { return challengeOfTheDay; }

    public String getHour() { return hour; }

    public void setProfilePicture(int profilePicture) {this.profilePicture = profilePicture;}

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

    public void setPoints(String points) {
        this.points = points;
    }

    public void setChallengeOfTheDay(String challengeOfTheDay) { this.challengeOfTheDay = challengeOfTheDay; }

    public void setHour(String hour) { this.hour = hour; }
}
