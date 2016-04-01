package com.example.bsaraci.blitzone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class recycleviewAdapter extends  RecyclerView.Adapter<recycleviewAdapter.ViewHolder> {

    List<viewDataProvider> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public ImageView mProfile;
        public TextView mUsername;
        public TextView mPoints;
        public ImageView mBlitz;
        public TextView mChallenge;
        public TextView mHour;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProfile= (ImageView) view.findViewById(R.id.profile_picture);
            mUsername = (TextView) view.findViewById(R.id.name);
            mPoints= (TextView) view.findViewById(R.id.points);
            mBlitz = (ImageView) view.findViewById(R.id.blitz);
            mChallenge = (TextView) view.findViewById(R.id.chapterDescription);
            mHour = (TextView) view.findViewById(R.id.timePublished);

        }
    }

    public recycleviewAdapter(List<viewDataProvider> list ) {

        this.list=list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blitzone_view_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        viewDataProvider viewDataProvider = list.get(position);
        holder.mProfile.setImageResource(viewDataProvider.getProfilePicture());
        holder.mUsername.setText(viewDataProvider.getUsername());
        holder.mBlitz.setImageResource(viewDataProvider.getBlitz());
        holder.mPoints.setText(viewDataProvider.getPoints());
        holder.mChallenge.setText(viewDataProvider.getChallengeOfTheDay());
        holder.mHour.setText(viewDataProvider.getHour());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clear() {

        list.clear();

        notifyDataSetChanged();

    }

    public void addAll(List<viewDataProvider> items) {

        list.addAll(items);

        notifyDataSetChanged();

    }
}

