package com.example.bsaraci.blitzone.Search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.bsaraci.blitzone.Profile.PhotoChapter;
import com.example.bsaraci.blitzone.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class User implements Comparable<User> {
    private Bitmap profilePicture;
    private String username;
    private String profilePictureUrl;
    private Integer blitz;
    private ArrayList <User> userList;
    private boolean following;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public Bitmap getProfilePicture(Context context, String url) {
        return profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public Integer getBlitz() {
        return blitz;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setBlitz(Integer blitz) {
        this.blitz = blitz;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }


    public void loadPicture(final Context c, String url, ImageView imageView){
        Glide.with(c)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.color.lightGray)
                .dontAnimate()
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        //never called
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        //never called
                    }
                });
    }

    @Override
    public int compareTo(@NonNull User user) {
        return user.blitz - this.blitz;
    }

    public static void order(ArrayList <User> users){
        Collections.sort(users);
    }
}
