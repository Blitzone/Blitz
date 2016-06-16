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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;
import java.util.List;

public class DailyTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private List<ViewDataProvider> list = new ArrayList<>();
    private List <String> usernames = new ArrayList<>();
    private List <String> points = new ArrayList<>();
    private List <Integer> blitz = new ArrayList<>();
    private List <Integer> blitzClicked = new ArrayList<>();
    private List <Integer> like = new ArrayList<>();
    private List <Integer> likeClicked = new ArrayList<>();
    private List <Integer> dislike = new ArrayList<>();
    private List <Integer> dislikeClicked = new ArrayList<>();
    ArrayList <SingleViewModel> singleViewModels = new ArrayList<SingleViewModel>();
    private SwipeRefreshLayout swipeLayout;
    RecyclerView recyclerView;
    RecycleviewAdapter adap ;
    LinearLayoutManager linearLayoutManager;
    private TextView tvEmptyView;
    protected Handler handler;
    //TODO TESTING ONLY
    private Integer teaCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.daily_tab_content,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.dailyList);
        tvEmptyView = (TextView)view.findViewById(R.id.emptyView);
        handler = new Handler();

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.b);
        SingleViewModel singleViewModel = new SingleViewModel(bm,"test");
        singleViewModels.add(singleViewModel);
        singleViewModels.add(singleViewModel);
        singleViewModels.add(singleViewModel);
        singleViewModels.add(singleViewModel);
        singleViewModels.add(singleViewModel);
        singleViewModels.add(singleViewModel);

        prepareData();
        initRecyclerView();


        if (list.isEmpty()) {
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
                list.add(null);
                adap.notifyItemInserted(list.size() - 1); //if u take this the list does back to the beggining everytime

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        list.remove(list.size() - 1);
                        adap.notifyItemRemoved(list.size());
                        //add items one by one
                        int start = list.size();
                        int end = start + 2;

                        for (int i = start + 1; i <= end; i++) {
                            //TODO COUNTING VIEWS FOR TESTING. REMOVE WHEN YOU DEPLOY
                            teaCount++;
                            list.add(new ViewDataProvider("teasaraci" + teaCount.toString(), "200", 0,0,R.mipmap.ic_gray_like,R.mipmap.ic_like_clicked, R.mipmap.ic_gray_dislike, R.mipmap.ic_dislike_clicked,singleViewModels));
                            adap.notifyItemInserted(list.size());
                        }
                        adap.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });



        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.mint,
                R.color.mint,
                R.color.mint,
                R.color.mint);
        return view;

    }




    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adap = new RecycleviewAdapter(getContext(),list,recyclerView);
        recyclerView.setAdapter(adap);
    }


    public void prepareData (){

        for(int i =0;i<10; i++){
            usernames.add(i,"jv21");
            points.add(i,"15");
            blitz.add(i,R.mipmap.ic_gray_blitz);
            blitzClicked.add(i,R.mipmap.ic_orange_blitz);
            like.add(i,R.mipmap.ic_gray_like);
            likeClicked.add(i,R.mipmap.ic_like_clicked);
            dislike.add(i,R.mipmap.ic_gray_dislike);
            dislikeClicked.add(i,R.mipmap.ic_dislike_clicked);


            ViewDataProvider l =new ViewDataProvider(usernames.get(i) ,points.get(i) ,blitz.get(i),blitzClicked.get(i),like.get(i),likeClicked.get(i),dislike.get(i),dislikeClicked.get(i),singleViewModels);
            list.add(l);
        }
    }

    //Takes the list from the database
    public void modifyDataOnRefresh (){
        usernames.add("testUsername");
        points.add("testPoints");
        blitz.add(0);
        blitzClicked.add(0);
        like.add(R.mipmap.ic_gray_like);
        likeClicked.add(R.mipmap.ic_like_clicked);
        dislike.add(R.mipmap.ic_gray_dislike);
        dislikeClicked.add(R.mipmap.ic_dislike_clicked);

        ViewDataProvider l =new ViewDataProvider(usernames.get(usernames.size()-1) ,points.get(points.size()-1) ,blitz.get(points.size()-1),blitzClicked.get(points.size()-1),like.get(points.size()-1),likeClicked.get(points.size()-1),dislike.get(points.size()-1),dislikeClicked.get(points.size()-1),singleViewModels);
        list.add(0, l);


    }


    @Override
    public void onRefresh() {
        executeSchedule(); //Necessary. If not it wont refresh
        modifyDataOnRefresh();
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