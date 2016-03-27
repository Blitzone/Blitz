package com.example.bsaraci.blitzone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DailyTabFragment extends Fragment {

    private List<viewDataProvider> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.daily_tab_content, container, false);

        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

        RecyclerView recyclerView = (RecyclerView) rv.findViewById(R.id.dailyList);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL)); //For the divider

        rv.setAdapter(new recycleviewAdapter(list));

        prepareData();

        return rv;

    }
    public void prepareData (){
        viewDataProvider l =new viewDataProvider( R.mipmap.ic_orange_profile,"jv21" ,"15" ,R.mipmap.ic_orange_blitz,"Old travel throwback", "2 seconds ago");
        list.add(l);
        l =new viewDataProvider( R.mipmap.ic_orange_profile,"teasaraci" ,"150" ,0,"Tickets to a new adventure", "4 minutes ago");
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
        list.add(l);




    }
}