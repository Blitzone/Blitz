package com.example.bsaraci.blitzone.Blitzone;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsaraci.blitzone.Profile.RoundedImageView;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class RecycleviewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ViewDataProvider> list;
    private Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public RecycleviewAdapter(Context context,List<ViewDataProvider> list, RecyclerView recyclerView) {
        this.context = context;
        this.list=list;

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Log.i("amga", "nagato");
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blitzone_view_content, parent, false);

            vh = new DailyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DailyViewHolder) {

            ViewDataProvider viewDataProvider= (ViewDataProvider) list.get(position);

            ((DailyViewHolder) holder).mUsername.setText(viewDataProvider.getUsername());
            ((DailyViewHolder) holder).mBlitz.setImageResource(viewDataProvider.getBlitz());
            ((DailyViewHolder) holder).mBlitzClicked.setImageResource(viewDataProvider.getBlitzClicked());
            ((DailyViewHolder) holder).mLike.setImageResource(viewDataProvider.getLike());
            ((DailyViewHolder) holder).mLikeClicked.setImageResource(viewDataProvider.getLikeClicked());
            ((DailyViewHolder) holder).mDislike.setImageResource(viewDataProvider.getDislike());
            ((DailyViewHolder) holder).mDislikeClicked.setImageResource(viewDataProvider.getDislikeClicked());
            ((DailyViewHolder) holder).mPoints.setText(viewDataProvider.getPoints());

            ArrayList singleViewModels = list.get(position).getAllTopicPhotos();

            SingleViewModelAdapter singleViewModelAdapter = new SingleViewModelAdapter(context,singleViewModels);

            ((DailyViewHolder) holder).mRecyclerView.setHasFixedSize(true);
            ((DailyViewHolder) holder).mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ((DailyViewHolder) holder).mRecyclerView.setAdapter(singleViewModelAdapter);


                    ((DailyViewHolder) holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    if (v == ((DailyViewHolder) holder).mBlitz) {
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mBlitzClicked.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mBlitzClicked) {
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mBlitz.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mLike) {
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mDislike.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mLikeClicked.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mLikeClicked) {
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mDislike.setVisibility(View.VISIBLE);
                        ((DailyViewHolder) holder).mLike.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mDislike) {
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mLike.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mDislikeClicked.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mDislikeClicked) {
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mLike.setVisibility(View.VISIBLE);
                        ((DailyViewHolder) holder).mDislike.setVisibility(View.VISIBLE);
                    }
                }
            });

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public List<ViewDataProvider> getItems() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class DailyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {

        ItemClickListener itemClickListener;
        public View mView;

        public RoundedImageView mProfile;
        public TextView mUsername;
        public TextView mPoints;
        public ImageView mBlitz;
        public ImageView mBlitzClicked;
        public ImageButton mLike;
        public ImageButton mLikeClicked;
        public ImageButton mDislike;
        public ImageButton mDislikeClicked;
        public RecyclerView mRecyclerView;


        public DailyViewHolder(View view) {
            super(view);
            mView = view;
            this.mProfile= (RoundedImageView) view.findViewById(R.id.profile_picture);
            this.mUsername = (TextView) view.findViewById(R.id.name);
            this.mPoints= (TextView) view.findViewById(R.id.points);
            this.mBlitz = (ImageView) view.findViewById(R.id.blitzIcon);
            this.mBlitzClicked = (ImageView) view.findViewById(R.id.blitzClickedIcon);
            this.mLike = (ImageButton) view.findViewById(R.id.like);
            this.mLikeClicked = (ImageButton) view.findViewById(R.id.likeClicked);
            this.mDislike = (ImageButton) view.findViewById(R.id.dislike);
            this.mDislikeClicked = (ImageButton)view.findViewById(R.id.dislikeClicked);
            this.mRecyclerView = (RecyclerView) view.findViewById(R.id.chapterOfTheDay);

            mBlitz.setOnClickListener(DailyViewHolder.this);
            mBlitzClicked.setOnClickListener(DailyViewHolder.this);
            mLike.setOnClickListener(DailyViewHolder.this);
            mLikeClicked.setOnClickListener(DailyViewHolder.this);
            mDislike.setOnClickListener(DailyViewHolder.this);
            mDislikeClicked.setOnClickListener(DailyViewHolder.this);
        }

        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic) {this.itemClickListener=ic;}
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}

