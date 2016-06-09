package com.example.bsaraci.blitzone.Search;

import android.content.Context;
import android.support.design.widget.Snackbar;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable{

    Context c;
    ArrayList<SearchModel> users,filterList;
    CustomFilter filter;
    String username;


    public SearchAdapter(Context ctx,ArrayList<SearchModel> users)
    {
        this.c=ctx;
        this.users=users;
        this.filterList=users;
    }


    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_model,null);

        SearchViewHolder holder=new SearchViewHolder(v);

        return holder;
    }

    //DATA BOUND TO VIEWS
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {

        //BIND DATA
        holder.posTxt.setText(users.get(position).getPos());
        String url = (users.get(position).getUser().getProfilePictureUrl());
        username = (users.get(position).getUser().getUsername());
        if (url!= null){
            users.get(position).getUser().loadPicture(c, url, holder.img);
            holder.nameTxt.setText(username);
        }

        else
        {
            holder.img.setImageResource(R.color.boldGray);
        }


        //IMPLEMENT CLICK LISTENER
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
               getFollowUser();
            }
        });

    }

    //GET TOTAL NUM OF USERS
    @Override
    public int getItemCount() {
        return users.size();
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList,this);
        }

        return filter;
    }

    private JSONObject getFollowUserParams(String username){
        Map<String, String> params = new HashMap<String, String>();
        params.put("followedUser", username);
        return new JSONObject(params);
    }

    private void getFollowUser(){
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(c,response.toString(),Toast.LENGTH_LONG).show();
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        };

        JWTManager jwtManager = new JWTManager(c);
        //Put everything in the request

        MRequest mRequest = new MRequest(
                RequestURL.FOLLOW_USER,
                Request.Method.POST,
                getFollowUserParams(username), //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(c).addToRequestQueue(mRequest);
    }
}