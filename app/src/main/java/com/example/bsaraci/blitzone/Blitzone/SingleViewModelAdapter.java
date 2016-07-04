package com.example.bsaraci.blitzone.Blitzone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
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
import com.bumptech.glide.request.target.Target;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.ItemClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        SingleViewModel singleItem = itemsList.get(i);
        final String url = singleItem.getChapterPhotoUrl();

        holder.tvChapter.setText(singleItem.getChapterName());
        if(url!=null){

            loadWithGlide(mContext, url, holder.itemImage);
            blurWithGlide(mContext,url,holder.transparentItemImage);
        }

        else{
            holder.itemImage.setImageResource(R.color.white);
        }


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

    public void blurWithGlide(Context context, String url, ImageView imageView){
        Glide
                .with( context )
                .load(url)
                .transform( new BlurTransformation( context ) )
                        //.bitmapTransform( new BlurTransformation( context ) ) // this would work too!
                .into(imageView);
    }
    public void loadWithGlide(Context context, String url, ImageView imageView){
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
                        return false;
                    }
                })
                .into(imageView);
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

            this.tvChapter = (TextView) view.findViewById(R.id.chapterName);
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