package com.example.bsaraci.blitzone.Search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity
{
    Toolbar searchToolbar ;
    TextView toolbarTitle;
    SearchView sv;
    RecyclerView rv;
    TextView chapter1,chapter2,chapter3,chapter4,chapter5;
    Topic topic;
    RelativeLayout rvContainer;
    SearchAdapter adapter;
    ImageButton backIcon;
    View dividerBackIcon;
    Boolean isVisible;
    String query;
    int chapterId1,chapterId2,chapterId3,chapterId4,chapterId5;
    int topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        initiateComponents();
        getTopic();

    }

    public void initiateComponents(){
        searchToolbar = (Toolbar) findViewById(R.id.toolbar_of_search);
        setSupportActionBar(searchToolbar);
        backIcon = (ImageButton)findViewById(R.id.blitzone_from_search);
        dividerBackIcon = (View)findViewById(R.id.divider1);
        toolbarTitle = (TextView) findViewById(R.id.search_toolbar_title);
        rvContainer = (RelativeLayout) findViewById(R.id.container);
        isVisible=true;
    }
    public void initiateRV(ArrayList<SearchModel> searchModels){
        rv= (RecyclerView) findViewById(R.id.myRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter=new SearchAdapter(this,searchModels);
        rv.setAdapter(adapter);
    }
    public void initiateTextViews(Boolean isVisible){
        try{
            topicId=topic.getId();
            chapter1 = (TextView)findViewById(R.id.chapter1);
            chapter1.setText(topic.getPhotoChapterFromPosition(0).getChapterName());
            chapterId1=topic.getPhotoChapterFromPosition(0).getChapterId();
            chapterCallback(chapter1);
            if(isVisible){
                visibleTextView(chapter1);
            }
            else{
                invisibleTextView(chapter1);
            }
            chapter2 = (TextView)findViewById(R.id.chapter2);
            chapter2.setText(topic.getPhotoChapterFromPosition(1).getChapterName());
            chapterId2=topic.getPhotoChapterFromPosition(1).getChapterId();
            chapterCallback(chapter2);
            if(isVisible){
                visibleTextView(chapter2);
            }
            else{
                invisibleTextView(chapter2);
            }
            chapter3 = (TextView)findViewById(R.id.chapter3);
            chapter3.setText(topic.getPhotoChapterFromPosition(2).getChapterName());
            chapterId3=topic.getPhotoChapterFromPosition(2).getChapterId();
            chapterCallback(chapter3);
            if(isVisible){
                visibleTextView(chapter3);
            }
            else{
                invisibleTextView(chapter3);
            }
            chapter4 = (TextView)findViewById(R.id.chapter4);
            chapter4.setText(topic.getPhotoChapterFromPosition(3).getChapterName());
            chapterId4=topic.getPhotoChapterFromPosition(3).getChapterId();
            chapterCallback(chapter4);
            if(isVisible){
                visibleTextView(chapter4);
            }
            else{
                invisibleTextView(chapter4);
            }
            chapter5 = (TextView)findViewById(R.id.chapter5);
            chapter5.setText(topic.getPhotoChapterFromPosition(4).getChapterName());
            chapterId5=topic.getPhotoChapterFromPosition(4).getChapterId();
            chapterCallback(chapter5);
            if(isVisible){
                visibleTextView(chapter5);
            }
            else{
                invisibleTextView(chapter5);
            }

        }
        catch (IndexOutOfBoundsException e){

        }

    }

    public void invisibleTextView(TextView tv){
        tv.setVisibility(View.GONE);
    }

    public void visibleTextView(TextView tv){
        tv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv= (SearchView) menu.findItem(R.id.action_search).getActionView();
        changeSearchViewTextColor(sv);
        sv.setQueryHint("Search for users");
        sv.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Set styles for expanded state here
                initiateRV(new ArrayList<SearchModel>());
                toolbarTitle.setVisibility(View.GONE);
                rv.setVisibility(View.GONE);
                isVisible = false;
                initiateTextViews(isVisible);
                backIcon.setVisibility(View.GONE);
                dividerBackIcon.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Set styles for collapsed state here
                toolbarTitle.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                isVisible = true;
                initiateTextViews(isVisible);
                backIcon.setVisibility(View.VISIBLE);
                dividerBackIcon.setVisibility(View.VISIBLE);
                return true;
            }
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String q) {
                //FILTER AS YOU TYPE
//              adapter.getFilter().filter(query);
                query=q;
                getSearchUserList();
                rv.setVisibility(View.VISIBLE);
                return false;
            }
        });

        return true;
    }

    private JSONObject getSearchUserParams(String query){
        Map<String, String> params = new HashMap<String, String>();
        params.put("query", query);
        return new JSONObject(params);
    }


    private void getSearchUserList(){
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateSearchList(response);
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
                RequestURL.SEARCH_USER,
                Request.Method.POST,
                getSearchUserParams(query), //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(Search.this).addToRequestQueue(mRequest);
    }

    private void updateSearchList(JSONObject searchUser) {

        try {
            ArrayList<SearchModel> users=new ArrayList<>();
            JSONArray searchUserList=(JSONArray)searchUser.getJSONArray("userList");
            int searchUserListSize = searchUserList.length();
            if(!query.equals("")){
                for (int i = 0; i < searchUserListSize; i++) {
                    User u = new User();
                    SearchModel p=new SearchModel();
                    p.setUser(u);
                    p.getUser().setUsername(((JSONObject) searchUserList.get(i)).getString("user"));
                    p.setPos("Add");
                    p.getUser().setProfilePictureUrl(RequestURL.IP_ADDRESS + ((JSONObject) searchUserList.get(i)).getString("avatar"));
                    users.add(p);
                }
            }
            initiateRV(users);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.DKGRAY);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
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
                    intent.putExtra("chapterId",chapterId1);
                    intent.putExtra("topicId",topicId);
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
                    intent.putExtra("chapterId",chapterId2);
                    intent.putExtra("topicId",topicId);
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
                    intent.putExtra("chapterId",chapterId3);
                    intent.putExtra("topicId",topicId);
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
                    intent.putExtra("chapterId",chapterId4);
                    intent.putExtra("topicId",topicId);
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
                    intent.putExtra("chapterId",chapterId5);
                    intent.putExtra("topicId",topicId);
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
        initiateTextViews(isVisible);
    }
}
