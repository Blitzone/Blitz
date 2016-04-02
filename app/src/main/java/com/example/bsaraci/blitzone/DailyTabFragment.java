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
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.daily_tab_content,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.dailyList);
        initRecyclerView();

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.orange,
                R.color.orange,
                R.color.orange,
                R.color.orange);

        return view;

    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adap);
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