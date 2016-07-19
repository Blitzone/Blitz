package com.example.bsaraci.blitzone.Search;

/**This class represents the intent of the GridViewSearch. Here users can search for other users by looking the photos of the same
* chapter. When u click on a grid element it animates a fullsize image of this photo. This class updates the adapter of the grid
* and also sends some request to the server like; taking chapter photos, follow or unfollow a user etc .
**********************************************************************************************************************************
* BUGS : ADD OR REMOVE IS NOT UPDATING CORRECTLY
**********************************************************************************************************************************
* AMELIORATION : FIND A NEW WAY TO UPDATE THE ADD OR REMOVE BUTTON AFTER REOPENING THE FULLSIZE BECAUSE WE CALL EVERY TIME THE
                 METHOD getPhotoChapter()*/

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.bsaraci.blitzone.Profile.RecyclerItemClickListener;
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

public class GridViewSearch extends AppCompatActivity {

    private TextView toolbarTitle;                  //The title of the toolbar corresponding to the chapter name
    private Integer topicId;                        //The Id of the topic
    private Integer chapterId;                      //The Id of the chapter
    private Animator mCurrentAnimator;              //The animator when we click to a grid element
    private int mShortAnimationDuration;            //The duration of the animation
    private TextView usernameInToolbar;             //The username in the toolbar when we click to a grid element
    private boolean isFullsize;                     //True if we click a grid element
    private View thumbView;                         //The small picture in grid
    private ImageButton gridViewFromFullsize;       //The back button when we are in full size. Goes back to the grid
    private ImageButton backFromGrid;               //The back button when we are in the grid layout. Goes back to search
    private TextView points;                        //Points when we are in fullsize
    private ImageView blitz;                        //The small orange blitz near points
    private Button add;                             //Add button when the photo is in fullsize
    private Button remove;                          //Remove button when the photo is in fullsize
    private String uname;                           //The string of the username
    private RelativeLayout container;
    RecyclerView gridView;
    GridViewAdapter gridAdapter;
    ArrayList<String> pointsList = new ArrayList<>();
    ArrayList<Boolean> followed = new ArrayList<>();
    int globalPosition;

    /**
    THIS METHOD IS TRIGGERED WHEN THE INTENT IS CREATED
*/
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view_search);
        initiateComponents();       //Initiates the components of this intent
        getGridPhotoChapters();     //Triggers the method which sends the request to the server
    }

/**
    METHOD THAT INITIATES THE INTENT COMPONENTS
*/
    public void initiateComponents(){
        String title = getIntent().getExtras().getString("toolbarTitle");                   //Title of the toolbar. Takes it from the previous intent
        chapterId = getIntent().getExtras().getInt("chapterId");                            //The id of the chapter. Takes it from the previous intent
        topicId = getIntent().getExtras().getInt("topicId");                                //The id of the topic. Takes it from the previous intent

        @SuppressWarnings("unused")
        Toolbar gridViewToolbar = (Toolbar) findViewById(R.id.toolbar_of_gridView_search);  //Affects this xml element to the gridViewToolbar

        toolbarTitle = (TextView) findViewById(R.id.gridView_search_toolbar_title);         //Affects this xml element to the toolbarTitle
        toolbarTitle.setText(title);                                                        //Sets the toolbarTitle
        usernameInToolbar = (TextView) findViewById(R.id.username_search_toolbar);          //Affects this xml element to the usernameInToolbar
        gridViewFromFullsize = (ImageButton) findViewById(R.id.gridView_from_fullsize);     //Affects this xml element to the gridViewFromFullsize
        backFromGrid = (ImageButton) findViewById(R.id.search_from_gridViewSearch);         //Affects this xml element to the backFromGrid
        points = (TextView) findViewById(R.id.pointsGridView);                              //Affects this xml element to the points
        blitz = (ImageView) findViewById(R.id.blitzGridView);                               //Affects this xml element to the blitz
        add = (Button) findViewById(R.id.addGridView);                                      //Affects this xml element to the add button
        remove = (Button) findViewById(R.id.removeGridView);                                //Affects this xml element to the remove button
        container = (RelativeLayout) findViewById(R.id.container);
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime); //Sets the animation duration
    }

