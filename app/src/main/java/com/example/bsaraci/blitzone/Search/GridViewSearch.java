package com.example.bsaraci.blitzone.Search;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.Profile.PhotoChapter;
import com.example.bsaraci.blitzone.Profile.ProfileRecyclerviewAdapter;
import com.example.bsaraci.blitzone.Profile.Topic;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;
import com.example.bsaraci.blitzone.ServerComm.RequestURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bsaraci on 5/9/2016.
 */
public class GridViewSearch extends AppCompatActivity {
    Toolbar gridViewToolbar ;
    TextView toolbarTitle;
    GridView gridView;
    GridViewAdapter gridAdapter;
    Integer topicId;
    Integer chapterId;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view_search);

        String title = getIntent().getExtras().getString("toolbarTitle");
        chapterId = getIntent().getExtras().getInt("chapterId");
        topicId = getIntent().getExtras().getInt("topicId");
        gridViewToolbar = (Toolbar) findViewById(R.id.toolbar_of_gridView_search);
        toolbarTitle = (TextView) findViewById(R.id.gridView_search_toolbar_title);
        toolbarTitle.setText(title);
        getGridPhotoChapters();

    }

    public void initiateGrid(final ArrayList<GridItem> gridItems){
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_view_item, gridItems);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(GridViewSearch.this, gridItems.get(position).getUser().getUsername(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getGridPhotoChapters() {

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateGridPhotoChapters(response);
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        };

        JWTManager jwtManager = new JWTManager(getApplicationContext());
        //Put everything in the request

        MRequest mRequest = new MRequest(
                RequestURL.GRID_SEARCH_PHOTOS,
                Request.Method.POST,
                getGridPhotoChapterParams(topicId,chapterId), //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);
    }

    private void updateGridPhotoChapters(JSONObject response) {

        try {
            ArrayList<GridItem> items = new ArrayList<>();
            JSONArray photoChapterGrid = (JSONArray) response.get("searchPhotoChapters");
            int photoChapterGridSize = photoChapterGrid.length();
            for (int i = 0; i < photoChapterGridSize; i++) {
                JSONObject jsonPhotoChapter = (JSONObject) photoChapterGrid.getJSONObject(i);
                GridItem gridItem = new GridItem();
                User u = new User();
                u.setUsername("username");
                gridItem.setUrl(RequestURL.IP_ADDRESS + jsonPhotoChapter.getString("image"));
                gridItem.setUser(u);
                items.add(gridItem);
            }
            initiateGrid(items);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getGridPhotoChapterParams(Integer topicId,Integer chapterId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("chapter", chapterId.toString());
        params.put("topic",topicId.toString());

        return new JSONObject(params);
    }

    public void searchFromGridViewSearch (View view){finish();}
}
