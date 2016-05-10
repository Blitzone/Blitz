package com.example.bsaraci.blitzone.Search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class Search extends AppCompatActivity
{
    Toolbar searchToolbar ;
    TextView toolbarTitle;
    SearchView sv;
    RecyclerView rv;
    TextView chapter1,chapter2,chapter3,chapter4,chapter5;
    Topic topic;
    RelativeLayout chapterContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        searchToolbar = (Toolbar) findViewById(R.id.toolbar_of_search);
        toolbarTitle = (TextView) findViewById(R.id.search_toolbar_title);
        chapterContainer = (RelativeLayout) findViewById(R.id.chapterContainer);
        getTopic();

        rv= (RecyclerView) findViewById(R.id.myRecycler);
        sv= (SearchView) findViewById(R.id.mSearch);
        sv.setOnSearchClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toolbarTitle.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                visibleLayout(chapterContainer);
            }
        });
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                toolbarTitle.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                invisibleLayout(chapterContainer);
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

    public void initiateTextViews(){
        try{
            chapter1 = (TextView)findViewById(R.id.chapter1);
            chapter1.setText(topic.getPhotoChapterFromPosition(0).getChapterName());
            chapterCallback(chapter1);
            chapter2 = (TextView)findViewById(R.id.chapter2);
            chapter2.setText(topic.getPhotoChapterFromPosition(1).getChapterName());
            chapterCallback(chapter2);
            chapter3 = (TextView)findViewById(R.id.chapter3);
            chapter3.setText(topic.getPhotoChapterFromPosition(2).getChapterName());
            chapterCallback(chapter3);
            chapter4 = (TextView)findViewById(R.id.chapter4);
            chapter4.setText(topic.getPhotoChapterFromPosition(3).getChapterName());
            chapterCallback(chapter4);
            chapter5 = (TextView)findViewById(R.id.chapter5);
            chapter5.setText(topic.getPhotoChapterFromPosition(4).getChapterName());
            chapterCallback(chapter5);

        }
        catch (IndexOutOfBoundsException e){

        }

    }

    public void invisibleLayout(RelativeLayout relativeLayout){
        relativeLayout.setVisibility(View.GONE);
    }

    public void visibleLayout(RelativeLayout relativeLayout){
       relativeLayout.setVisibility(View.VISIBLE);
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

    public void chapterCallback (TextView textView){
        if(textView==chapter1){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class);
                    intent.putExtra("toolbarTitle", chapter1.getText());
                    startActivity(intent);
                }
            });
        }
        else if(textView==chapter2){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class);
                    intent.putExtra("toolbarTitle", chapter2.getText());
                    startActivity(intent);
                }
            });
        }
        else if(textView==chapter3){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class);
                    intent.putExtra("toolbarTitle", chapter3.getText());
                    startActivity(intent);
                }
            });
        }
        else if(textView==chapter4){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class);
                    intent.putExtra("toolbarTitle", chapter4.getText());
                    startActivity(intent);
                }
            });

        }
        else if(textView==chapter5){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class);
                    intent.putExtra("toolbarTitle", chapter5.getText());
                    startActivity(intent);
                }
            });

        }
    }

    private void getTopic() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateTopic(response);
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
                RequestURL.TOPIC,
                Request.Method.GET,
                null, //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);
    }

    private void updateTopic(JSONObject response) {
        try {
            JSONObject jsonTopic = (JSONObject)response.get("topic");
            JSONArray jsonChapterList = (JSONArray)response.get("chapters");

            Integer topicId     = (Integer)jsonTopic.get("id");
            String topicName    = jsonTopic.getString("name");
            topic = new Topic(topicId, topicName);


            updateChapters(jsonChapterList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateChapters(JSONArray chapterList) {

        try {
            int chapterListSize = chapterList.length();
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.color.lightGray);
            for (int i = 0; i < chapterListSize; i++) {
                JSONObject jsonChapter = (JSONObject) chapterList.getJSONObject(i);
                Integer chapterId = (Integer) jsonChapter.get("id");
                String chapterName = jsonChapter.get("name").toString();
                topic.addPhotoChapter(chapterId, chapterName);
                topic.addPhotoByChapterId(chapterId, bitmap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        initiateTextViews();
    }
}
