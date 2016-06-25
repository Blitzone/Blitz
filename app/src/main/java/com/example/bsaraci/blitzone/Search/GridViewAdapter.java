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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {

    private Context context;                //The context that the gridAdapter is called
    private int layoutResourceId;           //The xml related to the grid item
    private ArrayList <GridItem> data;      //An ArrayList of GridItems

/**
    CONSTRUCTOR
    @param context, that the adapter is being created
    @param layoutResourceId, xml file that contents a grid item
    @param data, data of our grid
*/
    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<GridItem> data) {

        //noinspection unchecked
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

/**
    THIS METHOD GETS A View THAT DISPLAYS THE DATA AT TH SPECIFIED POSITION IN THE DATA SET
    @param position, of an item in the data
    @param convertView, old view to reuse, if possible
    @param parent, that this view will eventually be attached to
    @return a View corresponding to the data at the specified position.
*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;         //Row is the old view in the beginning
        ViewHolder holder;              //The ViewHolder holds the view of the grid item

        //Enters if we don't have any old view
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater(); //Creates the inflater
            row = inflater.inflate(layoutResourceId, parent, false);            //The view will now take the grid item view
            holder = new ViewHolder();                                          //holder is now a new ViewHolder
            holder.image = (ImageView) row.findViewById(R.id.image);            //We set the holder image as this xml element
            row.setTag(holder);                                                 //Sets the tag associated with this view
        }

        //Enters if we already have an old view
        else {

            holder = (ViewHolder) row.getTag();                                 //We create the holder by getting the tag of the old view
        }

        GridItem item = data.get(position);                                     //item is found at this position of ArrayList data
        String url = item.getUrl();                                             //Takes the url of the item

        //Enters if url already exists
        if(url!=null){

            item.getUser().loadPicture(context,url,holder.image);               //Loads the picture found in this url in holder.image
        }

        //Enters if we don't have an url
        else {

            holder.image.setImageResource(R.color.boldGray);                    //Loads a gray background
        }

        return row;
    }

/**
    This class holds the views for a grid item
*/
    static class ViewHolder {
        ImageView image;
    }
}