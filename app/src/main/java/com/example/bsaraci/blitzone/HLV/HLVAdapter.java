package com.example.bsaraci.blitzone.HLV;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;

public class HLVAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;

    ArrayList<String> alName;
    ArrayList<Bitmap> alImage;

    public HLVAdapter(Context context, ArrayList<String> alName, ArrayList<Bitmap> alImage) {

        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;
    }

    @Override
    public int getCount() {
        return alName.size();
    }

    @Override
    public Object getItem(int position) {
        return alName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.hlv_inflate, parent, false);
            holder = new ViewHolder();
            holder.imgThumbnail = (ImageView) view.findViewById(R.id.img_thumbnail);
            holder.tvSpecies = (TextView) view.findViewById(R.id.photo_chapter);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.tvSpecies.setText(alName.get(position));
        holder.imgThumbnail.setImageBitmap(alImage.get(position));
        return view;
    }

    private class ViewHolder {
        public ImageView imgThumbnail;
        public TextView tvSpecies;
    }
}