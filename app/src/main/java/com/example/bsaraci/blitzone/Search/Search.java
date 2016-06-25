package com.example.bsaraci.blitzone.Search;

/**This class is the main intent of the Search. It is related to the classes : SearchAdapter, SearchModel, and all the classes
* of the server communication. In this class we handle the behaviour of the TextViews related to chapters that are coming from
* the server. Also it handles the SearchView and its actions when we expand it or collapse it. We call the server in this class
* to get our chapters and then we pass these chapters with putExtra to the GridViewSearch. We are also calling the server to get
* the searchUserList, the list of users that appears when we start typing on search.
*********************************************************************************************************************************
* BUGS : CHAPTERS DON'T APPEAR WHEN WE TYPE A LETTER IN THE SEARCH EDIT TEXT AND THEN WE CLICK ON BACK BUTTON OF THE SEARCH VIEW.
*       THE SAME THING WHEN WE CLICK ON THE BACK BUTTON OF THE CELLPHONE
*********************************************************************************************************************************
* AMELIORATION : MAYBE FIND ANOTHER WAY TO HIDE THE TEXTVIEWS AND MAKE THE rv APPEAR*/

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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

public class Search extends AppCompatActivity {

    private TextView toolbarTitle;                                          //The title of the searchToolbar. In this case 'Discover'
    private SearchView sv;                                                  //The Search View. (When u click the button search)
    private RecyclerView rv;                                                //The recycle view containing the users that you are searching
    private TextView chapter1,chapter2,chapter3,chapter4,chapter5;          //Maximum 5 TextView that correspond to our chapters
    private Topic topic;                                                    //The topic
    private ImageButton backIcon;                                           //It is the mint back icon in the searchToolbar
    private View dividerBackIcon;                                           //The divider near the back icon in the searchToolbar
    private Boolean isVisible;                                              //True if chapters are visible. False if not
    private int chapterId1,chapterId2,chapterId3,chapterId4,chapterId5;     //Chapter Id's. Numbers taken from the server
    private int topicId;                                                    //The id of the topic

/**
    THIS METHOD IS TRIGGERED WHEN THE INTENT IS CREATED
*/
    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        initiateComponents();
        getTopic();

    }

/**
    THIS METHOD INITIATES searchToolbar, backIcon, dividerBackIcon, toolbarTitle, rvContainer, SETS THE VISIBILITY OF THE CHAPTERS TRUE
*/
    public void initiateComponents(){
        Toolbar searchToolbar = (Toolbar) findViewById(R.id.toolbar_of_search);
        setSupportActionBar(searchToolbar);                                         //Sets the searchToolbar as an action bar
        backIcon = (ImageButton)findViewById(R.id.blitzone_from_search);            //Affects this xml element to the backIcon
        dividerBackIcon = findViewById(R.id.divider1);                              //Affects this xml element to the dividerBackIcon
        toolbarTitle = (TextView) findViewById(R.id.search_toolbar_title);          //Affects this xml element to the toolbarTitle

        @SuppressWarnings("unused")
        RelativeLayout rvContainer = (RelativeLayout) findViewById(R.id.container); //Affects this xml element to the rVContainer
        isVisible=true;                                                             //The chapters are visible
    }

/**
    THIS METHOD INITIATES THE RV WITH ALL ITS PROPERTIES (LayoutManager, adapter)
    @param searchModels, the ArrayList needed for the adapter constructor
*/
    public void initiateRV(ArrayList<SearchModel> searchModels){
        rv= (RecyclerView) findViewById(R.id.myRecycler);               //Affects this xml element to the rv
        rv.setLayoutManager(new LinearLayoutManager(this));             //Sets its LayoutManager
        SearchAdapter adapter = new SearchAdapter(this, searchModels);  //Creates the adapter
        rv.setAdapter(adapter);                                         //Sets the adapter of the rv
    }

