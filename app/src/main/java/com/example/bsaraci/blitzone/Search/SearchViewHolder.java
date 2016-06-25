package com.example.bsaraci.blitzone.Search;

/**This class defines the views that are going to be present in our row. It appears in : SearchAdapter. It also overrides
 * the onClick method from the Interface ItemClickListener
*************************************************************************************************************************
* BUGS : NO BUGS FOR THE MOMENT
*************************************************************************************************************************
* AMELIORATION : NO AMELIORATION FOR THE MOMENT*/

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView img;                          //For the profile picture
    TextView usernameText;                  //For the username
    TextView addUserText;                   //For the Add button
    TextView removeUserText;                //For the Remove button
    ItemClickListener itemClickListener;    //New ItemClickListener

/**
    CONSTRUCTOR
    @param itemView, the view in this case the row of the search user list
*/
    public SearchViewHolder(View itemView) {
        super(itemView);

        this.img= (ImageView) itemView.findViewById(R.id.userImage);                //Affects this xml element to img
        this.usernameText = (TextView) itemView.findViewById(R.id.nameTxt);         //Affects this xml element to usernameText
        this.addUserText = (TextView) itemView.findViewById(R.id.addUser);          //Affects this xml element to addUserText
        this.removeUserText = (TextView) itemView.findViewById(R.id.removeUser);    //Affects this xml element to removeUserText

        addUserText.setOnClickListener(this);       //Sets the click only for add button
        removeUserText.setOnClickListener(this);    //Sets the click only for remove button
    }

/**
    METHOD THAT HANDLES THE CLICKS ON VIEW v
    @param v, the view that we click
*/
    @Override
    public void onClick(View v) {

        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }

/**
    SETTER FOR itemClickListener
    @param ic, the new ItemClickListener that we want to affect to a button of the row
*/
    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }
}