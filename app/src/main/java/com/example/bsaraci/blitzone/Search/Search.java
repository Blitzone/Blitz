package com.example.bsaraci.blitzone.Search;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;

public class Search extends AppCompatActivity
{
    Toolbar searchToolbar ;
    TextView toolbarTitle;
    SearchView sv;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        searchToolbar = (Toolbar) findViewById(R.id.toolbar_of_profile);
        toolbarTitle = (TextView) findViewById(R.id.search_toolbar_title);
        rv= (RecyclerView) findViewById(R.id.myRecycler);
        rv.setVisibility(View.GONE);
        sv= (SearchView) findViewById(R.id.mSearch);
        sv.setOnSearchClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toolbarTitle.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

            }
        });
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                toolbarTitle.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                return false;
            }
        });
        //SET ITS PROPETRIES
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        final SearchAdapter adapter=new SearchAdapter(this,getSearchModels());
        rv.setAdapter(adapter);

        //SEARCH
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Search.this,"text",Toast.LENGTH_LONG).show();
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                adapter.getFilter().filter(query);
                return false;
            }
        });



    }

    //ADD USERS TO ARRAYLIST
    private ArrayList<SearchModel> getSearchModels()
    {
        ArrayList<SearchModel> users=new ArrayList<>();
        SearchModel p=new SearchModel();
        p.setName("teasaraci");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);

        p=new SearchModel();
        p.setName("sarasaraci");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);

        p=new SearchModel();
        p.setName("enderballa");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);

        p=new SearchModel();
        p.setName("sidrodritorja");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);

        p=new SearchModel();
        p.setName("jonagolemi");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);

        p=new SearchModel();
        p.setName("andreamaka");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);

        p=new SearchModel();
        p.setName("julivuka");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);

        p=new SearchModel();
        p.setName("mikelvuka");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);

        p=new SearchModel();
        p.setName("testuser1");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);

        p=new SearchModel();
        p.setName("testuser2");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        users.add(p);


        return users;
    }


    public void blitzoneFromSearchButtonCallback (View view)
    {
        finish();
    }
}
