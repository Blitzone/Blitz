package com.example.bsaraci.blitzone.Search;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;

/**
 * Created by bsaraci on 5/9/2016.
 */
public class GridViewSearch extends AppCompatActivity {
    Toolbar gridViewToolbar ;
    TextView toolbarTitle;
    GridView gridView;
    GridViewAdapter gridAdapter;
    ArrayList<GridItem> items;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view_search);

        String title = getIntent().getExtras().getString("toolbarTitle");
        gridViewToolbar = (Toolbar) findViewById(R.id.toolbar_of_gridView_search);
        toolbarTitle = (TextView) findViewById(R.id.gridView_search_toolbar_title);
        toolbarTitle.setText(title);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_view_item, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(GridViewSearch.this,items.get(position).getUsername(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<GridItem> getData() {
        items = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            items.add(new GridItem(R.color.lightGray,"username "+(i+1)+""));
        }
        return items;
    }

    public void searchFromGridViewSearch (View view){finish();}
}
