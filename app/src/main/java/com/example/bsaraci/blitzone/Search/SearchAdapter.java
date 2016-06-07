package com.example.bsaraci.blitzone.Search;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

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
        String url = (users.get(position).getUser().getProfilePictureUrl());
        String username = (users.get(position).getUser().getUsername());
        if (url!= null){
            users.get(position).getUser().loadPicture(c, url, holder.img);
            holder.nameTxt.setText(username);
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

    //GET TOTAL NUM OF USERS
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