package com.example.bsaraci.blitzone.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;

public class ProfileRecyclerviewAdapter extends RecyclerView.Adapter<ProfileRecyclerviewAdapter.DataObjectHolder> {

    private ProfilePhotosDataSet mDataset;
    private Context context;


    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView chapterTextView;
        ImageView photoChapterImageView;
        ProgressBar progressBar;

        public DataObjectHolder(View itemView) {
            super(itemView);
            chapterTextView = (TextView) itemView.findViewById(R.id.chapter);
            photoChapterImageView = (ImageView) itemView.findViewById(R.id.photo_chapter);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress1);
        }

            @Override
            public void onClick(View v) {
            }
    }


    public ProfileRecyclerviewAdapter(ProfilePhotosDataSet myDataset, Context context) {
        mDataset = myDataset;
        this.context=context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hlv_inflate, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        Chapter chap = mDataset.getChapter(position);
        final PhotoChapter photoChapter1 = mDataset.getPhotoChapter(chap);
        if (photoChapter1.is_urlUpdated())
        {
            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(this.context)
                    .load(photoChapter1.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.photoChapterImageView);

            Glide
                    .with(this.context)
                    .load(photoChapter1.getUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(300,300) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            photoChapter1.setBitmap(resource);
                        }
                    });

        }
        else
        {
            holder.photoChapterImageView.setImageBitmap(photoChapter1.getBitmap());
        }
        holder.chapterTextView.setText(chap.getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.getSize();
    }

}

