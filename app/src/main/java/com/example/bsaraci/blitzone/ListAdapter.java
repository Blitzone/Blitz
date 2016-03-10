package com.example.bsaraci.blitzone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ListAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler{

        ImageView profilePicture;
        TextView username;
        TextView points;
        ImageView blitz;
    }

        public void add(Object object){
            super.add(object);
            list.add(object);
        }
        public int getCount(){
            return this.list.size();
        }

        @Override
        public Object getItem(int position) {
            return this.list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            row=convertView;
            DataHandler handler;
            if(convertView==null)
            {
                LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.blitzone_row_content,parent,false);
                handler=new DataHandler();
                handler.profilePicture=(ImageView)row.findViewById(R.id.profile_picture);
                handler.username=(TextView)row.findViewById(R.id.name);
                handler.points=(TextView)row.findViewById(R.id.points);
                handler.blitz=(ImageView)row.findViewById(R.id.blitz);
                row.setTag(handler);
            }
            else {

                handler=(DataHandler)row.getTag();
            }
            ListDataProvider dataProvider;

            dataProvider=(ListDataProvider)this.getItem(position);
            handler.profilePicture.setImageResource(dataProvider.getProfilePicture());
            handler.username.setText(dataProvider.getUsername());
            handler.points.setText(dataProvider.getPoints());
            handler.blitz.setImageResource(dataProvider.getBlitz());
            return row;
        }

}
