package com.example.bsaraci.blitzone.Search;

/**This class takes all the properties of an user. It appears in many classes because it is the basic object of our app.
* Its characteristics we will take from the server and affect to an user by using the setters. Mostly we will use the empty
* constructor because we don't always know the properties for an user.
***************************************************************************************************************************
* BUGS : NO BUGS FOR THE MOMENT
***************************************************************************************************************************
* AMELIORATION : MAYBE WE WILL HAVE TO ADD OTHER METHODS IF NEEDED*/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.bsaraci.blitzone.R;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Comparable<User> {

    private Bitmap profilePicture;              //Bitmap for the profile picture
    private String username;                    //String for username
    private String profilePictureUrl;           //String for Url of the profile picture
    private Integer blitz;                      //Integer for number of blitzes
    private ArrayList <User> followedUserList;  //ArrayList for the followed users
    private boolean following;                  //boolean that tells if you're following an user or not
    private Integer primaryKey;                 //the key that identifies an user

/**
    EMPTY CONSTRUCTOR
*/
    public User() {}

/**
    CONSTRUCTOR
    @param username, the username of the user
*/
    @SuppressWarnings("unused")
    public User(String username) {
        this.username = username;
    }

/**
    GETTER FOR followedUserList
    @return an ArrayList of followed users
*/
    @SuppressWarnings("unused")
    public ArrayList<User> getFollowedUserList() {
        return followedUserList;
    }

/**
    SETTER FOR followedUserList
    @param followedUserList, the new ArrayList of users that we want to affect to followedUserList
*/
    @SuppressWarnings("unused")
    public void setFollowedUserList(ArrayList<User> followedUserList) {
        this.followedUserList = followedUserList;
    }

/**
    GETTER FOR profilePicture
    @return the profile picture of the user
*/
    public Bitmap getProfilePicture() {
        return profilePicture;
    }

/**
    GETTER FOR following
    @return true if you follow this user. False if not
*/
    public boolean isFollowing() {
        return following;
    }

/**
    SETTER FOR following
    @param following, a boolean we set to true of false to say whether if we follow the user or not
*/
    public void setFollowing(boolean following) {
        this.following = following;
    }

/**
    GETTER FOR profilePicture WITH CONTEXT AND URL IN PARAMETER
    @param context, the context where the user appears
    @param url, the url of the picture in our server
    @return the profile picture that is found in this url
*/
    @SuppressWarnings("unused")
    public Bitmap getProfilePicture(Context context, String url) {
        return profilePicture;
    }

/**
    GETTER FOR username
    @return the username of the user
*/
    public String getUsername() {
        return username;
    }

/**
    GETTER FOR blitz
    @return the number of blitzes the user has
*/
    public Integer getBlitz() {
        return blitz;
    }

/**
    GETTER FOR profilePictureUrl
    @return the url of the profile picture for the user
*/
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

/**
    SETTER FOR profilePicture
    @param profilePicture, the profile picture we want to set for the user
*/
    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

/**
    SETTER FOR blitz
    @param blitz, the number of blitzes we want to set for a user
*/
    public void setBlitz(Integer blitz) {
        this.blitz = blitz;
    }

/**
    SETTER FOR username
    @param username, the username we want to set for the user
*/
    public void setUsername(String username) {
        this.username = username;
    }

/**
    SETTER FOR profilePictureUrl
    @param profilePictureUrl, the profile picture url we want to set for the user
*/
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

/**
    METHOD THAT LOADS A PICTURE IF YOU PUT THE URL. LOADS IT WITH GLIDE
    @param c, the context we are loading the photo
    @param url, the url where the photo is found
    @param imageView, the image view where we want to put the loaded photo
*/
    public void loadPicture(final Context c, String url, ImageView imageView){
        Glide.with(c)                                                   //Put the context
                .load(url)                                              //Url where u want the photo to load from
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)            //DiskCacheStrategy
                .placeholder(R.color.lightGray)                         //Before loading what is going to show
                .dontAnimate()                                          //No Animation
                .into(new GlideDrawableImageViewTarget(imageView) {     //Where you put it

                    //When the photo is ready
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        //never called
                    }

                    //If loading failed
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        //never called
                    }
                });
    }

/**
    GETTER FOR primaryKey
    @return the key related to the user
*/
    public Integer getPrimaryKey() {
        return primaryKey;
    }

/**
    SETTER FOR primaryKey
    @param primaryKey, the new key we want to affect to an user
*/
    public void setPrimaryKey(Integer primaryKey) {
        this.primaryKey = primaryKey;
    }

/**
    METHOD THAT COMPARES AN USER BY HIS BLITZES
    @param user, the user we want to compare with the actual user
    @return 1 if user has more blitzes than the actual user, -1 if he has less, 0 if they have the same number of blitzes
*/
    @Override
    public int compareTo(@NonNull User user) {
        return user.blitz - this.blitz;
    }

/**
    METHOD THAT ORDERS A LIST BY THE COMPARISON WE WANT
    @param users, the ArrayList we want to reorder
*/
    public static void order(ArrayList <User> users){
        Collections.sort(users);
    }
}