/**
    METHOD THAT INITIATES THE GRID
    @param gridItems, the ArrayList that the adapter takes in its constructor
*/
    public void initiateGrid(final ArrayList<GridItem> gridItems){
        gridView = (RecyclerView)findViewById(R.id.gridView);
        gridView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        int spacingInPixels =1;
        gridView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        gridAdapter = new GridViewAdapter(this,gridItems);
        gridView.setAdapter(gridAdapter);   //Sets the adapter of the gridView
        gridView.setHasFixedSize(true);

        //Sets an ItemClickListener for the gridView
        gridView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, gridView, new RecyclerItemClickListener.OnItemClickListener() {
                    public void onItemClick(View view, int position) {
                        thumbView = view;
                        isFullsize = true;                              //Now we are in full size
                        String url = gridItems.get(position).getUrl();  //Sets the url of the picture
                        User u = gridItems.get(position).getUser();     //Sets the user who posted that picture
                        uname = u.getUsername();                        //Gets the username of the user u
                        usernameInToolbar.setText(uname);               //Sets the username of the username TextView when we are in full size
                        points.setText(pointsList.get(position));
                        globalPosition=position;
                        if(followed.get(position)){
                            add.setVisibility(View.GONE);
                            remove.setVisibility(View.VISIBLE);
                        }
                        else{
                            add.setVisibility(View.VISIBLE);
                            remove.setVisibility(View.GONE);
                        }

                        //Triggers the method of animation
                        zoomImageFromThumb(container,points, blitz, add, remove, backFromGrid, gridViewFromFullsize, toolbarTitle, usernameInToolbar, view, url);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                })
        );


    }

/**
    METHOD THAT ANIMATES THE PASSAGE FROM THUMBVIEW TO FULLSIZE
    @param pts, TextView of points
    @param blz, ImageView of orange blitz icon
    @param addUser, add User button
    @param removeUser, remove User button
    @param backFromGrid, back Button to go on discover layout again
    @param backFromFullSize, back Button to go on grid layout again
    @param title, chapter title
    @param username, username in toolbar
    @param thumbView, the grid square that holds the photo
    @param url, the url of the photo
*/
    private void zoomImageFromThumb(final View container,final View pts, final View blz, final View addUser, final View removeUser, final View backFromGrid, final View backFromFullSize, final View title, final View username,final View thumbView, String url) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(R.id.expandedImage);
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.color.white)
                .dontAnimate()
                .into(new GlideDrawableImageViewTarget(expandedImageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        //never called
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Toast.makeText(GridViewSearch.this, "Error loading profile picture", Toast.LENGTH_SHORT).show();
                        //never called
                    }
                });

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        container.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and other components and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        gridView.setVisibility(View.GONE);
        container.setBackgroundColor(Color.WHITE);
        thumbView.setAlpha(0f);
        title.setAlpha(0f);
        backFromGrid.setAlpha(0f);
        backFromFullSize.setVisibility(View.VISIBLE);
        username.setVisibility(View.VISIBLE);
        pts.setVisibility(View.VISIBLE);
        blz.setVisibility(View.VISIBLE);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        gridView.setVisibility(View.VISIBLE);
                        container.setBackgroundColor(Color.TRANSPARENT);
                        thumbView.setAlpha(1f);
                        title.setAlpha(1f);
                        backFromGrid.setAlpha(1f);
                        backFromFullSize.setVisibility(View.GONE);
                        username.setVisibility(View.GONE);
                        pts.setVisibility(View.GONE);
                        blz.setVisibility(View.GONE);
                        addUser.setVisibility(View.GONE);
                        removeUser.setVisibility(View.GONE);
                        expandedImageView.setVisibility(View.GONE);
                        /*getGridPhotoChapters();*/
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        gridView.setVisibility(View.VISIBLE);
                        container.setBackgroundColor(Color.TRANSPARENT);
                        thumbView.setAlpha(1f);
                        title.setAlpha(1f);
                        backFromGrid.setAlpha(1f);
                        backFromFullSize.setVisibility(View.GONE);
                        username.setVisibility(View.GONE);
                        pts.setVisibility(View.GONE);
                        blz.setVisibility(View.GONE);
                        addUser.setVisibility(View.GONE);
                        removeUser.setVisibility(View.GONE);
                        expandedImageView.setVisibility(View.GONE);
                        /*getGridPhotoChapters();*/
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

