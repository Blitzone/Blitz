package com.example.bsaraci.blitzone.Search;

public class GridItem {

    private String url;     //The Url of the picture
    private User user;      //The User who posted that picture

    //CONSTRUCTOR WITH THR URL AND USER
    public GridItem(String url, User user) {
        super();
        this.url = url;
        this.user = user;
    }

    //EMPTY CONSTRUCTOR
    public GridItem() {}

    //GETTER FOR user
    public User getUser() {
        return user;
    }

    //SETTER FOR user
    public void setUser(User username) {
        this.user = username;
    }

    //GETTER FOR url
    public String getUrl() {return url;}

    //SETTER FOR url
    public void setUrl(String url) {
        this.url = url;
    }

}