/**
    THIS METHOD INITIATES THE CHAPTERS (TextViews). TRY AND CATCH BECAUSE WE DON'T ALWAYS HAVE 5 CHAPTERS COMING FROM THE SERVER
    @param isVisible, a boolean that tells if we should initiate visible or invisible
*/
    public void initiateTextViews(Boolean isVisible){
        try{

            topicId=topic.getId();                                                      //Affects the id to the topic
            chapter1 = (TextView)findViewById(R.id.chapter1);                           //Affects this xml element to the chapter1
            chapter1.setText(topic.getPhotoChapterFromPosition(0).getChapterName());    //Sets the text of chapter1. Takes the chapterName of the topic.photoChapters(0)
            chapterId1=topic.getPhotoChapterFromPosition(0).getChapterId();             //Sets the id of chapter1. Takes the chapterId of the topic.photoChapters(0)
            chapterCallback(chapter1);                                                  //Triggers the method chapterCallback

            if(isVisible){
                visibleTextView(chapter1);                                              //If true then chapter1 is visible
            }

            else{
                invisibleTextView(chapter1);                                            //If false then chapter1 is invisible
            }

            chapter2 = (TextView)findViewById(R.id.chapter2);                           //Affects this xml element to the chapter2
            chapter2.setText(topic.getPhotoChapterFromPosition(1).getChapterName());    //Sets the text of chapter2. Takes the chapterName of the topic.photoChapters(1)
            chapterId2=topic.getPhotoChapterFromPosition(1).getChapterId();             //Sets the id of chapter2. Takes the chapterId of the topic.photoChapters(1)
            chapterCallback(chapter2);                                                  //Triggers the method chapterCallback

            if(isVisible){
                visibleTextView(chapter2);                                              //If true then chapter2 is visible
            }

            else{
                invisibleTextView(chapter2);                                            //If false then chapter2 is invisible
            }

            chapter3 = (TextView)findViewById(R.id.chapter3);                           //Affects this xml element to the chapter3
            chapter3.setText(topic.getPhotoChapterFromPosition(2).getChapterName());    //Sets the text of chapter3. Takes the chapterName of the topic.photoChapters(2)
            chapterId3=topic.getPhotoChapterFromPosition(2).getChapterId();             //Sets the id of chapter3. Takes the chapterId of the topic.photoChapters(2)
            chapterCallback(chapter3);                                                  //Triggers the method chapterCallback

            if(isVisible){
                visibleTextView(chapter3);                                              //If true then chapter3 is visible
            }

            else{
                invisibleTextView(chapter3);                                            //If false then chapter3 is invisible
            }

            chapter4 = (TextView)findViewById(R.id.chapter4);                           //Affects this xml element to the chapter3
            chapter4.setText(topic.getPhotoChapterFromPosition(3).getChapterName());    //Sets the text of chapter4. Takes the chapterName of the topic.photoChapters(3)
            chapterId4=topic.getPhotoChapterFromPosition(3).getChapterId();             //Sets the id of chapter4. Takes the chapterId of the topic.photoChapters(3)
            chapterCallback(chapter4);                                                  //Triggers the method chapterCallback

            if(isVisible){
                visibleTextView(chapter4);                                              //If true then chapter4 is visible
            }

            else{
                invisibleTextView(chapter4);                                            //If false then chapter4 is invisible
            }


            chapter5 = (TextView)findViewById(R.id.chapter5);                           //Affects this xml element to the chapter3
            chapter5.setText(topic.getPhotoChapterFromPosition(4).getChapterName());    //Sets the text of chapter5. Takes the chapterName of the topic.photoChapters(4)
            chapterId5=topic.getPhotoChapterFromPosition(4).getChapterId();             //Sets the id of chapter5. Takes the chapterId of the topic.photoChapters(4)
            chapterCallback(chapter5);                                                  //Triggers the method chapterCallback

            if(isVisible){
                visibleTextView(chapter5);                                              //If true then chapter5 is visible
            }

            else{
                invisibleTextView(chapter5);                                            //If false then chapter5 is invisible
            }

        }

        catch (IndexOutOfBoundsException e){    //Catches all the outOfBoundsException. Not every time the server is giving us 5 chapters.
                                                //So probably one case of topic.photoChapters is empty.
                                                //With this we are excluding the empty cases
        }

    }

/**
    THIS METHOD SETS THE CHAPTERS (TextViews), INVISIBLE
    @param tv, the TextView that we want to set invisible
*/
    public void invisibleTextView(TextView tv){
        tv.setVisibility(View.GONE);    //tv is now invisible
    }

/**
    THIS METHOD SETS THE CHAPTERS (TextViews), VISIBLE
    @param tv, the TextView that we want to set visible
*/
    public void visibleTextView(TextView tv){
        tv.setVisibility(View.VISIBLE);     //tv is now visible
    }

