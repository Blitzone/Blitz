package com.example.bsaraci.blitzone.Blitzone;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BestTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private List<RowDataProvider> rowDataProviderList = new ArrayList<RowDataProvider>();
    private ArrayList<User> followingUserList = new ArrayList<User>();
    RecyclerView recyclerView;
    TextView tvEmptyView;
    protected Handler handler;
    private SwipeRefreshLayout swipeLayout;
    RecyclerowAdapter adap ;
    //TODO TESTING ONLY
    private Integer teaCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.best_tab_content, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.bestList);
        tvEmptyView = (TextView)v.findViewById(R.id.emptyView);
        handler = new Handler();

        prepareUserList();
        User.order(followingUserList);
        prepareData();
        initRecyclerView();

        if (rowDataProviderList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        adap.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                rowDataProviderList.add(null);
                adap.notifyItemInserted(rowDataProviderList.size() - 1); //if u take this the rowDataProviderList does back to the beggining everytime

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        rowDataProviderList.remove(rowDataProviderList.size() - 1);
                        adap.notifyItemRemoved(rowDataProviderList.size());
                        //add items one by one
                        int start = rowDataProviderList.size();
                        int end = start + 2;

                        for (int i = start + 1; i <= end; i++) {
                            //TODO COUNTING VIEWS FOR TESTING. REMOVE WHEN YOU DEPLOY
                            teaCount++;
                            rowDataProviderList.add(new RowDataProvider());
                            adap.notifyItemInserted(rowDataProviderList.size());
                        }
                        adap.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.mint,
                R.color.mint,
                R.color.mint,
                R.color.mint);

        return v;

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adap = new RecyclerowAdapter(getContext(),rowDataProviderList,recyclerView);
        recyclerView.setAdapter(adap);
    }

    public void prepareData (){
        for(int i = 0; i<30 ; i++){
            RowDataProvider rowDataProvider = new RowDataProvider();
            rowDataProvider.setUser(followingUserList.get(i));
            rowDataProviderList.add(rowDataProvider);
        }
    }

    public void prepareUserList(){
        for(int i = 0; i<30 ; i++){
            Random random = new Random();
            int r = random.nextInt(1000);
            User u = new User();
            u.setUsername("user " + (i + 1));
            u.setBlitz(r);
            u.setProfilePicture(BitmapFactory.decodeResource(this.getResources(), R.drawable.b));
            u.setFollowing(true);
            followingUserList.add(u);
        }
    }

    @Override
    public void onRefresh() {
        executeSchedule();
        Toast.makeText(getContext(),"Test Refreshing", Toast.LENGTH_LONG).show();
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recyclerView.setAdapter(adap);
            swipeLayout.setRefreshing(false);

        }

    }
}