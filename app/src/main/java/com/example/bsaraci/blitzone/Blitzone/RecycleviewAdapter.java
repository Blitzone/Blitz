package com.example.bsaraci.blitzone.Blitzone;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.Profile.RoundedImageView;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.Search.ItemClickListener;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;
import com.example.bsaraci.blitzone.ServerComm.RequestURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecycleviewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ViewDataProvider> list;
    private Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();

    public RecycleviewAdapter(Context context,List<ViewDataProvider> list, RecyclerView recyclerView) {
        this.context = context;
        this.list=list;

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

    }

    public void setList(List<ViewDataProvider> list) {
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blitzone_view_content, parent, false);

            vh = new DailyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DailyViewHolder) {

            final ViewDataProvider viewDataProvider= list.get(position);

            String profilePictureUrl = viewDataProvider.getUser().getProfilePictureUrl();

            final boolean isLiked = viewDataProvider.getUser().is_liked();
            final boolean isDisliked = viewDataProvider.getUser().is_disliked();
            final boolean isBlitzed = viewDataProvider.getUser().is_blitzed();

            if(isBlitzed){
                ((DailyViewHolder) holder).mBlitz.setVisibility(View.GONE);
                ((DailyViewHolder) holder).mBlitzClicked.setVisibility(View.VISIBLE);
            }

            else if(!isBlitzed){
                ((DailyViewHolder) holder).mBlitz.setVisibility(View.VISIBLE);
                ((DailyViewHolder) holder).mBlitzClicked.setVisibility(View.GONE);
            }

            if(isLiked){
                ((DailyViewHolder) holder).mLike.setVisibility(View.GONE);
                ((DailyViewHolder) holder).mLikeClicked.setVisibility(View.VISIBLE);
                ((DailyViewHolder) holder).mDislike.setVisibility(View.GONE);
                ((DailyViewHolder) holder).mDislikeClicked.setVisibility(View.GONE);
            }

            else if(isDisliked){
                ((DailyViewHolder) holder).mLike.setVisibility(View.GONE);
                ((DailyViewHolder) holder).mLikeClicked.setVisibility(View.GONE);
                ((DailyViewHolder) holder).mDislike.setVisibility(View.GONE);
                ((DailyViewHolder) holder).mDislikeClicked.setVisibility(View.VISIBLE);
            }

            else{
                ((DailyViewHolder) holder).mLike.setVisibility(View.VISIBLE);
                ((DailyViewHolder) holder).mLikeClicked.setVisibility(View.GONE);
                ((DailyViewHolder) holder).mDislike.setVisibility(View.VISIBLE);
                ((DailyViewHolder) holder).mDislikeClicked.setVisibility(View.GONE);
            }


            ((DailyViewHolder) holder).mUsername.setText(viewDataProvider.getUser().getUsername());
            ((DailyViewHolder) holder).mPoints.setText(viewDataProvider.getUser().getBlitz().toString());
            ((DailyViewHolder) holder).mBlitzesText.setText(viewDataProvider.getBlitzesText());

            ArrayList singleViewModels = viewDataProvider.getPhotoChapters();

            SingleViewModelAdapter singleViewModelAdapter = new SingleViewModelAdapter(context,singleViewModels);

            ((DailyViewHolder) holder).mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            ((DailyViewHolder) holder).mRecyclerView.setLayoutManager(linearLayoutManager);
            ((DailyViewHolder) holder).mRecyclerView.setAdapter(singleViewModelAdapter);
            ((DailyViewHolder) holder).setPosition(position);

            if (scrollStatePositionsMap.containsKey(position)) {
                ((DailyViewHolder) holder).mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        ((DailyViewHolder) holder).mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                        ((DailyViewHolder) holder).mRecyclerView.getLayoutManager().onRestoreInstanceState(scrollStatePositionsMap.get(position));
                        return false;
                    }
                });
            }

            if(profilePictureUrl!=null){
                viewDataProvider.getUser().loadPicture(context,profilePictureUrl,((DailyViewHolder) holder).mProfile);
            }
            else{
                ((DailyViewHolder) holder).mProfile.setImageResource(R.color.boldGray);
            }

            final Integer userPrimaryKey = viewDataProvider.getUser().getPrimaryKey();

                    ((DailyViewHolder) holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    if (v == ((DailyViewHolder) holder).mBlitz) {
                        v.setVisibility(View.GONE);
                        Animation scaleAndShake = AnimationUtils.loadAnimation(context, R.anim.scale_translation_and_shake);
                        ((DailyViewHolder) holder).mBlitzClicked.bringToFront();
                        ((DailyViewHolder) holder).mBlitzClicked.startAnimation(scaleAndShake);
                        ((DailyViewHolder) holder).mBlitzClicked.setVisibility(View.VISIBLE);
                        sendBlitz(userPrimaryKey);
                        viewDataProvider.getUser().setIs_blitzed(true);
                    }
                    else if (v == ((DailyViewHolder) holder).mLike) {
                        v.setVisibility(View.GONE);
                        Animation scale = AnimationUtils.loadAnimation(context, R.anim.scale_and_translation_left_bottom);
                        ((DailyViewHolder) holder).mLikeClicked.bringToFront();
                        ((DailyViewHolder) holder).mLikeClicked.startAnimation(scale);
                        ((DailyViewHolder) holder).mDislike.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mLikeClicked.setVisibility(View.VISIBLE);
                        viewDataProvider.getUser().setIs_liked(true);
                        likeUserTopic(userPrimaryKey);
                    } else if (v == ((DailyViewHolder) holder).mLikeClicked) {
                        v.setVisibility(View.GONE);
                        Animation scale = AnimationUtils.loadAnimation(context, R.anim.scale_and_translation_left_up);
                        ((DailyViewHolder) holder).mLike.bringToFront();
                        ((DailyViewHolder) holder).mLike.startAnimation(scale);
                        ((DailyViewHolder) holder).mDislike.setVisibility(View.VISIBLE);
                        ((DailyViewHolder) holder).mLike.setVisibility(View.VISIBLE);
                        viewDataProvider.getUser().setIs_liked(false);
                        unlikeUserTopic(userPrimaryKey);
                    } else if (v == ((DailyViewHolder) holder).mDislike) {
                        v.setVisibility(View.GONE);
                        Animation scale = AnimationUtils.loadAnimation(context, R.anim.scale_and_translation_right_bottom);
                        ((DailyViewHolder) holder).mDislikeClicked.bringToFront();
                        ((DailyViewHolder) holder).mDislikeClicked.startAnimation(scale);
                        ((DailyViewHolder) holder).mLike.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mDislikeClicked.setVisibility(View.VISIBLE);
                        viewDataProvider.getUser().setIs_disliked(true);
                        dislikeUserTopic(userPrimaryKey);
                    } else if (v == ((DailyViewHolder) holder).mDislikeClicked) {
                        v.setVisibility(View.GONE);
                        Animation scale = AnimationUtils.loadAnimation(context, R.anim.scale_and_translation_right_up);
                        ((DailyViewHolder) holder).mDislike.bringToFront();
                        ((DailyViewHolder) holder).mDislike.startAnimation(scale);
                        ((DailyViewHolder) holder).mLike.setVisibility(View.VISIBLE);
                        ((DailyViewHolder) holder).mDislike.setVisibility(View.VISIBLE);
                        viewDataProvider.getUser().setIs_disliked(false);
                        undislikeUserTopic(userPrimaryKey);
                    }
                }
            });

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public JSONObject likeUserTopicParams(Integer userPrimaryKey){
        Map<String, Integer> params = new HashMap<>();        //Creates the HashMap
        params.put("user", userPrimaryKey);                   //Puts username in key 'followedUser'
        return new JSONObject(params);                        //Returns the params
    }

    public void likeUserTopic(Integer userPrimaryKey){
        //Adds a listener to the response. In this case it will alert user if the user is added or remove correctly.
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We use try and catch to handle JSONExceptions
                try {

                    //Enters if statusCode is 404
                    if((int)response.get("statusCode")==404){
                        Toast.makeText(context,"There was an error",Toast.LENGTH_SHORT).show(); //Alerts user
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

        JWTManager jwtManager = new JWTManager(context);  //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.LIKE_TOPIC,                  //The URL (/images/likeTopic/)
                Request.Method.POST,                    //Type of method
                likeUserTopicParams(userPrimaryKey),    //Put the parameters of the request here (JSONObject format)
                listener,                               //The listener
                errorListener,                          //The errorListener
                jwtManager                              //The JWTManager
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(mRequest);   //Sends the request
    }

    public JSONObject unlikeUserTopicParams(Integer userPrimaryKey){
        Map<String, Integer> params = new HashMap<>();        //Creates the HashMap
        params.put("user", userPrimaryKey);                   //Puts username in key 'followedUser'
        return new JSONObject(params);                        //Returns the params
    }

    public void unlikeUserTopic(Integer userPrimaryKey){
        //Adds a listener to the response. In this case it will alert user if the user is added or remove correctly.
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We use try and catch to handle JSONExceptions
                try {

                    //Enters if statusCode is 404
                    if((int)response.get("statusCode")==404){
                        Toast.makeText(context,"There was an error",Toast.LENGTH_SHORT).show(); //Alerts user
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

        JWTManager jwtManager = new JWTManager(context);  //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.UNLIKE_TOPIC,                //The URL (/images/unlikeTopic/)
                Request.Method.POST,                    //Type of method
                unlikeUserTopicParams(userPrimaryKey),  //Put the parameters of the request here (JSONObject format)
                listener,                               //The listener
                errorListener,                          //The errorListener
                jwtManager                              //The JWTManager
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(mRequest);   //Sends the request
    }

    public JSONObject dislikeUserTopicParams(Integer userPrimaryKey){
        Map<String, Integer> params = new HashMap<>();        //Creates the HashMap
        params.put("user", userPrimaryKey);                   //Puts username in key 'followedUser'
        return new JSONObject(params);                        //Returns the params
    }

    public void dislikeUserTopic(Integer userPrimaryKey){
        //Adds a listener to the response. In this case it will alert user if the user is added or remove correctly.
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We use try and catch to handle JSONExceptions
                try {

                    //Enters if statusCode is 404
                    if((int)response.get("statusCode")==404){
                        Toast.makeText(context,"There was an error",Toast.LENGTH_SHORT).show(); //Alerts user
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

        JWTManager jwtManager = new JWTManager(context);  //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.DISLIKE_TOPIC,                   //The URL (/images/dislikeTopic/)
                Request.Method.POST,                        //Type of method
                dislikeUserTopicParams(userPrimaryKey),     //Put the parameters of the request here (JSONObject format)
                listener,                                   //The listener
                errorListener,                              //The errorListener
                jwtManager                                  //The JWTManager
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(mRequest);   //Sends the request
    }

    public JSONObject undislikeUserTopicParams(Integer userPrimaryKey){
        Map<String, Integer> params = new HashMap<>();        //Creates the HashMap
        params.put("user", userPrimaryKey);                   //Puts username in key 'followedUser'
        return new JSONObject(params);                        //Returns the params
    }

    public void undislikeUserTopic(Integer userPrimaryKey){
        //Adds a listener to the response. In this case it will alert user if the user is added or remove correctly.
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We use try and catch to handle JSONExceptions
                try {

                    //Enters if statusCode is 404
                    if((int)response.get("statusCode")==404){
                        Toast.makeText(context,"There was an error",Toast.LENGTH_SHORT).show(); //Alerts user
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

        JWTManager jwtManager = new JWTManager(context);  //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.UNDISLIKE_TOPIC,                 //The URL (/images/undislikeTopic/)
                Request.Method.POST,                        //Type of method
                undislikeUserTopicParams(userPrimaryKey),   //Put the parameters of the request here (JSONObject format)
                listener,                                   //The listener
                errorListener,                              //The errorListener
                jwtManager                                  //The JWTManager
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(mRequest);   //Sends the request
    }

    public JSONObject sendBlitzParams(Integer userPrimaryKey){
        Map<String, Integer> params = new HashMap<>();        //Creates the HashMap
        params.put("user", userPrimaryKey);                   //Puts username in key 'followedUser'
        return new JSONObject(params);                        //Returns the params
    }

    public void sendBlitz (Integer userPrimaryKey){
        //Adds a listener to the response. In this case it will alert user if the user is added or remove correctly.
                Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We use try and catch to handle JSONExceptions
                try {

                    //Enters if statusCode is 409
                    if((int)response.get("statusCode")==409){
                        Toast.makeText(context,"There was an error",Toast.LENGTH_SHORT).show(); //Alerts user
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

        JWTManager jwtManager = new JWTManager(context);  //Creates the JWTManager

        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.SEND_BLITZ,                      //The URL (/images/undislikeTopic/)
                Request.Method.POST,                        //Type of method
                sendBlitzParams(userPrimaryKey),            //Put the parameters of the request here (JSONObject format)
                listener,                                   //The listener
                errorListener,                              //The errorListener
                jwtManager                                  //The JWTManager
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(mRequest);   //Sends the request
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public List<ViewDataProvider> getItems() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {

        ItemClickListener itemClickListener;
        public View mView;

        public RoundedImageView mProfile;
        public TextView mUsername;
        public TextView mPoints;
        public ImageView mBlitz;
        public ImageView mBlitzClicked;
        public ImageButton mLike;
        public ImageButton mLikeClicked;
        public ImageButton mDislike;
        public ImageButton mDislikeClicked;
        public CustomRecyclerView mRecyclerView;
        public TextView mBlitzesText;
        public int position;


        public void setPosition(int position) {
            this.position = position;
        }

        public DailyViewHolder(View view) {
            super(view);
            mView = view;
            this.mProfile= (RoundedImageView) view.findViewById(R.id.profile_picture);
            this.mUsername = (TextView) view.findViewById(R.id.name);
            this.mPoints= (TextView) view.findViewById(R.id.points);
            this.mBlitz = (ImageView) view.findViewById(R.id.blitzIcon);
            this.mBlitzClicked = (ImageView) view.findViewById(R.id.blitzClickedIcon);
            this.mLike = (ImageButton) view.findViewById(R.id.like);
            this.mLikeClicked = (ImageButton) view.findViewById(R.id.likeClicked);
            this.mDislike = (ImageButton) view.findViewById(R.id.dislike);
            this.mDislikeClicked = (ImageButton)view.findViewById(R.id.dislikeClicked);
            this.mRecyclerView = (CustomRecyclerView) view.findViewById(R.id.chapterOfTheDay);

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        scrollStatePositionsMap.put(position, recyclerView.getLayoutManager().onSaveInstanceState());
                    }
                }
            });

            this.mBlitzesText = (TextView)view.findViewById(R.id.blitzesTextViewDaily);

            mBlitz.setOnClickListener(DailyViewHolder.this);
            mBlitzClicked.setOnClickListener(DailyViewHolder.this);
            mLike.setOnClickListener(DailyViewHolder.this);
            mLikeClicked.setOnClickListener(DailyViewHolder.this);
            mDislike.setOnClickListener(DailyViewHolder.this);
            mDislikeClicked.setOnClickListener(DailyViewHolder.this);
        }

        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic) {this.itemClickListener=ic;}
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}

