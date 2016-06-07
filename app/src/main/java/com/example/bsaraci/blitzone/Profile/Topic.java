package com.example.bsaraci.blitzone.Profile;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mikel on 5/3/16.
 */
public class Topic
{
    private Date startDate;
    private Date endDate;
    private Integer id;
    private String name;
    private ArrayList<PhotoChapter> photoChapters;

    public Topic(Integer id, String name)
    {
        this.id = id;
        this.name = name;
        this.photoChapters = new ArrayList<PhotoChapter>();
    }

    public Topic(Integer id){
        this.id=id;
    }

    public void addPhotoChapter(int chapterId, String chapterName)
    {
        this.photoChapters.add(new PhotoChapter(chapterId, chapterName));
    }

    public ArrayList<PhotoChapter> getPhotoChapters() {
        return photoChapters;
    }

    public void setPhotoChapters(ArrayList<PhotoChapter> photoChapters) {
        this.photoChapters = photoChapters;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPhotoByChapterId(Integer chapterId, Bitmap bitmap) {
        for (PhotoChapter obj : this.photoChapters)
            if (obj.getChapterId() == chapterId)
                obj.setPhoto(bitmap);
    }

    public Bitmap getPhotoFromChapter(Integer chapterId)
    {
        for (PhotoChapter photoChapter : this.photoChapters)
            if (photoChapter.getChapterId() == chapterId)
                return photoChapter.getPhoto();
        return null;
    }

    public PhotoChapter getPhotoChapterFromPosition(int position) {
        return this.photoChapters.get(position);
    }

    public PhotoChapter getPhotoChapterFromChapterId(int id)
    {
        for (PhotoChapter photoChapter : this.getPhotoChapters())
            if (photoChapter.getChapterId() == id)
                return photoChapter;
        return null;
    }
}
