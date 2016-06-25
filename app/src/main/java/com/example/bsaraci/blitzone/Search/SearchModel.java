package com.example.bsaraci.blitzone.Search;

/**This class defines the data for the row of our search user list. It is related to classes Search, SearchAdapter
* It has the getters and the setters for user , add, remove. If we want to create a new SearchModel we have two constructors.
* One is empty the other one is not. We will rarely use the second one because every time we need a user we will set its properties
* from the server response.
**********************************************************************************************************************************
* BUGS : NO BUGS DETECTED FOR THE MOMENT
**********************************************************************************************************************************
* AMELIORATION : NO AMELIORATION DETECTED FOR THE MOMENT */

public class SearchModel {

    private User user;      //User
    private String add;     //Add text for the button
    private String remove;  //Remove text for the button

/**
    EMPTY CONSTRUCTOR
*/
    public SearchModel() {
    }

/**
    CONSTRUCTOR
    @param user, the user with all its properties
    @param add, the text of add button
    @param remove, the text of remove button

*/
    @SuppressWarnings("unused")
    public SearchModel(User user, String add, String remove) {
        this.user = user;
        this.add = add;
        this.remove=remove;
    }

/**
    GETTER FOR Add
    @return the text for the add button
*/
    public String getAdd() {
        return add;
    }

/**
    SETTER FOR Add
    @param add, the text we want to set for the add button
*/
    public void setAdd(String add) {
        this.add = add;
    }

/**
    GETTER FOR User
    @return the user that then we can put in the row of our search list
*/
    public User getUser() {
        return user;
    }

/**
    SETTER FOR User
    @param user, the user we want to set for the row of our search list
*/
    public void setUser(User user) {
        this.user = user;
    }

/**
    GETTER FOR Remove
    @return the text for the remove button
*/
    public String getRemove() {return remove;}

/**
    SETTER FOR Remove
    @param remove, the text we want to set for the remove button
*/
    public void setRemove(String remove) {this.remove = remove;}
}