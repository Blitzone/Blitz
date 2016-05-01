package com.example.bsaraci.blitzone.Blitzone;

public class ViewDataProvider {

    private int profilePicture;
    private String username;
    private String points;
    private int blitz;
    private String challengeOfTheDay;
    private String hour;

    public ViewDataProvider(int profilePicture, String username, String points, int blitz, String challengeOfTheDay, String hour) {
        this.setProfilePicture(profilePicture);
        this.setUsername(username);
        this.setPoints(points);
        this.setBlitz(blitz);
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

    public String getChallengeOfTheDay() { return challengeOfTheDay; }

    public String getHour() { return hour; }

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

    public void setChallengeOfTheDay(String challengeOfTheDay) { this.challengeOfTheDay = challengeOfTheDay; }

    public void setHour(String hour) { this.hour = hour; }
}
