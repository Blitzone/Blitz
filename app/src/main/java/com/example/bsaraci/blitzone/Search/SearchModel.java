package com.example.bsaraci.blitzone.Search;

public class SearchModel {

    private User user;      //User
    private String add;     //Add text for the button
    private String remove;  //Remove text for the button

    //EMPTY CONSTRUCTOR
    public SearchModel() {
    }

    //CONSTRUCTOR WITH User, add AND remove
    public SearchModel(User user, String add, String remove) {
        this.user = user;
        this.add = add;
        this.remove=remove;
    }

    //GETTER FOR Add
    public String getAdd() {
        return add;
    }

    //SETTER FOR Add
    public void setAdd(String add) {
        this.add = add;
    }

    //GETTER FOR User
    public User getUser() {
        return user;
    }

    //SETTER FOR User
    public void setUser(User user) {
        this.user = user;
    }

    //GETTER FOR Remove
    public String getRemove() {return remove;}

    //SETTER FOR Remove
    public void setRemove(String remove) {this.remove = remove;}
}