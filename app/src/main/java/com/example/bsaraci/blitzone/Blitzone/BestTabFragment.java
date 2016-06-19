package com.example.bsaraci.blitzone.Blitzone;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BestTabFragment extends Fragment {

    private List<RowDataProvider> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleviewAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.best_tab_content, container, false);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        RecyclerView recyclerView = (RecyclerView) rv.findViewById(R.id.bestList);
        prepareData();
        rv.setAdapter(new RecyclerowAdapter(list));

        return rv;

    }

    public void prepareData (){
        for(int i = 0; i<10 ; i++){
            RowDataProvider rowDataProvider = new RowDataProvider();
            User u = new User();
            Random random = new Random();
            int r = random.nextInt(10);
            u.setUsername("user " + (i+1));
            u.setBlitz(r);
            u.setProfilePicture(BitmapFactory.decodeResource(this.getResources(), R.drawable.b));
            u.setFollowing(true);
            rowDataProvider.setUser(u);
            list.add(rowDataProvider);
        }
    }
}