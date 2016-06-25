package com.example.bsaraci.blitzone.Search;

/**This class represents the model of the grid view. It has an user and a photo url that we will take from the server
* and then affect by using the setters.
*********************************************************************************************************************
* BUGS : NO BUGS FOR THE MOMENT
*********************************************************************************************************************
* AMELIORATION : NO AMELIORATIONS FOR THE MOMENT*/

public class GridItem {

    private String url;     //The Url of the picture
    private User user;      //The User who posted that picture

/**
    CONSTRUCTOR
    @param url, the url of the photo
    @param user, the user who posted a photo
*/
    @SuppressWarnings("unused")
    public GridItem(String url, User user) {
        super();
        this.url = url;
        this.user = user;
    }

/**
    EMPTY CONSTRUCTOR
*/
    public GridItem() {}

/**
    GETTER FOR user
    @return the user who is related to the grid element
*/
    public User getUser() {
        return user;
    }

/**
    SETTER FOR user
    @param user, the user we want to set for ou grid element
*/
    public void setUser(User user) {
        this.user = user;
    }

/**
    GETTER FOR url
    @return the url we want to associate to the grid element
*/
    public String getUrl() {return url;}

/**
    SETTER FOR url
    @param url, the url we want to set for the grid thumb image
*/
    public void setUrl(String url) {
        this.url = url;
    }

}