/**
    THIS METHOD CHANGES THE COLOR OF THE TEXTS WHEN WE TYPE SOMETHING IN THE EditText OF THE SearchView
    @param view, the view that we want to change its colour
*/
    private void changeSearchViewTextColor(View view) {
        if (view != null) {                                                 //Enters the loop if the view is not null

            if (view instanceof TextView) {                                 //If this view is a TextView
                ((TextView) view).setTextColor(Color.DKGRAY);               //Sets the color of this TextView

            }
            else if (view instanceof ViewGroup) {                           //If this this view is a ViewGroup
                ViewGroup viewGroup = (ViewGroup) view;                     //Creates a ViewGroup

                for (int i = 0; i < viewGroup.getChildCount(); i++) {       //Sets the color of every child component of the viewGroup
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

/**
    THIS METHOD INITIATES THE SearchView
    @param menu, the menu where we have our search view
*/
    public void initiateSearchView (Menu menu){
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE); //Associates searchable configuration with the SearchView
        sv= (SearchView) menu.findItem(R.id.action_search).getActionView();                     //Affects this menu element to sv
        changeSearchViewTextColor(sv);                                                          //Changes the color of the text in sv
        sv.setQueryHint("Search for users");                                                    //Sets the query hint
        sv.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));              //Sets the SearchableInfo for the sv
    }

/**
    THIS METHOD HANDLES THE BEHAVIOUR OF THE SEARCHVIEW
    @param menu, the menu we want to handle its behaviour
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);                     //Affects to the menu, R.menu.menu_search. Look at res.menu

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);    //Affects to searchMenuItem, R.id.action_search Look at res.menu
        initiateSearchView(menu);                                       //Triggers the initiateSearchView method.

        //The behaviour when we open or close the sv
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {

            //The behaviour when we open the sv. Set styles for expanded state here
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                initiateRV(new ArrayList<SearchModel>());       //Initiates rv with a blank list
                toolbarTitle.setVisibility(View.GONE);          //Makes the toolbarTitle invisible
                backIcon.setVisibility(View.GONE);              //Makes the backIcon invisible
                dividerBackIcon.setVisibility(View.GONE);       //Makes the divideBarcIcon invisible
                rv.setVisibility(View.VISIBLE);                 //Sets the rv visible
                isVisible = false;                              //TextViews are not visible because this is false
                //noinspection ConstantConditions
                initiateTextViews(isVisible);                   //Triggers this method
                return true;
            }

            //The behaviour when we close the sv. Set styles for collapsed state here
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                toolbarTitle.setVisibility(View.VISIBLE);     //Sets the visibility of the toolbarTitle to visible
                backIcon.setVisibility(View.VISIBLE);         //Makes the backIcon visible
                dividerBackIcon.setVisibility(View.VISIBLE);  //Makes the divider back icon visble
                rv.setVisibility(View.GONE);                  //Makes the rv invisible
                isVisible = true;                             //TextViews are visible because this is true

                //noinspection ConstantConditions
                initiateTextViews(isVisible);                 //Triggers this method
                return true;
            }
        });

        //Sets a listener when we are changing the query text and when we submit
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //When we submit the query
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //When we modify the query
            @Override
            public boolean onQueryTextChange(String q) {
                getSearchUserList(q);               //Gets the user list from the server
                rv.setVisibility(View.VISIBLE);     //Sets the visibility of rv to visible
                return false;
            }
        });

        return true;
    }

/**
    THIS METHOD PUTS THE PARAMS NEEDED FOR THE MRequest IN A JSONObject
    @param query, the query we put on the key 'query'
    @return the params for the MRequest
*/
    private JSONObject getSearchUserParams(String query){
        Map<String, String> params = new HashMap<>(); //Creates the HashMap
        params.put("query", query);                                 //Puts in this HashMap , query to the key 'query'
        return new JSONObject(params);                              //Returns JSONObject with the params
    }

/**
    THIS METHOD SENDS THE MRequest TO GET THE RESPONSE FOR THE searchUserList WITH THE PARAMS TAKEN FROM getSearchUserParams
    @param query, the query we put in the MRequest
*/
    private void getSearchUserList(final String query){

        //Adds a listener to the response. In this case the response of the server will trigger the method updateSearchList
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateSearchList(response,query); //Triggers this method
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        };

        JWTManager jwtManager = new JWTManager(getApplicationContext()); //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.SEARCH_USER,     //Url of the request (/accounts/searchUser/)
                Request.Method.POST,        //Type of the method
                getSearchUserParams(query), //Put the parameters of the request here (JSONObject format)
                listener,                   //listener
                errorListener,              //errorListener
                jwtManager                  //jwtManager
        );

        RequestQueueSingleton.getInstance(Search.this).addToRequestQueue(mRequest); //Sends the request
    }

