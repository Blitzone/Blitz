package com.example.bsaraci.blitzone.Search;

/**This class represents the behaviour of our search user list. It is related to classes : Search, SearchModel, SearchViewHolder and CustomFilter
* SearchAdapter affects to all elements of the row of our list of users we want to search, all the data needed to initiate it. Also
* it sends the request for following or unfollowing a user to the server. Then when the buttons add or remove are clicked the request
* is sent. Also it filters the list as we start typing a letter in the search EditText
*************************************************************************************************************************************
* BUGS : NO BUGS DETECTED FOR THE MOMENT
*************************************************************************************************************************************
* AMELIORATION : NO AMELIORATION DETECTED FOR THE MOMENT */

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;
import com.example.bsaraci.blitzone.ServerComm.RequestURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable{

    Context c;                                  //Context where we are calling the adapter
    ArrayList<SearchModel> users,filterList;    //Two ArrayList<SearchModel>, one for users, the second one for filtered list
    CustomFilter filter;                        //The filter

/**
    CONSTRUCTOR OF THE SEARCH ADAPTER
    @param ctx, the context where we are calling the adapter
    @param users, the ArrayList of the users that is related to the other methods of this class
*/
    public SearchAdapter(Context ctx,ArrayList<SearchModel> users) {
        this.c=ctx;
        this.users=users;
        this.filterList=users;
    }

/**
    THIS METHOD IS CALLED WHEN RecyclerView NEEDS A NEW RecyclerView.ViewHolder OF THE GIVEN TYPE TO REPRESENT AND ITEM
    @param parent, the ViewGroup into which the new View will be added after it is bound to an adapter position
    @param viewType, the view type of the new View
    @return a new ViewHolder that holds a View of the given view type
*/
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        @SuppressLint("InflateParams") View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_model,null); //Inflates the xml element to the view v
        @SuppressWarnings("UnnecessaryLocalVariable") SearchViewHolder holder=new SearchViewHolder(v); //Creates a new SearchViewHolder
        return holder;
    }

/**
    THIS METHOD IS CALLED BY RecyclerView TO DISPLAY THE DATA AT THE SPECIFIED POSITION
    @param holder, the ViewHolder which should be updated to represent the contents of the item at the given position in the data set
    @param position, the position of the item within the adapter's data set.
*/
    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {

        //Bind data
        holder.addUserText.setText(users.get(position).getAdd());       //Puts the text for Add button which is found at position
        holder.removeUserText.setText(users.get(position).getRemove()); //Puts the text for Remove button which is found at position

            //Enters if you follow user in position
            if(users.get(position).getUser().isFollowing()){

                holder.addUserText.setVisibility(View.GONE);        //Makes the addUserText invisible
                holder.removeUserText.setVisibility(View.VISIBLE);  //Makes the removeUserText visible
            }

            //Enters if you don't follow user in position
            else {

                holder.addUserText.setVisibility(View.VISIBLE);     //Makes the addUserText visible
                holder.removeUserText.setVisibility(View.GONE);     //Makes the removeUserText invisible
            }

        String url = (users.get(position).getUser().getProfilePictureUrl());    //Takes the Url of the picture for user in position
        String username = (users.get(position).getUser().getUsername());        //Takes the username for user in position

            //Enters if we have an url for the picture
            if (url!= null){

                users.get(position).getUser().loadPicture(c, url, holder.img); //Loads the picture using glide to the appropriated ImageView
                holder.usernameText.setText(username);  //Sets the text for the appropriated TextView
            }

            //Enters if we don't have an url for the picture
            else {

                holder.img.setImageResource(R.color.boldGray);  //Loads a gray picture
            }


        //Implement clickListener
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                //Enters if we click on Add
                if (v == holder.addUserText){

                    String username = (users.get(pos).getUser().getUsername());     //Takes the username of the user in pos
                    getFollowUser(username);                                        //Triggers the method getFollowUser
                    v.setVisibility(View.GONE);                                     //Makes Add button invisible
                    holder.removeUserText.setVisibility(View.VISIBLE);              //Makes Remove button visible
                }

                //Enters if we click on Remove
                else if(v == holder.removeUserText){

                    String username = (users.get(pos).getUser().getUsername());     //Takes the username of the user in pos
                    getUnfollowUser(username);                                      //Triggers the method getUnfollowUser
                    v.setVisibility(View.GONE);                                     //Makes Remove button invisible
                    holder.addUserText.setVisibility(View.VISIBLE);                 //Makes Add button visible
                }
            }
        });

    }

