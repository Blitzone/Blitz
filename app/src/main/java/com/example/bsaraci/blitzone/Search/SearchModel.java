package com.example.bsaraci.blitzone.Search;

/**
 * Created by bsaraci on 5/8/2016.
 */
public class SearchModel {

    private User user;
    private String add;
    private String remove;

    public SearchModel() {
    }

    public SearchModel(User user, String add, String remove) {
        this.user = user;
        this.add = add;
        this.remove=remove;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRemove() {return remove;}

    public void setRemove(String remove) {this.remove = remove;}
}