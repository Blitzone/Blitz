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

public class BestTabFragment extends Fragment {

    private List<RowDataProvider> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleviewAdapter mAdapter;

    /*int [] profilePictures = {R.mipmap.ic_orange_profile,R.mipmap.ic_orange_profile,R.mipmap.ic_orange_profile,
            R.mipmap.ic_orange_profile,R.mipmap.ic_orange_profile,R.mipmap.ic_orange_profile,
            R.mipmap.ic_orange_profile,R.mipmap.ic_orange_profile,R.mipmap.ic_orange_profile};
    String [] usernames = {"teasaraci","mikelv92","sarasaraci","jonagolemi","ergysmati","enderballa","ilirpirani","sidritdritorja","jv21"};
    String [] points = {"500","117","110","90","57","50","34","31","10"};
    int [] blitz = {R.mipmap.ic_orange_blitz,R.mipmap.ic_orange_blitz,R.mipmap.ic_orange_blitz,
            R.mipmap.ic_orange_blitz,R.mipmap.ic_orange_blitz,R.mipmap.ic_orange_blitz,
            R.mipmap.ic_orange_blitz,R.mipmap.ic_orange_blitz,R.mipmap.ic_orange_blitz};*/

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.best_tab_content, container, false);

        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

        RecyclerView recyclerView = (RecyclerView) rv.findViewById(R.id.bestList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        rv.setAdapter(new RecyclerowAdapter(list));

        prepareData();

        return rv;

    }
    public void prepareData (){
        RowDataProvider l =new RowDataProvider( R.mipmap.ic_profile_avatar,"sarasaraci" ,"200" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider( R.mipmap.ic_profile_avatar,"teasaraci" ,"150" ,0);
        list.add(l);
        l =new RowDataProvider( R.mipmap.ic_profile_avatar,"enderballa" ,"100" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider( R.mipmap.ic_profile_avatar,"mikelv92" ,"100" ,0);
        list.add(l);
        l =new RowDataProvider( R.mipmap.ic_profile_avatar,"jonagolemi" ,"30" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider( R.mipmap.ic_profile_avatar,"jv21" ,"15" ,0);
        list.add(l);
        l =new RowDataProvider( R.mipmap.ic_profile_avatar,"ergysmati" ,"10" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider( R.mipmap.ic_profile_avatar,"sidritdritorja" ,"10" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider( R.mipmap.ic_profile_avatar,"ilirpirani" ,"1" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider( R.mipmap.ic_profile_avatar,"arditmeti" ,"0" ,R.mipmap.ic_orange_blitz);
        list.add(l);


    }
}