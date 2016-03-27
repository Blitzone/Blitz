package com.example.bsaraci.blitzone;

public class rowDataProvider {

    private int profilePicture;
    private String username;
    private String points;
    private int blitz;

    public rowDataProvider(int profilePicture, String username, String points, int blitz) {
        this.setProfilePicture(profilePicture);
        this.setUsername(username);
        this.setPoints(points);
        this.setBlitz(blitz);
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

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBlitz(int blitz) {
        this.blitz = blitz;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
