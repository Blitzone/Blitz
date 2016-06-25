package com.example.bsaraci.blitzone.Search;

/*IT IS AN INTERFACE THAT ALLOWS TO CREATE AN ItemClickListener FOR A RECYCLER VIEW*/

import android.view.View;

public interface ItemClickListener {

/**
    METHOD onItemClick THAT SHOULD BE CALLED WHENEVER WE USE THIS INTERFACE
*/
    void onItemClick(View v,int pos);
}