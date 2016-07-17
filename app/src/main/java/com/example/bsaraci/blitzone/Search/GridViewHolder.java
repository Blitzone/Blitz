package com.example.bsaraci.blitzone.Search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.bsaraci.blitzone.R;

/**
 This class holds the views for a grid item
 */
public class GridViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    public GridViewHolder(View itemView){
        super(itemView);

        this.image= (ImageView) itemView.findViewById(R.id.image);                //Affects this xml element to img
    }
}