package com.example.bsaraci.blitzone.Search;


import android.view.View;

//IT IS AN INTERFACE THAT ALLOWS TO CREATE AN ItemClickListener FOR A RECYCLER VIEW
public interface ItemClickListener {

    //Method onItemClick that should be called whenever we use this interface.
    void onItemClick(View v,int pos);
}