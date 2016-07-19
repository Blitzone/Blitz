package com.example.bsaraci.blitzone.Search;

/**This is the Adapter of the Grid View. It handles all its behaviour. It is nearly the same logic as the RecycleView adapter.
* For this class we need to know the context where it is going to be shown , the xml file that represents the design of a grid item
* and one ArrayList data whose type is GridItem. We call this class in GridViewSearch.
*********************************************************************************************************************************
* BUGS : NO BUGS FOR THIS MOMENT
*********************************************************************************************************************************
* AMELIORATION : NO AMELIORATIONS FOR THE MOMENT*/

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.bsaraci.blitzone.Blitzone.RowDataProvider;
import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewHolder>  {

    private Context context;                //The context that the gridAdapter is called
    private ArrayList <GridItem> data;      //An ArrayList of GridItems

/**
    CONSTRUCTOR
    @param context, that the adapter is being created
    @param data, data of our grid
*/
    public GridViewAdapter(Context context, ArrayList<GridItem> data) {

        this.context = context;
        this.data = data;
    }

    public void setList( ArrayList<GridItem> data) {
        this.data = data;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item, null);
        return new GridViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        String url = data.get(position).getUrl();
        if(url!=null){
            data.get(position).getUser().loadPicture(context,url,holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
