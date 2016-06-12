package com.example.bsaraci.blitzone.Blitzone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;
import java.util.List;

public class BestTabFragment extends Fragment {

    private List<RowDataProvider> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleviewAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.best_tab_content, container, false);

        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

        RecyclerView recyclerView = (RecyclerView) rv.findViewById(R.id.bestList);

        rv.setAdapter(new RecyclerowAdapter(list));

        prepareData();

        return rv;

    }
    public void prepareData (){
        RowDataProvider l =new RowDataProvider(R.color.lightGray,"sarasaraci" ,"200" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider(R.color.lightGray,"teasaraci" ,"150" ,0);
        list.add(l);
        l =new RowDataProvider(R.color.lightGray,"enderballa" ,"100" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider(R.color.lightGray,"mikelv92" ,"100" ,0);
        list.add(l);
        l =new RowDataProvider(R.color.lightGray,"jonagolemi" ,"30" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider(R.color.lightGray,"jv21" ,"15" ,0);
        list.add(l);
        l =new RowDataProvider(R.color.lightGray,"ergysmati" ,"10" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider(R.color.lightGray,"sidritdritorja" ,"10" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider(R.color.lightGray,"ilirpirani" ,"1" ,R.mipmap.ic_orange_blitz);
        list.add(l);
        l =new RowDataProvider(R.color.lightGray,"arditmeti" ,"0" ,R.mipmap.ic_orange_blitz);
        list.add(l);
    }
}