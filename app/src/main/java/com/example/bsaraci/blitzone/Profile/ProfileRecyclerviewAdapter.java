package com.example.bsaraci.blitzone.Profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;

public class ProfileRecyclerviewAdapter extends RecyclerView.Adapter<ProfileRecyclerviewAdapter.DataObjectHolder> {

    public interface OnItemClickListener {
        void onItemClick(ProfilePhotosProvider item);
    }

    private ProfilePhotosDataSet mDataset;
    private ImageLoader imageLoader;
    private Context context;


    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView chapterTextView;
        ImageView photoChapterImageView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            chapterTextView = (TextView) itemView.findViewById(R.id.chapter);
            photoChapterImageView = (ImageView) itemView.findViewById(R.id.photo_chapter);
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
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Chapter chap = mDataset.getChapter(position);
        PhotoChapter photoChapter1 = mDataset.getPhotoChapter(chap);
        if (photoChapter1.is_urlUpdated())
        {
            imageLoader = RequestQueueSingleton.getInstance(context).getImageLoader();
            imageLoader.get(photoChapter1.getUrl(), ImageLoader.getImageListener(holder.photoChapterImageView,
                    R.color.mint, R.color.mint));
        }
        else
        {
            holder.photoChapterImageView.setImageBitmap(photoChapter1.getBitmap());
        }
        holder.chapterTextView.setText(chap.getName());
        //holder.photoChapterImageView.setImageBitmap(photoChapter1.getBitmap());
    }

    public void addItem(ProfilePhotosProvider dataObj, int index) {
        Chapter chap = mDataset.getChapter(index);
        mDataset.addChapter(dataObj.getChapter());
        mDataset.addPhotoChapter(dataObj.getPhotoChapter(),chap);
        notifyItemInserted(index);
    }

    /*public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }*/

    @Override
    public int getItemCount() {
        return mDataset.getSize();
    }

}

