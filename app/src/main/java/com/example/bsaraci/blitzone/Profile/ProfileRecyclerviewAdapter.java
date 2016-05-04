package com.example.bsaraci.blitzone.Profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.bsaraci.blitzone.R;

public class ProfileRecyclerviewAdapter extends RecyclerView.Adapter<ProfileRecyclerviewAdapter.DataObjectHolder> {

    private Topic topic;
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


    public ProfileRecyclerviewAdapter(Topic myDataset, Context context) {
        topic = myDataset;
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
        final PhotoChapter photoChapter = topic.getPhotoChapterFromPosition(position);
        if (photoChapter.getUrl() != null) {
            holder.progressBar.setVisibility(View.VISIBLE);
            loadWithGlide(this.context, photoChapter.getUrl(), holder.progressBar, holder.photoChapterImageView);
            setBitmapWithGlide(this.context, photoChapter.getUrl(), photoChapter);
        }
        else
        {
            holder.photoChapterImageView.setImageBitmap(photoChapter.getPhoto());
        }
        holder.chapterTextView.setText(photoChapter.getChapterName());
    }

    @Override
    public int getItemCount() {
        return topic.getPhotoChapters().size();
    }

    public void loadWithGlide(Context context, String url, final ProgressBar pg, ImageView imageView){
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pg.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    public void setBitmapWithGlide(Context context, String url, final PhotoChapter photoChapter){
        Glide
                .with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(600, 600) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        photoChapter.setPhoto(resource);
                    }
                });
    }
}