/**
    THIS METHOD GETS THE TOTAL NUMBERS OF USERS
    @return the total number of items in the data set hold by the adapter
*/
    @Override
    public int getItemCount() {
        return users.size();
    }

/**
    THIS METHOD RETURNS THE FILTER LIST
    @return a new filter for the ArrayList<SearchModel>
*/
    @Override
    public Filter getFilter() {
        //ENTERS IF WE DONT HAVE A FILTER
        if(filter==null) {

            filter=new CustomFilter(filterList,this);   //Creates a new filter
        }

        return filter;
    }

/**
    THIS METHOD CREATES THE PARAMS NEEDED FOR THE MRequest getFollowUser
    @param username, the username to put in the HashMap
    @return the params needed for the MRequest
*/
    private JSONObject getFollowUserParams(String username){

        Map<String, String> params = new HashMap<>(); //Creates the HashMap
        params.put("followedUser", username);          //Puts username in key 'followedUser'
        return new JSONObject(params);                 //Returns the params
    }

/**
    THIS METHOD CREATES THE PARAMS NEEDED FOR THE MRequest getUnfollowUser
    @param username, the username to put in the HashMap
    @return the params needed for the MRequest
*/
    private JSONObject getUnfollowUserParams(String username){

        Map<String, String> params = new HashMap<>(); //Creates the HashMap
        params.put("followedUser", username);          //Puts username in key 'followedUser'
        return new JSONObject(params);                 //Returns the params
    }

/**
    THIS METHOD SENDS THE MRequest TO UNFOLLOW AN USER. TAKES IN PARAMETER THE USERNAME NEEDED TO COMPLETE THE REQUEST
    @param username, the username of the user we want to unfollow
*/
    private void getUnfollowUser(String username){

        //Adds a listener to the response. In this case it will alert user if the user is added or remove correctly.
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We use try and catch to handle JSONExceptions
                try {

                    //Enters if statusCode is 200
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(c,"User removed successfully",Toast.LENGTH_SHORT).show(); //Alerts user
                    }

                    //Enters when status code !=200 (=400)
                    else {
                        Toast.makeText(c,"There was an error when removing this user",Toast.LENGTH_SHORT).show(); //Alerts user
                    }
                }

                //In case of an exception
                catch (JSONException e){
                    e.printStackTrace();

                }
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        };

        JWTManager jwtManager = new JWTManager(c);  //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.UNFOLLOW_USER,           //The URL (/accounts/delFollow/)
                Request.Method.POST,                //Type of method
                getUnfollowUserParams(username),    //Put the parameters of the request here (JSONObject format)
                listener,                           //The listener
                errorListener,                      //The errorListener
                jwtManager                          //The JWTManager
        );

        RequestQueueSingleton.getInstance(c).addToRequestQueue(mRequest);   //Sends the request
    }

/**
    THIS METHOD SENDS THE MRequest TO FOLLOW AN USER. TAKES IN PARAMETER THE USERNAME NEEDED TO COMPLETE THE REQUEST
    @param username, the username of the user we want to follow
*/
    private void getFollowUser(String username){

        //Adds a listener to the response. In this case it will alert user if the user is added or remove correctly.
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We use try and catch to handle JSONExceptions
                try {

                    //Enters if statusCode is 200
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(c,"User added successfully",Toast.LENGTH_SHORT).show();  //Alerts user
                    }

                    //Enters when status code !=200 (=400)
                    else {
                        Toast.makeText(c,"There was an error when adding this user",Toast.LENGTH_SHORT).show(); //Alerts user
                    }
                }

                //In case of JSONException
                catch (JSONException e){
                    e.printStackTrace();

               }
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        };

        JWTManager jwtManager = new JWTManager(c);  //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.FOLLOW_USER,         //The Url (/accounts/addFollow/)
                Request.Method.POST,            //The type of method
                getFollowUserParams(username),  //Put the parameters of the request here (JSONObject format)
                listener,                       //The listener
                errorListener,                  //The errorListener
                jwtManager                      //The JWTManager
        );

        RequestQueueSingleton.getInstance(c).addToRequestQueue(mRequest);   //Sends the request
    }
}