package com.example.bsaraci.blitzone.Blitzone;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

import java.util.List;

public class RecyclerowAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<RowDataProvider> list;
    private Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public RecyclerowAdapter(Context context, List<RowDataProvider> list, RecyclerView recyclerView) {
        this.context = context;
        this.list=list;

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blitzone_row_content, parent, false);

            vh = new RowViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RowViewHolder) {
            RowDataProvider listDataProvider = list.get(position);
            ((RowViewHolder)holder).mProfile.setImageBitmap(listDataProvider.getUser().getProfilePicture());
            ((RowViewHolder)holder).mUsername.setText(listDataProvider.getUser().getUsername());
        String pts = listDataProvider.getUser().getBlitz().toString();
            ((RowViewHolder)holder).mPoints.setText(pts);
        if(listDataProvider.getUser().isFollowing()){
            ((RowViewHolder)holder).mBlitz.setImageResource(R.mipmap.ic_orange_blitz);
        }
        else{
            ((RowViewHolder)holder).mBlitz.setImageResource(0);
        }
        }
        else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public List<RowDataProvider> getItems() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder {


        public View mView;

        public ImageView mProfile;
        public TextView mUsername;
        public TextView mPoints;
        public ImageView mBlitz;


        public RowViewHolder(View view) {
            super(view);
            mView = view;
            mProfile= (ImageView) view.findViewById(R.id.profile_picture);
            mUsername = (TextView) view.findViewById(R.id.name);
            mPoints= (TextView) view.findViewById(R.id.points);
            mBlitz = (ImageView) view.findViewById(R.id.blitz);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}