/**
    METHOD THAT SEND REQUEST TO THE SERVER TO GET THE PHOTO CHAPTERS
*/
    private void getGridPhotoChapters() {

        //Adds a listener to the response. In this case the response of the server will trigger the method updateGridPhotoChapters
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

        JWTManager jwtManager = new JWTManager(getApplicationContext());    //Creates the JWTMangaer

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.GRID_SEARCH_PHOTOS,                  //The url (/images/searchPhotoChapters/)
                Request.Method.POST,                            //Type of the method
                getGridPhotoChapterParams(topicId,chapterId),   //Put the parameters of the request here (JSONObject format)
                listener,                                       //The listener
                errorListener,                                  //The errorListener
                jwtManager                                      //The JWTManager
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);    //Sends the request
    }

/**
    METHOD THAT UPDATES THE GRID PHOTO CHAPTERS WHEN THE SERVER RESPONDS
    @param response, what the servers gives us
*/
    private void updateGridPhotoChapters(JSONObject response) {

        //We use try and catch to handle JSONExceptions
        try {

            final ArrayList<GridItem> items = new ArrayList<>();                                //Creates a new ArrayList of GridItems
            JSONArray photoChapterGrid = (JSONArray) response.get("searchPhotoChapters");       //Takes the JSONArray from response
            int photoChapterGridSize = photoChapterGrid.length();                               //The length of JSONArray

            //Browses the JSONArray
            for (int i = 0; i < photoChapterGridSize; i++) {

                JSONObject jsonPhotoChapter = photoChapterGrid.getJSONObject(i);                //Every object of an array is a jsonPhotoChapter
                JSONObject jsonUser = jsonPhotoChapter.getJSONObject("user");                   //Takes the user that corresponds to the jsonPhotoChapter
                String username=jsonUser.getString("user");                                     //Takes the username corresponding to the user
                Integer blitzCount = jsonUser.getInt("blitzCount");                             //Takes the number of blitzes corresponding to the user
                boolean isFollowed = jsonPhotoChapter.getBoolean("is_followed");                //Takes information if the user is followed or not

                GridItem gridItem = new GridItem();                                             //Creates a new gridItem
                User u = new User();                                                            //Creates a new user
                u.setUsername(username);                                                        //Sets the username taken from the server
                u.setBlitz(blitzCount);                                                         //Sets the blitzes taken from the server
                String blz = blitzCount.toString();                                             //Convert blitzes to string
                pointsList.add(i, blz);                                                            //Sets the text of points
                u.setFollowing(isFollowed);                                                     //Sets the following boolean on user taken from server
                followed.add(i, u.isFollowing());
                gridItem.setUrl(RequestURL.IP_ADDRESS + jsonPhotoChapter.getString("image"));   //Sets the image url taken from server
                gridItem.setUser(u);                                                            //Sets an user for the gridItem
                items.add(gridItem);                                                            //Adds the gridItem created
            }
            initiateGrid(items);    //Initiates the grid passing the list that we filled

        }

        //In case of an JSONException
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

/**
    METHOD THAT TRIGGERS WHEN WE CLICK THE BACK BUTTON OF OUR PHONE
*/
    @Override
    public void onBackPressed() {

        //Enters if it is in full size
        if(isFullsize){

            initiateComponents();                                                               //Initiates components of the GridViewSearch
            gridView.setVisibility(View.VISIBLE);
            container.setBackgroundColor(Color.TRANSPARENT);
            final ImageView expandedImageView = (ImageView) findViewById(R.id.expandedImage);   //Affects this xml file to expandedImageView
            thumbView.setAlpha(1f);                                                             //thumbView is now visible
            toolbarTitle.setAlpha(1f);                                                          //toolbarTitle is now visible
            backFromGrid.setAlpha(1f);                                                          //backFromGrid is now visible
            gridViewFromFullsize.setVisibility(View.GONE);                                      //gridFromFullSize is now invisible
            expandedImageView.setVisibility(View.GONE);                                         //expandedImageView is now invisible
            points.setVisibility(View.GONE);                                                    //points is now invisible
            blitz.setVisibility(View.GONE);                                                     //blitz is now invisible
            add.setVisibility(View.GONE);                                                       //add is now invisible
            remove.setVisibility(View.GONE);                                                    //remove is now invisible
            usernameInToolbar.setVisibility(View.GONE);                                         //username is now invisible
            isFullsize=false;                                                                   //We're not in full size anymore
            /*getGridPhotoChapters();*/                                                             //Reinitialise the grid photos
        }

        //Enters if it is not in full size
        else{
            finish(); //Goes back to Discover
        }
    }

/**
    METHOD THAT HANDLES THE BEHAVIOUR WHEN THE ACTIVITY RESUMES
*/
    @Override
    public void onResume(){
        super.onResume();
    }

/**
    THIS METHOD PUTS THE PARAMS NEEDED FOR THE MRequest IN A JSONObject
    @param topicId, the id of the topic
    @param chapterId, the id of the chapter
    @return the params needed for the MRequest
*/
    private JSONObject getGridPhotoChapterParams(Integer topicId,Integer chapterId) {

        Map<String, String> params = new HashMap<>(); //Creates the HashMap
        params.put("chapter", chapterId.toString());                //Puts the chapterId to the key 'chapter'
        params.put("topic", topicId.toString());                    //Puts the topicId to the key 'topic'

        return new JSONObject(params);
    }

/**
    METHOD THAT HANDLES THE CLICK ON THE BACK BUTTON WHEN WE ARE IN THE GRID LAYOUT
    @param view, the view we are clicking, this case the button back
*/
    public void searchFromGridViewSearch (View view){finish();}

/**
    METHOD THAT HANDLES THE CLICK OF THE BACK BUTTON WHEN WE ARE IN THE FULLSIZE LAYOUT
    @param view, the view we are clicking, this case the button back from fullsize
*/
    public void gridFromFullsize (View view){

        initiateComponents();                                                               //Initiates components of the GridViewSearch
        final ImageView expandedImageView = (ImageView) findViewById(R.id.expandedImage);   //Affects this xml file to expandedImageView
        gridView.setVisibility(View.VISIBLE);
        container.setBackgroundColor(Color.TRANSPARENT);
        thumbView.setAlpha(1f);                                                             //thumbView is now visible
        toolbarTitle.setAlpha(1f);                                                          //toolbarTitle is now visible
        backFromGrid.setAlpha(1f);                                                          //backFromGrid is now visible
        gridViewFromFullsize.setVisibility(View.GONE);                                      //gridFromFullSize is now invisible
        expandedImageView.setVisibility(View.GONE);                                         //expandedImageView is now invisible
        points.setVisibility(View.GONE);                                                    //points is now invisible
        blitz.setVisibility(View.GONE);                                                     //blitz is now invisible
        add.setVisibility(View.GONE);                                                       //add is now invisible
        remove.setVisibility(View.GONE);                                                    //remove is now invisible
        usernameInToolbar.setVisibility(View.GONE);                                         //username is now invisible
        isFullsize=false;                                                                   //We're not in full size anymore
        /*getGridPhotoChapters();*/                                                            //Reinitialise the grid photos
    }

/**
    METHOD THAT HANDLES THE CLICK OF THE ADD BUTTON WHEN WE ARE IN THE FULLSIZE LAYOUT
    @param view, the view we are clicking, this case the add button in fullsize
*/
    public void addCallback(View view){

        followed.set(globalPosition,true);
        view.setVisibility(View.GONE);          //Sets the add button invisible
        getFollowUser(uname);                   //Send the request followUser to the server
        remove.setVisibility(View.VISIBLE);     //Sets the remove button visible
    }

/**
    METHOD THAT HANDLES THE CLICK OF THE REMOVE BUTTON WHEN WE ARE IN THE FULLSIZE LAYOUT
    @param view, the view we are clicking, this case the remove button in fullsize
*/
    public void removeCallback(View view){

        followed.set(globalPosition,false);
        view.setVisibility(View.GONE);          //Sets the remove button invisible
        getUnfollowUser(uname);                 //Sends the request unfollowUser to the server
        add.setVisibility(View.VISIBLE);        //Sets the add button visible
    }

/**
    THIS METHOD CREATES THE PARAMS NEEDED FOR THE MRequest getFollowUser
    @param username, the username to put in the HashMap
    @return the params needed for the MRequest
*/
    private JSONObject getFollowUserParams(String username){
        Map<String, String> params = new HashMap<>();     //Creates the HashMap
        params.put("followedUser", username);                           //Puts username in the key 'followedUser'
        return new JSONObject(params);
    }

/**
    THIS METHOD SENDS THE MRequest TO FOLLOW AN USER. TAKES IN PARAMETER THE USERNAME NEEDED TO COMPLETE THE REQUEST
    @param username, the username of the user we want to follow
*/
    private void getFollowUser(String username){

        //Adds a listener to the response. In this case it will alert user if the user is added or remove correctly.
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We use try and catch to handle JSONExceptions
                try {

                    //Enters if statusCode is 200
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(GridViewSearch.this,"User added successfully",Toast.LENGTH_SHORT).show();  //Alerts user
                    }

                    //Enters when status code !=200 (=400)
                    else {
                        Toast.makeText(GridViewSearch.this,"There was an error when adding this user",Toast.LENGTH_SHORT).show(); //Alerts user
                    }
                }

                //In case of JSONException
                catch (JSONException e){
                    e.printStackTrace();

                }
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        };

        JWTManager jwtManager = new JWTManager(GridViewSearch.this);  //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.FOLLOW_USER,         //The Url (/accounts/addFollow/)
                Request.Method.POST,            //The type of method
                getFollowUserParams(username),  //Put the parameters of the request here (JSONObject format)
                listener,                       //The listener
                errorListener,                  //The errorListener
                jwtManager                      //The JWTManager
        );

        RequestQueueSingleton.getInstance(GridViewSearch.this).addToRequestQueue(mRequest);   //Sends the request
    }

