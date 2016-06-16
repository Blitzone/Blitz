package com.example.bsaraci.blitzone.Blitzone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.ItemClickListener;

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
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        SingleViewModel singleItem = itemsList.get(i);

        Bitmap blurred = blurRenderScript(mContext,singleItem.getImage(), 15);

        holder.tvChapter.setText(singleItem.getChapter());
        holder.itemImage.setImageBitmap(singleItem.getImage());
        holder.transparentItemImage.setImageBitmap(blurred);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if(v==holder.itemImage){
                    v.setVisibility(View.GONE);
                    holder.tvChapter.setVisibility(View.VISIBLE);
                    holder.transparentItemImage.setVisibility(View.VISIBLE);
                }

                else if(v==holder.transparentItemImage){
                    v.setVisibility(View.GONE);
                    holder.tvChapter.setVisibility(View.GONE);
                    holder.itemImage.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvChapter;
        public ImageView itemImage;
        public ImageView transparentItemImage;
        ItemClickListener itemClickListener;



        public SingleItemRowHolder(View view) {
            super(view);

            this.tvChapter = (TextView) view.findViewById(R.id.chapter);
            this.itemImage = (ImageView) view.findViewById(R.id.photo);
            this.transparentItemImage = (ImageView) view.findViewById(R.id.transparentPhoto);

            itemImage.setOnClickListener(this);
            transparentItemImage.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());

        }

        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }

    }

    @SuppressLint("NewApi")
    public static Bitmap blurRenderScript(Context context,Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

}