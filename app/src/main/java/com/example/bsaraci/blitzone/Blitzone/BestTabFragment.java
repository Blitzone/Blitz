package com.example.bsaraci.blitzone.Blitzone;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.User;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;
import com.example.bsaraci.blitzone.ServerComm.RequestURL;
import com.example.bsaraci.blitzone.Start.LogIn;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class BestTabFragment extends Fragment {

    private ArrayList<RowDataProvider> rowDataProviderList;
    RecyclerView recyclerView;
    TextView tvEmptyView;
    protected Handler handler;
    RecyclerowAdapter adap ;
    LinearLayoutManager linearLayoutManager;
    PullToRefreshView mPullToRefreshView ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.best_tab_content, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.bestList);
        tvEmptyView = (TextView)v.findViewById(R.id.emptyView);
        TextView onTop1 = (TextView) getActivity().findViewById(R.id.blitzone_toolbar_title1);
        onTop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTop1();
            }
        });
        handler = new Handler();

        mPullToRefreshView = (PullToRefreshView)v.findViewById(R.id.pull_to_refresh);

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(LogIn.isNetworkStatusAvialable(getContext())){
                            rowDataProviderList= new ArrayList<>();
                            adap.setList(rowDataProviderList);
                            getBestFollowingUsers(rowDataProviderList);
                            mPullToRefreshView.setRefreshing(false);
                        }
                        else{
                            Toast.makeText(getContext(), "Cannot refresh. No internet connection", Toast.LENGTH_SHORT).show();
                            mPullToRefreshView.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });


        rowDataProviderList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adap = new RecyclerowAdapter(getContext(), rowDataProviderList, recyclerView);
        adap.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBestFollowingUsers(rowDataProviderList);
                    }
                }, 2000);

            }
        });

        recyclerView.setAdapter(adap);



        return v;
    }

    public void goTop1(){
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onResume() {
        super.onResume();
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

        if (getContext() == null){
            return;
        }

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
                u.setIs_blitzed(jsonUser.getBoolean("is_blitzed"));
                u.setPrimaryKey(jsonUser.getInt("pk"));
                rowDataProvider.setUser(u);
                rowDataProviderList.add(rowDataProvider);
            }

            if (rowDataProviderList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
            }

            adap.notifyDataSetChanged();
            adap.setLoaded();
            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();


        }
        catch (JSONException e){
            e.printStackTrace();
        }

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
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI from here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            rowDataProviderList= new ArrayList<>();
            adap.setList(rowDataProviderList);
            getBestFollowingUsers(rowDataProviderList);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }
}