/**
    THIS METHOD CREATES THE PARAMS NEEDED FOR THE MRequest getUnfollowUser
    @param username, the username to put in the HashMap
    @return the params needed for the MRequest
*/
    private JSONObject getUnfollowUserParams(String username){

        Map<String, String> params = new HashMap<>(); //Creates the HashMap
        params.put("followedUser", username);          //Puts username in key 'followedUser'
        return new JSONObject(params);                 //Returns the params
    }

/**
    THIS METHOD SENDS THE MRequest TO UNFOLLOW AN USER. TAKES IN PARAMETER THE USERNAME NEEDED TO COMPLETE THE REQUEST
    @param username, the username of the user we want to unfollow
*/
    private void getUnfollowUser(String username){

        //Adds a listener to the response. In this case it will alert user if the user is added or remove correctly.
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We use try and catch to handle JSONExceptions
                try {

                    //Enters if statusCode is 200
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(GridViewSearch.this,"User removed successfully",Toast.LENGTH_SHORT).show(); //Alerts user
                    }

                    //Enters when status code !=200 (=400)
                    else {
                        Toast.makeText(GridViewSearch.this,"There was an error when removing this user",Toast.LENGTH_SHORT).show(); //Alerts user
                    }
                }

                //In case of an exception
                catch (JSONException e){
                    e.printStackTrace();

                }
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        };

        JWTManager jwtManager = new JWTManager(GridViewSearch.this);  //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.UNFOLLOW_USER,           //The URL (/accounts/delFollow/)
                Request.Method.POST,                //Type of method
                getUnfollowUserParams(username),    //Put the parameters of the request here (JSONObject format)
                listener,                           //The listener
                errorListener,                      //The errorListener
                jwtManager                          //The JWTManager
        );

        RequestQueueSingleton.getInstance(GridViewSearch.this).addToRequestQueue(mRequest);   //Sends the request
    }

}
