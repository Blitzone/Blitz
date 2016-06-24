package com.example.bsaraci.blitzone.Search;

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

    //EMPTY CONSTRUCTOR
    public User() {}

    //CONSTRUCTOR WITH USERNAME
    public User(String username) {
        this.username = username;
    }

    //GETTER FOR followedUserList
    public ArrayList<User> getFollowedUserList() {
        return followedUserList;
    }

    //SETTER FOR followedUserList
    public void setFollowedUserList(ArrayList<User> followedUserList) {
        this.followedUserList = followedUserList;
    }

    //GETTER FOR profilePicture
    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    //GETTER FOR following
    public boolean isFollowing() {
        return following;
    }

    //SETTER FOR following
    public void setFollowing(boolean following) {
        this.following = following;
    }

    //GETTER FOR profilePicture WITH CONTEXT AND URL IN PARAMETER
    public Bitmap getProfilePicture(Context context, String url) {
        return profilePicture;
    }

    //GETTER FOR username
    public String getUsername() {
        return username;
    }

    //GETTER FOR blitz
    public Integer getBlitz() {
        return blitz;
    }

    //GETTER FOR profilePictureUrl
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    //SETTER FOR profilePicture
    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    //SETTER FOR blitz
    public void setBlitz(Integer blitz) {
        this.blitz = blitz;
    }

    //SETTER FOR username
    public void setUsername(String username) {
        this.username = username;
    }

    //SETTER FOR profilePictureUrl
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    //METHOD THAT LOADS A PICTURE IF YOU PUT THE URL. LOADS IT WITH GLIDE
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

    //METHOD THAT COMPARES AN USER BY HIS BLITZES
    @Override
    public int compareTo(@NonNull User user) {
        return user.blitz - this.blitz;
    }

    //METHOD THAT ORDERS A LIST BY THE COMPARISON WE WANT
    public static void order(ArrayList <User> users){
        Collections.sort(users);
    }
}
