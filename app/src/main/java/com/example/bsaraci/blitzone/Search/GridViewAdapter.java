package com.example.bsaraci.blitzone.Search;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by bsaraci on 5/9/2016.
 */
public class GridViewAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResourceId;
    private ArrayList <GridItem> data ;

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<GridItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = data.get(position);
        String url = item.getUrl();

        if(url!=null){
            item.getUser().loadPicture(context,url,holder.image);
        }
        else {
            holder.image.setImageResource(R.color.boldGray);
        }
        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
}