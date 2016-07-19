package com.example.bsaraci.blitzone.Profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.bsaraci.blitzone.Blitzone.Blitzone;
import com.example.bsaraci.blitzone.Start.LogIn;
import com.example.bsaraci.blitzone.Notifications.Notifications;
import com.example.bsaraci.blitzone.Options.Options;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.PhotoUploadRequest;
import com.example.bsaraci.blitzone.ServerComm.PhotoUploadResponse;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;
import com.example.bsaraci.blitzone.ServerComm.RequestURL;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Profile extends AppCompatActivity {
    Toolbar profileToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Topic topic;
    TextView toolbarTitle;
    ProgressDialog dialog;

    private String username;
    private int chapterClicked = 0;
    private int requestGallery = 0;
    private static final int UPLOAD_PROFILE_IMAGE_FROM_GALLERY = 2;
    private static final int UPLOAD_CHAPTER_IMAGE_FROM_GALLERY = 4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        getProfileData();
        getTopic();

        profileToolbar = (Toolbar) findViewById(R.id.toolbar_of_profile);
        toolbarTitle = (TextView) findViewById(R.id.profile_toolbar_title);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(LogIn.isNetworkStatusAvialable(getApplicationContext())){

            if (requestCode == requestGallery && resultCode == RESULT_OK && null != data && data.getData() != null) {
                Uri selectedImage = data.getData();
                startCropImageActivity(selectedImage);
            }

            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && requestGallery == UPLOAD_PROFILE_IMAGE_FROM_GALLERY ) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap photo = photoSaveFromGallery(resultUri);
                    ImageView imageView1 = (ImageView) this.findViewById(R.id.profile_picture);
                    dialog = ProgressDialog.show(Profile.this, "", "Uploading profile image ...", true);
                    uploadPicture(photo, RequestURL.AVATAR, null);
                    imageView1.setImageBitmap(photo);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }


            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && requestGallery == UPLOAD_CHAPTER_IMAGE_FROM_GALLERY) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap photo = photoSaveFromGallery(resultUri);

                    PhotoChapter photoChapter = topic.getPhotoChapterFromPosition(chapterClicked);
                    photoChapter.setPhoto(photo);
                    Integer chapterId = photoChapter.getChapterId();

                    dialog = ProgressDialog.show(Profile.this, "", "Uploading chapter image ...", true);
                    uploadPicture(photo, RequestURL.UPLOAD_USER_CHAPTER, getPhotoChapterFromChapterParams(chapterId));
                    mAdapter = new ProfileRecyclerviewAdapter(topic, this);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

            }

        }
        else{
            Toast.makeText(getApplicationContext(), "Cannot upload. No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfileData();
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setInitialCropWindowPaddingRatio(0)
                .setAllowRotation(false)
                .start(this);
    }


    private Bitmap photoSaveFromGallery(Uri selectedImage) {
        Bitmap bitmap = null;

        try {
            //Getting the Bitmap from Gallery
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//          ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//          bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void uploadPicture(Bitmap bitmap, String url, JSONObject params) {
        final String uploadUrl = url;
        PhotoUploadRequest r = new PhotoUploadRequest(
                uploadUrl,
                new JWTManager(getApplicationContext()),
                new PhotoUploadResponse() {
                    @Override
                    public void uploadFinishedCallback(JSONObject response) {
                        try
                        {
                            if ((int) response.get("responseCode") == HttpURLConnection.HTTP_ENTITY_TOO_LARGE) {
                                Toast.makeText(Profile.this, "Photo size is too big.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else if ((int)response.get("responseCode") == HttpURLConnection.HTTP_OK) {
                                if (uploadUrl == RequestURL.UPLOAD_USER_CHAPTER)
                                {
                                    String imageUrl = RequestURL.IP_ADDRESS + response.getString("imageUrl");
                                    PhotoChapter photoChapter = topic.getPhotoChapterFromPosition(chapterClicked);
                                    photoChapter.setUrl(imageUrl);
                                    notifyDatasetChanged();
                                }
                                dialog.dismiss();
                            }
                        } catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                params
        );
        r.execute(bitmap);
    }

    private void notifyDatasetChanged() {
        mAdapter = new ProfileRecyclerviewAdapter(topic, this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void chooseImageFromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, requestGallery);
    }

    public void removeProfilePicture() {
    }

    public void blitzoneFromProfileButtonCallback(View view) {
        finish();
    }

    public void notificationsFromProfileButtonCallback(View view) {
        Intent intent = new Intent(this, Notifications.class);

        startActivity(intent);
    }

    private void getProfileData() {

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateProfile(response);
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
                RequestURL.PROFILE,
                Request.Method.GET,
                null, //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        //Send the request to execute
        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);

    }

    private void updateProfile(JSONObject response) {
        try {
            String usernameFromServer = response.get("user").toString();
            Boolean isBanned = response.get("is_banned").toString().equals("true");
            Integer blitzCount = (Integer) response.get("blitzCount");
            Integer numFollowers = (Integer) response.get("followers");
            String avatar = RequestURL.IP_ADDRESS + response.get("avatar").toString();

            Integer likes = response.getInt("likes");
            Integer dislikes = response.getInt("dislikes");


            final ImageView imageView = (ImageView) this.findViewById(R.id.profile_picture);

            if (imageView!=null){
            Glide.with(this)
                    .load(avatar)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.color.white)
                    .dontAnimate()
                    .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            //never called
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            imageView.setImageResource(R.color.white);
                            //never called
                        }
                    });
            }

            TextView likesTextView = (TextView)findViewById(R.id.number_of_likes);
            TextView dislikesTextView = (TextView)findViewById(R.id.number_of_dislikes);

            likesTextView.setText(likes.toString());
            dislikesTextView.setText(dislikes.toString());

            TextView blitzCountView = (TextView) findViewById(R.id.number_of_blitz);
            blitzCountView.setText(blitzCount.toString());
            TextView followersView = (TextView) findViewById(R.id.number_of_followers);
            followersView.setText(numFollowers.toString());
            TextView usernameView = (TextView) findViewById(R.id.profile_toolbar_title);
            usernameView.setText(usernameFromServer);
            username=toolbarTitle.getText().toString();

        } catch (JSONException e) {
            e.printStackTrace();
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
        mRecyclerView = (RecyclerView) findViewById(R.id.hlvProfile);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    public void onItemClick(View view, int position) {
                        chapterClicked = position;
                        showChapterPhotoAlertDialogWithListView();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                })
        );
        mAdapter = new ProfileRecyclerviewAdapter(topic, this);
        mRecyclerView.setAdapter(mAdapter);
        getPhotoChapters();
    }

    private void getPhotoChapters() {

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updatePhotoChapters(response);
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
                RequestURL.GET_USER_CHAPTERS,
                Request.Method.POST,
                null, //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);
    }

    private void updatePhotoChapters(JSONObject response) {

        try {

            JSONArray photoChapterList = (JSONArray) response.get("userChapters");
            int photoChapterListSize = photoChapterList.length();

            for (int i = 0; i < photoChapterListSize; i++) {
                JSONObject jsonPhotoChapter = (JSONObject) photoChapterList.getJSONObject(i);
                Integer chapterId = (Integer) jsonPhotoChapter.get("chapter");
                PhotoChapter photoChapter = topic.getPhotoChapterFromChapterId(chapterId);
                photoChapter.setUrl(RequestURL.IP_ADDRESS + jsonPhotoChapter.get("image").toString());
                mAdapter = new ProfileRecyclerviewAdapter(topic, this);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private JSONObject getPhotoChapterFromChapterParams(Integer chapterId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("chapter", chapterId.toString());

        return new JSONObject(params);
    }


    public void showProfilePhotoAlertDialogWithListview() {
        List<String> uploadOptions = new ArrayList<String>();
        uploadOptions.add("Upload a photo from gallery");
        uploadOptions.add("Remove photo");


        //Create sequence of items
        final CharSequence[] Options = uploadOptions.toArray(new String[uploadOptions.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Change profile picture");
        dialogBuilder.setItems(Options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0) {
                    requestGallery = UPLOAD_PROFILE_IMAGE_FROM_GALLERY;
                    chooseImageFromGallery();
                }  else if (item == 1) {
                    removeProfilePicture();
                }
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

    public void showChapterPhotoAlertDialogWithListView() {

        List<String> uploadOptions = new ArrayList<String>();
        uploadOptions.add("Upload a photo from gallery");
        uploadOptions.add("Remove photo");

        //Create sequence of items
        final CharSequence[] Options = uploadOptions.toArray(new String[uploadOptions.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Change chapter picture");
        dialogBuilder.setItems(Options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0) {
                    requestGallery = UPLOAD_CHAPTER_IMAGE_FROM_GALLERY;
                    chooseImageFromGallery();
                } /*else if (item == 2) {
                    removeProfilePicture();
                }*/
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

    public void profilePictureCallback(View view) {
        showProfilePhotoAlertDialogWithListview();
    }

    public void optionsCallback(View view) {

        Intent intent = new Intent(this, Options.class);
        intent.putExtra("username",username);
        startActivity(intent);

    }

}

