package com.example.bsaraci.blitzone.Profile;

import java.util.ArrayList;

/**
 * Created by bsaraci on 5/1/2016.
 */
public class ProfilePhotosDataSet {
    private ArrayList<ProfilePhotosProvider> mArray;

    public ProfilePhotosDataSet(){
        mArray = new ArrayList<ProfilePhotosProvider>();
    }

    public ProfilePhotosDataSet(ArrayList<ProfilePhotosProvider> mArray){
        this.mArray=mArray;
    }

    public void addProfileHorizontalPhotoProvider (ProfilePhotosProvider profilePhotosProvider){
        mArray.add(profilePhotosProvider);
    }

    public void addChapter (Chapter chapter){
        ProfilePhotosProvider profilePhotosProvider = new ProfilePhotosProvider(chapter,null);
        mArray.add(profilePhotosProvider);
    }

    public Chapter getChapter (int position){
       return mArray.get(position).getChapter();
    }

    public void addChapters (ArrayList<Chapter> list){
        for(Chapter obj : list){
            mArray.add(new ProfilePhotosProvider(obj));
        }
    }

    public void initPhotoChapters(ArrayList<PhotoChapter> list){
        for (int i = 0; i < this.getSize(); i++)
        {
            ProfilePhotosProvider elem = mArray.get(i);
            elem.setPhotoChapter(list.get(i));
        }
    }

    public PhotoChapter getPhotoChapter(Chapter chapter){
        for (ProfilePhotosProvider obj : mArray){
            if(obj.getChapter().getId() == chapter.getId()){
                return obj.getPhotoChapter();
            }
        }
        return null;
    }

    public void addPhotoChapter(PhotoChapter bitmap, Chapter chapter){
        for (ProfilePhotosProvider obj : mArray){
            if(obj.getChapter().getId() == chapter.getId()){
                obj.setPhotoChapter(bitmap);
            }
        }
    }

    public int getSize (){
        return mArray.size();
    }

}
