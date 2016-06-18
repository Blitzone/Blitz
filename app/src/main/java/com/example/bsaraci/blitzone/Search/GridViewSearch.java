package com.example.bsaraci.blitzone.Search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
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


public class GridViewSearch extends AppCompatActivity {
    private Toolbar gridViewToolbar ;
    private TextView toolbarTitle;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Integer topicId;
    private Integer chapterId;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private TextView usernameInToolbar;
    boolean isFullsize;
    private View thumbView;
    private ImageButton gridViewFromFullsize;
    private ImageButton backFromGrid;
    private TextView points;
    private ImageView blitz;
    private Button add;
    private Button remove;
    private String uname;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view_search);
        initiateComponents();
        getGridPhotoChapters();

    }

    public void initiateComponents(){
        String title = getIntent().getExtras().getString("toolbarTitle");
        chapterId = getIntent().getExtras().getInt("chapterId");
        topicId = getIntent().getExtras().getInt("topicId");
        gridViewToolbar = (Toolbar) findViewById(R.id.toolbar_of_gridView_search);
        toolbarTitle = (TextView) findViewById(R.id.gridView_search_toolbar_title);
        toolbarTitle.setText(title);
        usernameInToolbar = (TextView) findViewById(R.id.username_search_toolbar);
        gridViewFromFullsize = (ImageButton) findViewById(R.id.gridView_from_fullsize);
        backFromGrid = (ImageButton) findViewById(R.id.search_from_gridViewSearch);
        points = (TextView) findViewById(R.id.pointsGridView);
        blitz = (ImageView) findViewById(R.id.blitzGridView);
        add = (Button) findViewById(R.id.addGridView);
        remove = (Button) findViewById(R.id.removeGridView);
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    public void initiateGrid(final ArrayList<GridItem> gridItems){
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_view_item, gridItems);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                isFullsize=true;
                thumbView=v;
                String url = gridItems.get(position).getUrl();
                User u = gridItems.get(position).getUser();
                usernameInToolbar.setText(u.getUsername());
                boolean followed = u.isFollowing();
                uname=u.getUsername();
                zoomImageFromThumb(points,blitz,add,remove,backFromGrid,gridViewFromFullsize,toolbarTitle,usernameInToolbar,v, url,followed);
            }
        });

    }

    private void zoomImageFromThumb(final View pts, final View blz, final View addUser, final View removeUser, final View backFromGrid, final View backFromFullSize, final View title, final View username,final View thumbView, String url, boolean followed) {
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
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
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

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        title.setAlpha(0f);
        backFromGrid.setAlpha(0f);
        backFromFullSize.setVisibility(View.VISIBLE);
        username.setVisibility(View.VISIBLE);
        pts.setVisibility(View.VISIBLE);
        blz.setVisibility(View.VISIBLE);
        if(followed){
            addUser.setVisibility(View.GONE);
            removeUser.setVisibility(View.VISIBLE);
        }
        else{
            addUser.setVisibility(View.VISIBLE);
            removeUser.setVisibility(View.GONE);
        }
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
                        getGridPhotoChapters();
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
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
                        getGridPhotoChapters();
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
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
            final ArrayList<GridItem> items = new ArrayList<>();
            JSONArray photoChapterGrid = (JSONArray) response.get("searchPhotoChapters");
            int photoChapterGridSize = photoChapterGrid.length();
            for (int i = 0; i < photoChapterGridSize; i++) {
                JSONObject jsonPhotoChapter = (JSONObject) photoChapterGrid.getJSONObject(i);
                JSONObject jsonUser = (JSONObject) jsonPhotoChapter.getJSONObject("user");
                String username=jsonUser.getString("user");
                Integer blitzCount = jsonUser.getInt("blitzCount");
                boolean isFollowed = jsonPhotoChapter.getBoolean("is_followed");
                GridItem gridItem = new GridItem();
                User u = new User();
                u.setUsername(username);
                u.setBlitz(blitzCount);
                String blz = blitzCount.toString();
                points.setText(blz);
                u.setFollowing(isFollowed);
                gridItem.setUrl(RequestURL.IP_ADDRESS + jsonPhotoChapter.getString("image"));
                gridItem.setUser(u);
                items.add(gridItem);
            }
            initiateGrid(items);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if(isFullsize){
            initiateComponents();
            final ImageView expandedImageView = (ImageView) findViewById(R.id.expandedImage);
            thumbView.setAlpha(1f);
            toolbarTitle.setAlpha(1f);
            backFromGrid.setAlpha(1f);
            gridViewFromFullsize.setVisibility(View.GONE);
            expandedImageView.setVisibility(View.GONE);
            points.setVisibility(View.GONE);
            blitz.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
            usernameInToolbar.setVisibility(View.GONE);
            isFullsize=false;
            getGridPhotoChapters();
        }
        else{
            finish();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private JSONObject getGridPhotoChapterParams(Integer topicId,Integer chapterId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("chapter", chapterId.toString());
        params.put("topic", topicId.toString());

        return new JSONObject(params);
    }

    public void searchFromGridViewSearch (View view){finish();}

    public void gridFromFullsize (View view){
        initiateComponents();
        final ImageView expandedImageView = (ImageView) findViewById(R.id.expandedImage);
        thumbView.setAlpha(1f);
        toolbarTitle.setAlpha(1f);
        backFromGrid.setAlpha(1f);
        gridViewFromFullsize.setVisibility(View.GONE);
        expandedImageView.setVisibility(View.GONE);
        points.setVisibility(View.GONE);
        blitz.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
        usernameInToolbar.setVisibility(View.GONE);
        isFullsize=false;
        getGridPhotoChapters();
    }

    public void addCallback(View view){
        view.setVisibility(View.GONE);
        getFollowUser(uname);
        remove.setVisibility(View.VISIBLE);
    }

    public void removeCallback(View view){
        view.setVisibility(View.GONE);
        getUnfollowUser(uname);
        add.setVisibility(View.VISIBLE);
    }

    private JSONObject getFollowUserParams(String username){
        Map<String, String> params = new HashMap<String, String>();
        params.put("followedUser", username);
        return new JSONObject(params);
    }

    private void getFollowUser(String username){
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(GridViewSearch.this,"User added successfully",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(GridViewSearch.this,"There was an error when adding this user",Toast.LENGTH_SHORT).show();
                    }
                }
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

        JWTManager jwtManager = new JWTManager(GridViewSearch.this);
        //Put everything in the request

        MRequest mRequest = new MRequest(
                RequestURL.FOLLOW_USER,
                Request.Method.POST,
                getFollowUserParams(username), //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(GridViewSearch.this).addToRequestQueue(mRequest);
    }

    private JSONObject getUnfollowUserParams(String username){
        Map<String, String> params = new HashMap<String, String>();
        params.put("followedUser", username);
        return new JSONObject(params);
    }

    private void getUnfollowUser(String username){
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(GridViewSearch.this,"User removed successfully",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(GridViewSearch.this,"There was an error when removing this user",Toast.LENGTH_SHORT).show();
                    }
                }
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

        JWTManager jwtManager = new JWTManager(GridViewSearch.this);
        //Put everything in the request

        MRequest mRequest = new MRequest(
                RequestURL.UNFOLLOW_USER,
                Request.Method.POST,
                getUnfollowUserParams(username), //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(GridViewSearch.this).addToRequestQueue(mRequest);
    }
}
