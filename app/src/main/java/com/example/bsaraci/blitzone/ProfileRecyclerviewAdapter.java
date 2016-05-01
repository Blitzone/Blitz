package com.example.bsaraci.blitzone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileRecyclerviewAdapter extends RecyclerView.Adapter<ProfileRecyclerviewAdapter.DataObjectHolder> {

    public interface OnItemClickListener {
        void onItemClick(ProfilePhotosProvider item);
    }

    private ProfilePhotosDataSet mDataset;
    private static MyClickListener myClickListener;

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

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ProfileRecyclerviewAdapter(ProfilePhotosDataSet myDataset) {
        mDataset = myDataset;
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
        holder.chapterTextView.setText(chap.getName());
        PhotoChapter photoChapter1 = mDataset.getPhotoChapter(chap);
        holder.photoChapterImageView.setImageBitmap(photoChapter1.getBitmap());
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

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}