/**
    THIS METHOD UPDATES THE SEARCH LIST. IN PARAMETER IS THE RESPONSE FROM THE SERVER AND THE QUERY INSERTED
    @param searchUser, JSONObject given from the server that we want to take its elements (response)
    @param query, the query related to what we type in the search edit text
*/
    private void updateSearchList(JSONObject searchUser, String query) {

        //We user a try and catch to handle JSONExceptions
        try {

            ArrayList<SearchModel> users=new ArrayList<>();                             //Creates a new ArrayList<SearchModel>
            JSONArray searchUserList= searchUser.getJSONArray("userList");    //Takes the JSONArray from the response
            int searchUserListSize = searchUserList.length();                           //Length of searchUserList

            //Enters in the loop if query is not empty
            if(!query.equals("")){

                //Browses every element of the searchUserList
                for (int i = 0; i < searchUserListSize; i++) {

                    User u = new User();                //Creates a new User
                    SearchModel p=new SearchModel();    //Creates a new SearchModel
                    p.setUser(u);                       //Sets the user of this SearchMode
                    p.getUser().setUsername(((JSONObject) searchUserList.get(i)).getString("user")); //Sets the username of User u
                    p.setAdd("Add");                    //Sets the text to the add button
                    p.setRemove("Remove");              //Sets the text to the remove button
                    p.getUser().setProfilePictureUrl(RequestURL.IP_ADDRESS + ((JSONObject) searchUserList.get(i)).getString("avatar")); //Sets the profile picture Url for User u
                    p.getUser().setFollowing(((JSONObject) searchUserList.get(i)).getBoolean("is_followed")); //Sets the boolean following for the User u
                    users.add(p);                       //Adds p with all its properties to users
                }
            }
            initiateRV(users);  //Initiates rv with users (ArrayList<SearchModel>)
        }

        //In case of error
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

/**
    THIS METHOD IS TRIGGERED WHEN WE CLICK TO A CHAPTER
    @param textView, the TextView that we will click
*/
    public void chapterCallback (TextView textView){

        //Enters if we click on chapter1
        if(textView==chapter1){

            //Sets the onClickListener
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class); //Creates the next Intent (GridViewSearch)
                    intent.putExtra("toolbarTitle", chapter1.getText());           //chapter1.getText() will serve as a toolbarTitle for GridViewSearch
                    intent.putExtra("chapterId",chapterId1);                       //Passes the chapterId with putExtra
                    intent.putExtra("topicId",topicId);                            //Passes the topicId with putExtra
                    startActivity(intent);                                         //Starts GridViewSearch
                }
            });
        }

        //Enters if we click on chapter2
        else if(textView==chapter2){

            //Sets the onClickListener
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class);  //Creates the next Intent (GridViewSearch)
                    intent.putExtra("toolbarTitle", chapter2.getText());            //chapter2.getText() will serve as a toolbarTitle for GridViewSearch
                    intent.putExtra("chapterId",chapterId2);                        //Passes the chapterId with putExtra
                    intent.putExtra("topicId",topicId);                             //Passes the topicId with putExtra
                    startActivity(intent);                                          //Starts GridViewSearch
                }
            });
        }

        //Enters if we click on chapter3
        else if(textView==chapter3){

            //Sets the onClickListener
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class);  //Creates the next Intent (GridViewSearch)
                    intent.putExtra("toolbarTitle", chapter3.getText());            //chapter3.getText() will serve as a toolbarTitle for GridViewSearch
                    intent.putExtra("chapterId",chapterId3);                        //Passes the chapterId with putExtra
                    intent.putExtra("topicId",topicId);                             //Passes the topicId with putExtra
                    startActivity(intent);                                          //Starts GridViewSearch
                }
            });
        }

        //Enters if we click on chapter4
        else if(textView==chapter4){

            //Sets the onClickListener
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class);  //Creates the next Intent (GridViewSearch)
                    intent.putExtra("toolbarTitle", chapter4.getText());            //chapter4.getText() will serve as a toolbarTitle for GridViewSearch
                    intent.putExtra("chapterId",chapterId4);                        //Passes the chapterId with putExtra
                    intent.putExtra("topicId",topicId);                             //Passes the topicId with putExtra
                    startActivity(intent);                                          //Starts GridViewSearch
                }
            });

        }

        //Enters if we click on chapter4
        else if(textView==chapter5){

            //Sets the onClickListener
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, GridViewSearch.class);  //Creates the next Intent (GridViewSearch)
                    intent.putExtra("toolbarTitle", chapter5.getText());            //chapter5.getText() will serve as a toolbarTitle for GridViewSearch
                    intent.putExtra("chapterId",chapterId5);                        //Passes the chapterId with putExtra
                    intent.putExtra("topicId",topicId);                             //Passes the topicId with putExtra
                    startActivity(intent);                                          //Starts GridViewSearch
                }
            });

        }
    }

