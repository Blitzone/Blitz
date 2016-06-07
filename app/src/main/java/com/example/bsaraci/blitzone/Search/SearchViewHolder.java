package com.example.bsaraci.blitzone.Search;

/**
 * Created by bsaraci on 5/8/2016.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //OUR VIEWS
    ImageView img;
    TextView nameTxt,posTxt;

    ItemClickListener itemClickListener;


    public SearchViewHolder(View itemView) {
        super(itemView);

        this.img= (ImageView) itemView.findViewById(R.id.userImage);
        this.nameTxt= (TextView) itemView.findViewById(R.id.nameTxt);
        this.posTxt= (TextView) itemView.findViewById(R.id.posTxt);

        //Set the click of only one element of the row. In this case the add button
        posTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());

    }

    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }
}