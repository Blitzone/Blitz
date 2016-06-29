package com.example.bsaraci.blitzone.Blitzone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.User;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;
import com.example.bsaraci.blitzone.ServerComm.RequestURL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class BestTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<RowDataProvider> rowDataProviderList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView tvEmptyView;
    protected Handler handler;
    private SwipeRefreshLayout swipeLayout;
    RecyclerowAdapter adap ;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.best_tab_content, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.bestList);
        tvEmptyView = (TextView)v.findViewById(R.id.emptyView);
        handler = new Handler();

        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.mint,
                R.color.mint,
                R.color.mint,
                R.color.mint);

        return v;

    }

    private void initRecyclerView(RecyclerowAdapter adap) {
        linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adap);
    }

    public JSONObject getBestFollowingUsersParams(ArrayList<RowDataProvider> bestUserList){

        HashMap<String,JSONArray> params = new HashMap<>();
        JSONArray keys = new JSONArray();
        for(int i = 0; i<bestUserList.size(); i++){
            try {
                keys.put(i, bestUserList.get(i).getUser().getPrimaryKey());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        params.put("client_pks", keys);

        return new JSONObject(params);

    }

    public void getBestFollowingUsers(ArrayList<RowDataProvider> bestUserList){

        //Adds a listener to the response. In this case the response of the server will trigger the method updateTopic
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateFollowingUserList(response);
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        };

        JWTManager jwtManager = new JWTManager(getContext());    //Creates the new JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.BEST_USERS,                                       //The Url (/accounts/getFollowing/)
                Request.Method.POST,                                         //Type of the method
                getBestFollowingUsersParams(bestUserList),                   //Put the parameters of the request here (JSONObject format)
                listener,                                                    //The listener
                errorListener,                                               //The errorListener
                jwtManager                                                   //The jwtManager
        );

        RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(mRequest);    //Sends the request
    }

    public void updateFollowingUserList (JSONObject response){
        try{
            JSONArray userList = response.getJSONArray("following");
            for(int i = 0; i<userList.length() ; i++){
                User u = new User();
                RowDataProvider rowDataProvider = new RowDataProvider();
                JSONObject jsonUser = (JSONObject) userList.get(i);
                u.setUsername(jsonUser.getString("user"));
                u.setBlitz(jsonUser.getInt("blitzCount"));
                u.setProfilePictureUrl(RequestURL.IP_ADDRESS + jsonUser.getString("avatar"));
                u.setPrimaryKey(jsonUser.getInt("pk"));
                rowDataProvider.setUser(u);
                rowDataProviderList.add(i, rowDataProvider);
            }

            swipeLayout.setRefreshing(false);

            if (rowDataProviderList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
            }

            adap.notifyDataSetChanged();
            initRecyclerView(adap);

            adap.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom
                    rowDataProviderList.add(null);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //   remove progress item
                            getBestFollowingUsers(rowDataProviderList);
                            adap.notifyItemInserted(rowDataProviderList.size() - 1); //if u take this the rowDataProviderList goes back to the beginning every time
                            adap.setLoaded();
                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                        }
                    }, 2000);

                }
            });



        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void prepareData(){
        for (int i =0 ; i<1; i++){
            User user = new User();
            RowDataProvider r = new RowDataProvider();
            user.setUsername("user");
            user.setBlitz(0);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.color.lightGray);
            user.setProfilePicture(bm);
            r.setUser(user);
            rowDataProviderList.add(r);
        }
    }

    @Override
    public void onRefresh() {
        executeSchedule();
    }

    @Override
    public void onStart() {
        super.onStart();
        executeSchedule(); //First execution and data loading.
    }

    public void executeSchedule() {
        new Schedule().execute();
    }

    //I think it will allow us to communicate with database
    private class Schedule extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!swipeLayout.isRefreshing()) {
                swipeLayout.measure(10, 10);
                swipeLayout.setRefreshing(true); //set refresh state
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI from here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            rowDataProviderList= new ArrayList<>();
            getBestFollowingUsers(rowDataProviderList);
            adap = new RecyclerowAdapter(getContext(),rowDataProviderList,recyclerView);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }
}