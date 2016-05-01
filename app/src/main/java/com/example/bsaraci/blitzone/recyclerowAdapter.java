package com.example.bsaraci.blitzone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerowAdapter extends  RecyclerView.Adapter<RecyclerowAdapter.ViewHolder> {

    List<RowDataProvider> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public ImageView mProfile;
        public TextView mUsername;
        public TextView mPoints;
        public ImageView mBlitz;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProfile= (ImageView) view.findViewById(R.id.profile_picture);
            mUsername = (TextView) view.findViewById(R.id.name);
            mPoints= (TextView) view.findViewById(R.id.points);
            mBlitz = (ImageView) view.findViewById(R.id.blitz);

        }
    }

    public RecyclerowAdapter(List<RowDataProvider> list) {

        this.list=list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blitzone_row_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        RowDataProvider listDataProvider = list.get(position);
        holder.mProfile.setImageResource(listDataProvider.getProfilePicture());
        holder.mUsername.setText(listDataProvider.getUsername());
        holder.mBlitz.setImageResource(listDataProvider.getBlitz());
        holder.mPoints.setText(listDataProvider.getPoints());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

