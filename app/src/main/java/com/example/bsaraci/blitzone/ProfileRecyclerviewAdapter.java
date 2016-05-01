package com.example.bsaraci.blitzone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileRecyclerviewAdapter extends RecyclerView.Adapter<ProfileRecyclerviewAdapter.DataObjectHolder> {

    public interface OnItemClickListener {
        void onItemClick(ProfileHorizontalPhotosProvider item);
    }

    private DataSet mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView chapter;
        ImageView photoChapter;

        public DataObjectHolder(View itemView) {
            super(itemView);
            chapter = (TextView) itemView.findViewById(R.id.chapter);
            photoChapter = (ImageView) itemView.findViewById(R.id.photo_chapter);
            itemView.setOnClickListener(this);
        }

            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(getPosition(), v);
            }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ProfileRecyclerviewAdapter(DataSet myDataset) {
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
        holder.chapter.setText(chap.getName());
        holder.photoChapter.setImageBitmap(mDataset.getPhotoChapter(chap));
    }

    public void addItem(ProfileHorizontalPhotosProvider dataObj, int index) {
        Chapter chap = mDataset.getChapter(index);
        mDataset.addChapter(dataObj.getmChapter());
        mDataset.addPhotoChapter(dataObj.getmBitpmap(),chap);
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

