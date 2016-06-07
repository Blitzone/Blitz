package com.example.bsaraci.blitzone.Search;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.bsaraci.blitzone.Profile.PhotoChapter;
import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable{

    Context c;
    ArrayList<SearchModel> users,filterList;
    CustomFilter filter;


    public SearchAdapter(Context ctx,ArrayList<SearchModel> users)
    {
        this.c=ctx;
        this.users=users;
        this.filterList=users;
    }


    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_model,null);

        SearchViewHolder holder=new SearchViewHolder(v);

        return holder;
    }

    //DATA BOUND TO VIEWS
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {

        //BIND DATA
        holder.posTxt.setText(users.get(position).getPos());
        holder.nameTxt.setText(users.get(position).getUser().getUsername());
        String url = (users.get(position).getUser().getProfilePictureUrl());
        String username = (users.get(position).getUser().getUsername());
        if (url!= null){
            users.get(position).getUser().loadProfilePicture(c,url,holder.img);
        }

        else
        {
            holder.img.setImageResource(R.color.boldGray);
        }


        //IMPLEMENT CLICK LISTENER
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Snackbar.make(v, users.get(pos).getUser().getUsername(), Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    public void loadWithGlide(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    public void setBitmapWithGlide(Context context, String url, final User u){
        Glide
                .with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        u.setProfilePicture(resource);
                    }
                });
    }


    //GET TOTAL NUM OF PLAYERS
    @Override
    public int getItemCount() {
        return users.size();
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList,this);
        }

        return filter;
    }
}