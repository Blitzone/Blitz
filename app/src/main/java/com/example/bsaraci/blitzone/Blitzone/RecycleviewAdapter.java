package com.example.bsaraci.blitzone.Blitzone;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public RecycleviewAdapter(Context context,List<ViewDataProvider> list, RecyclerView recyclerView) {
        this.context = context;
        this.list=list;

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Log.i("amga", "nagato");
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

            ViewDataProvider viewDataProvider= list.get(position);

            String profilePictureUrl = viewDataProvider.getUser().getProfilePictureUrl();

            boolean isLiked = viewDataProvider.getUser().is_liked();
            boolean isDisliked = viewDataProvider.getUser().is_disliked();

            ((DailyViewHolder) holder).mUsername.setText(viewDataProvider.getUser().getUsername());
            ((DailyViewHolder) holder).mBlitz.setImageResource(viewDataProvider.getBlitz());
            ((DailyViewHolder) holder).mBlitzClicked.setImageResource(viewDataProvider.getBlitzClicked());
            ((DailyViewHolder) holder).mLike.setImageResource(viewDataProvider.getLike());
            ((DailyViewHolder) holder).mLikeClicked.setImageResource(viewDataProvider.getLikeClicked());
            ((DailyViewHolder) holder).mDislike.setImageResource(viewDataProvider.getDislike());
            ((DailyViewHolder) holder).mDislikeClicked.setImageResource(viewDataProvider.getDislikeClicked());
            ((DailyViewHolder) holder).mPoints.setText(viewDataProvider.getUser().getBlitz().toString());

            ArrayList singleViewModels = viewDataProvider.getPhotoChapters();

            SingleViewModelAdapter singleViewModelAdapter = new SingleViewModelAdapter(context,singleViewModels);

            ((DailyViewHolder) holder).mRecyclerView.setHasFixedSize(true);
            ((DailyViewHolder) holder).mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ((DailyViewHolder) holder).mRecyclerView.setAdapter(singleViewModelAdapter);

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
                        ((DailyViewHolder) holder).mBlitzClicked.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mBlitzClicked) {
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mBlitz.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mLike) {
                        likeUserTopic(userPrimaryKey);
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mDislike.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mLikeClicked.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mLikeClicked) {
                        unlikeUserTopic(userPrimaryKey);
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mDislike.setVisibility(View.VISIBLE);
                        ((DailyViewHolder) holder).mLike.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mDislike) {
                        dislikeUserTopic(userPrimaryKey);
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mLike.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mDislikeClicked.setVisibility(View.VISIBLE);
                    } else if (v == ((DailyViewHolder) holder).mDislikeClicked) {
                        undislikeUserTopic(userPrimaryKey);
                        v.setVisibility(View.GONE);
                        ((DailyViewHolder) holder).mLike.setVisibility(View.VISIBLE);
                        ((DailyViewHolder) holder).mDislike.setVisibility(View.VISIBLE);
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

                    //Enters if statusCode is 200
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show(); //Alerts user
                    }

                    //Enters when status code !=200 (=400)
                    else {
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

                    //Enters if statusCode is 200
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(context, "Unliked", Toast.LENGTH_SHORT).show(); //Alerts user
                    }

                    //Enters when status code !=200 (=400)
                    else {
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

                    //Enters if statusCode is 200
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(context, "Disiked", Toast.LENGTH_SHORT).show(); //Alerts user
                    }

                    //Enters when status code !=200 (=400)
                    else {
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

                    //Enters if statusCode is 200
                    if((int)response.get("statusCode")==200){
                        Toast.makeText(context, "Undisiked", Toast.LENGTH_SHORT).show(); //Alerts user
                    }

                    //Enters when status code !=200 (=400)
                    else {
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

    public static class DailyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {

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
        public RecyclerView mRecyclerView;


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
            this.mRecyclerView = (RecyclerView) view.findViewById(R.id.chapterOfTheDay);

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

