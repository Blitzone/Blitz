package com.example.bsaraci.blitzone.Blitzone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;

public class SingleViewModelAdapter extends RecyclerView.Adapter<SingleViewModelAdapter.SingleItemRowHolder> {

    private ArrayList<SingleViewModel> itemsList;
    private Context mContext;

    public SingleViewModelAdapter(Context context, ArrayList<SingleViewModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blitzone_single_view_model, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        SingleViewModel singleItem = itemsList.get(i);

        holder.tvChapter.setText(singleItem.getChapter());
        holder.tvHour.setText(singleItem.getHour());
        holder.itemImage.setImageResource(singleItem.getImage());

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvChapter;
        protected TextView tvHour;
        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvChapter = (TextView) view.findViewById(R.id.chapter);
            this.tvHour = (TextView) view.findViewById(R.id.hour);
            this.itemImage = (ImageView) view.findViewById(R.id.photo);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(v.getContext(), tvChapter.getText(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}