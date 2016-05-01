package com.example.bsaraci.blitzone;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by bsaraci on 5/1/2016.
 */
public class DataSet {
    private ArrayList<ProfileHorizontalPhotosProvider> mArray;

    public DataSet(){
    }

    public DataSet( ArrayList<ProfileHorizontalPhotosProvider> mArray){
        this.mArray=mArray;
    }

    public void addProfileHorizontalPhotoProvider (ProfileHorizontalPhotosProvider profileHorizontalPhotosProvider){
        mArray.add(profileHorizontalPhotosProvider);
    }

    public void addChapter (Chapter chapter){
        ProfileHorizontalPhotosProvider profileHorizontalPhotosProvider = new ProfileHorizontalPhotosProvider(chapter,null);
        mArray.add(profileHorizontalPhotosProvider);
    }

    public Chapter getChapter (int position){
       return mArray.get(position).getmChapter();
    }

    public void addChapters (ArrayList<Chapter> list){
        for(Chapter obj : list){
            mArray.add(new ProfileHorizontalPhotosProvider(obj,null));
        }
    }

    public Bitmap getPhotoChapter(Chapter chapter){
        for (ProfileHorizontalPhotosProvider obj : mArray){
            if(obj.getmChapter().getId() == chapter.getId()){
                return obj.getmBitpmap();
            }
        }
        return null;
    }

    public void addPhotoChapter(Bitmap bitmap, Chapter chapter){
        for (ProfileHorizontalPhotosProvider obj : mArray){
            if(obj.getmChapter().getId() == chapter.getId()){
                obj.setmBitpmap(bitmap);
            }
        }
    }

    public int getSize (){
        return mArray.size();
    }

}
