package com.example.bsaraci.blitzone;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DailyTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private List<viewDataProvider> list = new ArrayList<>();
    private List <String> usernames = new ArrayList<>();
    private List <String> points = new ArrayList<>();
    private List <String> description = new ArrayList<>();
    private List <String> time = new ArrayList<>();
    private List <Integer> profilePictures = new ArrayList<>();
    private List <Integer> blitz = new ArrayList<>();
    private SwipeRefreshLayout swipeLayout;
    RecyclerView recyclerView;
    recycleviewAdapter adap = new recycleviewAdapter(list);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.daily_tab_content,container,false);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.dailyList);
        recyclerView.setAdapter(adap);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL)); //For the divider
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.orange,
                R.color.orange,
                R.color.orange,
                R.color.orange);

        return view;

    }

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        // Deserialize API response and then construct new objects to append to the adapter
        // Add the new objects to the data source for the adapter
        list.add(new viewDataProvider( R.mipmap.ic_orange_profile,"teasaraci" ,"150" ,0,"Tickets to a new adventure", "4 minutes ago"));
        // For efficiency purposes, notify the adapter of only the elements that got changed
        // curSize will equal to the index of the first element inserted because the list is 0-indexed
        int curSize = adap.getItemCount();
        adap.notifyItemRangeInserted(curSize, list.size() - 1);
    }

    public void prepareData (){

        for(int i =0;i<3; i++){
            usernames.add(i,"jv21");
            points.add(i,"15");
            description.add(i,"Tickets to a new adventure");
            time.add(i,"6 minutes ago");
            profilePictures.add(i,R.mipmap.ic_orange_profile);
            blitz.add(i,R.mipmap.ic_orange_blitz);

            viewDataProvider l =new viewDataProvider( profilePictures.get(i),usernames.get(i) ,points.get(i) ,blitz.get(i),description.get(i), time.get(i));
            list.add(l);
        }
        /*l =new viewDataProvider( R.mipmap.ic_orange_profile,"teasaraci" ,"150" ,0,"Tickets to a new adventure", "4 minutes ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"sarasaraci" ,"200" ,R.mipmap.ic_orange_blitz,"Your travelmate", "10 minutes ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"jv21" ,"15" ,0,"Old travel throwback", "16 minutes ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"enderballa" ,"100" ,R.mipmap.ic_orange_blitz,"Old travel throwback", "25 minutes ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"ergysmati" ,"10" ,R.mipmap.ic_orange_blitz,"Tickets to a new adventure", "34 minutes ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"sidritdritorja" ,"10" ,R.mipmap.ic_orange_blitz,"Your travelmate", "50 minutes ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"jonagolemi" ,"30" ,0,"Tickets to a new adventure", "55 minutes ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"ilirpirani" ,"1" ,R.mipmap.ic_orange_blitz,"Old travel throwback", "1 hour ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"arditmeti" ,"0" ,R.mipmap.ic_orange_blitz,"Your travelmate", "2 hours ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"mikelv92" ,"100" ,0,"Tickets to a new adventure", "6 hours ago");
        list.add(l);*/
    }

    //Takes the list from the database
    public void modifyDataOnRefresh (){
            usernames.add("testUsername");
            points.add("testPoints");
            profilePictures.add(R.mipmap.ic_orange_profile);
            blitz.add(0);
            description.add("testDescription");
            time.add("testTime");

            viewDataProvider l =new viewDataProvider( profilePictures.get(profilePictures.size()-1),usernames.get(usernames.size()-1) ,points.get(points.size()-1) ,blitz.get(points.size()-1),description.get(description.size()-1), time.get(time.size()-1));
            list.add(0,l);


    }


    @Override
    public void onRefresh() {
        executeSchedule(); //Necessary. If not it wont refresh
        modifyDataOnRefresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareData();
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recyclerView.setAdapter(adap);
            swipeLayout.setRefreshing(false);

        }

    }
}