/**
    THIS METHOD SENDS THE MRequest TO GET THE RESPONSE FOR THE TOPIC
*/
    private void getTopic() {

        //Adds a listener to the response. In this case the response of the server will trigger the method updateTopic
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

        JWTManager jwtManager = new JWTManager(getApplicationContext());    //Creates the new JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.TOPIC,       //The Url (/images/topic/)
                Request.Method.GET,     //Type of the method
                null,                   //Put the parameters of the request here (JSONObject format)
                listener,               //The listener
                errorListener,          //The errorListener
                jwtManager              //The jwtManager
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);    //Sends the request
    }

/**
    THIS METHOD UPDATES THE TOPIC. IN PARAMETER IS THE RESPONSE FROM THE SERVER
    @param response, the response from the server
*/
    private void updateTopic(JSONObject response) {

        //We user a try and catch to handle JSONExceptions
        try {
            JSONObject jsonTopic = (JSONObject)response.get("topic");         //Takes the topic from the response
            JSONArray jsonChapterList = (JSONArray)response.get("chapters");  //Takes the list of chapters from response
            Integer topicId = (Integer)jsonTopic.get("id");                   //Takes the topic id from the topic (taken from response)
            String topicName = jsonTopic.getString("name");                   //Takes the topic name from the topic (taken from response)
            topic = new Topic(topicId, topicName);                            //Creates a topic passing in its constructor the id and the name

            updateChapters(jsonChapterList); //Triggers the method passing in its parameter the chapterList from the response
        }

        //In case of error
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

/**
    THIS METHOD UPDATES THE CHAPTERS. IN PARAMETER IS THE chapterList TAKEN FROM THE RESPONSE
    @param chapterList, the ArrayList which has all the chapters from the server
*/
    private void updateChapters(JSONArray chapterList) {

        //We user a try and catch to handle JSONExceptions
        try {

            int chapterListSize = chapterList.length(); //The length of the chapterList
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.color.lightGray); //It is used to create a bitmap even though it wont show a gray image

            //Browses the chapterList
            for (int i = 0; i < chapterListSize; i++) {
                JSONObject jsonChapter = chapterList.getJSONObject(i);              //Every item of this list is a chapter
                Integer chapterId = (Integer) jsonChapter.get("id");                //Takes the id of the jsonChapter created
                String chapterName = jsonChapter.get("name").toString();            //Takes the name of the jsonChapter created
                topic.addPhotoChapter(chapterId, chapterName);                      //Adds in topic.photoChapters a new PhotoChapter with its Id and name
                topic.addPhotoByChapterId(chapterId, bitmap);                       //Sets the bitmap of the PhotoChapter whose id is chapterId
            }

        }

        //In case of error
        catch (JSONException e) {
            e.printStackTrace();
        }

        initiateTextViews(isVisible);   //Initiates the chapters (TextViews)
    }

/**
    THIS METHOD IS THE CALLBACK OF THE backIcon. GOES BACK TO BLITZONE
    @param view, the view we are clicking. This case tha backIcon
*/
    public void blitzoneFromSearchButtonCallback (View view)
    {
        finish();
    }

}
