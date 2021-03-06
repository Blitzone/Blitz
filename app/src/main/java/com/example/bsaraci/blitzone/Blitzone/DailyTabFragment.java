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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class DailyTabFragment extends Fragment{

    private ArrayList<ViewDataProvider> viewDataProviderList = new ArrayList<>();
    RecyclerView recyclerView;
    RecycleviewAdapter adap ;
    LinearLayoutManager linearLayoutManager;
    private TextView tvEmptyView;
    protected Handler handler;
    PullToRefreshView mPullToRefreshView ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.daily_tab_content,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.dailyList);
        tvEmptyView = (TextView)view.findViewById(R.id.emptyView);
        TextView onTop = (TextView) getActivity().findViewById(R.id.blitzone_toolbar_title);
        onTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTop();
            }
        });
        handler = new Handler();

        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.pull_to_refresh);

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(LogIn.isNetworkStatusAvialable(getContext())){
                            viewDataProviderList= new ArrayList<>();
                            adap.setList(viewDataProviderList);
                            getDailyUsers(viewDataProviderList);
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

        viewDataProviderList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adap = new RecycleviewAdapter(getContext(), viewDataProviderList, recyclerView);
        adap.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        getDailyUsers(viewDataProviderList);
                    }
                }, 2000);

            }
        });

        recyclerView.setAdapter(adap);

        return view;

    }

    public void goTop(){
        recyclerView.smoothScrollToPosition(0);
    }

    public JSONObject getDailyFollowingUsersParams(ArrayList<ViewDataProvider> dailyUserList){

        HashMap<String,JSONArray> params = new HashMap<>();
        JSONArray keys = new JSONArray();
        for(int i = 0; i<dailyUserList.size(); i++){
            try {
                keys.put(i, dailyUserList.get(i).getUser().getPrimaryKey());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        params.put("client_pks", keys);

        return new JSONObject(params);

    }

    public void getDailyUsers(ArrayList<ViewDataProvider> dailyUserList){

        if (getContext() == null){
            return;
        }

        //Adds a listener to the response. In this case the response of the server will trigger the method updateTopic
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateDailyUserList(response);
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
                RequestURL.DAILY_USERS,                                      //The Url (/images/daily/)
                Request.Method.POST,                                         //Type of the method
                getDailyFollowingUsersParams(dailyUserList),                 //Put the parameters of the request here (JSONObject format)
                listener,                                                    //The listener
                errorListener,                                               //The errorListener
                jwtManager                                                   //The jwtManager
        );

        RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(mRequest);    //Sends the request
    }

    public void updateDailyUserList (JSONObject response){
        try{
            JSONArray userTopics = response.getJSONArray("userTopics");
            for(int i = 0; i<userTopics.length() ; i++){
                User u = new User();
                ViewDataProvider viewDataProvider = new ViewDataProvider();
                JSONObject jsonUserTopic = userTopics.getJSONObject(i);
                JSONObject jsonUser = jsonUserTopic.getJSONObject("user");
                JSONArray jsonPhotoChapters = jsonUserTopic.getJSONArray("photoChapters");
                ArrayList<SingleViewModel> singleViewModels = new ArrayList<>();

                for(int j = 0; j<jsonPhotoChapters.length(); j++){
                    SingleViewModel singleViewModel = new SingleViewModel();
                    JSONObject jsonChapter = jsonPhotoChapters.getJSONObject(j);
                    singleViewModel.setChapterPhotoUrl(RequestURL.IP_ADDRESS + jsonChapter.getString("image") );
                    singleViewModel.setChapterName(jsonChapter.getString("chapter"));
                    singleViewModels.add(singleViewModel);
                }
                u.setUsername(jsonUser.getString("user"));
                u.setBlitz(jsonUser.getInt("blitzCount"));
                u.setProfilePictureUrl(RequestURL.IP_ADDRESS + jsonUser.getString("avatar"));
                u.setPrimaryKey(jsonUser.getInt("pk"));
                u.setNumFollowers(jsonUser.getInt("followers"));
                u.setLikes(jsonUserTopic.getInt("likes"));
                u.setDislikes(jsonUserTopic.getInt("dislikes"));
                u.setIs_liked(jsonUserTopic.getBoolean("is_liked"));
                u.setIs_disliked(jsonUserTopic.getBoolean("is_disliked"));
                u.setIs_blitzed(jsonUserTopic.getBoolean("is_blitzed"));
                viewDataProvider.setUser(u);
                viewDataProvider.setPhotoChapters(singleViewModels);
                viewDataProvider.setBlitz(R.mipmap.ic_orange_rounded_blitz);
                viewDataProvider.setBlitzClicked(R.mipmap.ic_orange_blitz);
                viewDataProvider.setLike(R.mipmap.ic_mint_rounded_like);
                viewDataProvider.setLikeClicked(R.mipmap.ic_like_clicked);
                viewDataProvider.setDislike(R.mipmap.ic_red_rounded_dislike);
                viewDataProvider.setDislikeClicked(R.mipmap.ic_dislike_clicked);
                viewDataProvider.setBlitzesText("blitzes");
                viewDataProviderList.add(viewDataProvider);
            }

            if (viewDataProviderList.isEmpty()) {
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
    public void onResume() {
        super.onResume();
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

            viewDataProviderList= new ArrayList<>();
            adap.setList(viewDataProviderList);
            getDailyUsers(viewDataProviderList);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }

    }


}