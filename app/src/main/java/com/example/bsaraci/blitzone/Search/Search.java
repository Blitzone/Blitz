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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;

public class Search extends AppCompatActivity
{
    Toolbar searchToolbar ;
    TextView toolbarTitle;
    SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        searchToolbar = (Toolbar) findViewById(R.id.toolbar_of_profile);
        toolbarTitle = (TextView) findViewById(R.id.profile_toolbar_title);
        
        sv= (SearchView) findViewById(R.id.mSearch);
        RecyclerView rv= (RecyclerView) findViewById(R.id.myRecycler);

        //SET ITS PROPETRIES
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        final SearchAdapter adapter=new SearchAdapter(this,getSearchModels());
        rv.setAdapter(adapter);

        //SEARCH
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

    //ADD PLAYERS TO ARRAYLIST
    private ArrayList<SearchModel> getSearchModels()
    {
        ArrayList<SearchModel> players=new ArrayList<>();
        SearchModel p=new SearchModel();
        p.setName("Ander Herera");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);

        p=new SearchModel();
        p.setName("David De Geaa");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);

        p=new SearchModel();
        p.setName("Michael Carrick");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);

        p=new SearchModel();
        p.setName("Juan Mata");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);

        p=new SearchModel();
        p.setName("Diego Costa");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);

        p=new SearchModel();
        p.setName("Andrea");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);

        p=new SearchModel();
        p.setName("Brajan");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);

        p=new SearchModel();
        p.setName("Mikel");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);

        p=new SearchModel();
        p.setName("Juli");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);

        p=new SearchModel();
        p.setName("Tea");
        p.setPos("Add");
        p.setImg(R.color.lightGray);
        players.add(p);


        return players;
    }


    public void blitzoneFromSearchButtonCallback (View view)
    {
        finish();
    }
}
