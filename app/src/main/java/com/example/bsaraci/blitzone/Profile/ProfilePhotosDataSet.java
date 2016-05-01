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

    public void addChapter (Profile.Chapter chapter){
        ProfilePhotosProvider profilePhotosProvider = new ProfilePhotosProvider(chapter,null);
        mArray.add(profilePhotosProvider);
    }

    public Profile.Chapter getChapter (int position){
       return mArray.get(position).getChapter();
    }

    public void addChapters (ArrayList<Profile.Chapter> list){
        for(Profile.Chapter obj : list){
            mArray.add(new ProfilePhotosProvider(obj));
        }
    }

    public void initPhotoChapters(ArrayList<Profile.PhotoChapter> list){
        for (int i = 0; i < this.getSize(); i++)
        {
            ProfilePhotosProvider elem = mArray.get(i);
            elem.setPhotoChapter(list.get(i));
        }
    }

    public Profile.PhotoChapter getPhotoChapter(Profile.Chapter chapter){
        for (ProfilePhotosProvider obj : mArray){
            if(obj.getChapter().getId() == chapter.getId()){
                return obj.getPhotoChapter();
            }
        }
        return null;
    }

    public void addPhotoChapter(Profile.PhotoChapter bitmap, Profile.Chapter chapter){
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
