package com.example.bsaraci.blitzone.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.android.volley.toolbox.ImageLoader;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Profile extends AppCompatActivity {
    Toolbar profileToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProfilePhotosDataSet profilePhotosDataSet = new ProfilePhotosDataSet();

    private ArrayList<Chapter> chapters;
    private ArrayList<PhotoChapter> photoChapters;
    private Integer topicId;
    TextView toolbarTitle;

    private static String root = null;
    private static String imageFolderPath = null;
    private String imageName = null;
    private static Uri fileUri = null;
    private int chapterClicked = 0;
    private int requestCamera = 0;
    private int requestGallery = 0;
    private static final int CAMERA_PROFILE_IMAGE_REQUEST = 1;
    private static final int UPLOAD_PROFILE_IMAGE_FROM_GALLERY = 2;
    private static final int CAMERA_CHAPTER_IMAGE_REQUEST = 3;
    private static final int UPLOAD_CHAPTER_IMAGE_FROM_GALLERY = 4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        getProfileData();
        getTopic();

        profileToolbar = (Toolbar) findViewById(R.id.toolbar_of_profile);
        toolbarTitle = (TextView) findViewById(R.id.profile_toolbar_title);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == requestCamera) {

            PhotoChapter photoChapter = photoSaveFromCamera();

            if (requestCamera == CAMERA_PROFILE_IMAGE_REQUEST) {
                ImageView imageView = (ImageView) this.findViewById(R.id.profile_picture);
                imageView.setImageBitmap(photoChapter.getBitmap());

                uploadPicture(photoChapter.getBitmap(), RequestURL.AVATAR, null);
            } else if (requestCamera == CAMERA_CHAPTER_IMAGE_REQUEST) {
                Chapter chap = profilePhotosDataSet.getChapter(chapterClicked);
                profilePhotosDataSet.addPhotoChapter(photoChapter, chap);
                uploadPicture(photoChapter.getBitmap(), RequestURL.UPLOAD_USER_CHAPTER, getPhotoChapterParams(chap));
                mAdapter = new ProfileRecyclerviewAdapter(profilePhotosDataSet);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
            }
        } else if (requestCode == requestGallery && resultCode == RESULT_OK && null != data && data.getData() != null) {
            Uri selectedImage = data.getData();

            PhotoChapter photoChapter = photoSaveFromGallery(selectedImage);

            if (requestGallery == UPLOAD_PROFILE_IMAGE_FROM_GALLERY) {
                ImageView imageView1 = (ImageView) this.findViewById(R.id.profile_picture);
                imageView1.setImageBitmap(photoChapter.getBitmap());
                uploadPicture(photoChapter.getBitmap(), RequestURL.AVATAR, null);
            } else if (requestGallery == UPLOAD_CHAPTER_IMAGE_FROM_GALLERY) {
                Chapter chap = profilePhotosDataSet.getChapter(chapterClicked);
                profilePhotosDataSet.addPhotoChapter(photoChapter, chap);
                uploadPicture(photoChapter.getBitmap(), RequestURL.UPLOAD_USER_CHAPTER, getPhotoChapterParams(chap));
                mAdapter = new ProfileRecyclerviewAdapter(profilePhotosDataSet);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

    private PhotoChapter photoSaveFromCamera() {
        Bitmap bitmap = null;
        PhotoChapter photoChapter1 = null;

        try {
            GetImageThumbnail getImageThumbnail = new GetImageThumbnail();
            bitmap = getImageThumbnail.getThumbnail(fileUri, this);
            photoChapter1 = new PhotoChapter(bitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return photoChapter1;
    }

    private PhotoChapter photoSaveFromGallery(Uri selectedImage) {
        Bitmap bitmap = null;
        PhotoChapter photoChapter1 = null;

        try {
            //Getting the Bitmap from Gallery
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//          ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//          bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            photoChapter1 = new PhotoChapter(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return photoChapter1;
    }

    private void uploadPicture(Bitmap bitmap, String url, JSONObject params) {
        PhotoUploadRequest r = new PhotoUploadRequest(
                url,
                new JWTManager(getApplicationContext()),
                new PhotoUploadResponse() {
                    @Override
                    public void uploadFinishedCallback(Integer responseCode) {
                        if (responseCode == HttpURLConnection.HTTP_ENTITY_TOO_LARGE) {
                            Toast.makeText(Profile.this, "Photo size is too big.", Toast.LENGTH_SHORT).show();
                        } else if (responseCode == HttpURLConnection.HTTP_OK) {
                            Toast.makeText(Profile.this, "Upload complete.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                params
        );
        r.execute(bitmap);
    }

    public void captureImage() {

        ImageView imageView = (ImageView) findViewById(R.id.profile_picture);

        // fetching the root directory
        root = Environment.getExternalStorageDirectory().toString()
                + "/Your_Folder";

        // Creating folders for Image
        imageFolderPath = root + "/Blitzmade";
        File imagesFolder = new File(imageFolderPath);
        imagesFolder.mkdirs();

        // Generating file name
        imageName = "test.png";

        // Creating image here

        File image = new File(imageFolderPath, imageName);
        fileUri = Uri.fromFile(image);
        imageView.setTag(imageFolderPath + File.separator + imageName);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(takePictureIntent, requestCamera);

    }

    public void chooseImageFromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, requestGallery);
    }

    public void removeProfilePicture() {
        ImageView imageView = (ImageView) findViewById(R.id.profile_picture);
        imageView.setImageResource(R.mipmap.ic_profile_avatar);
    }

    public void blitzoneFromProfileButtonCallback(View view) {
        Intent intent = new Intent(this, Blitzone.class);

        startActivity(intent);
    }

    public void notificationsFromProfileButtonCallback(View view) {
        Intent intent = new Intent(this, Notifications.class);

        startActivity(intent);
    }

    public void disconnectCallback() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to log out ?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Profile.this, LogIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                JWTManager jwtManager = new JWTManager(getApplicationContext());
                jwtManager.delToken();

                startActivity(intent);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
            String username = response.get("user").toString();
            Boolean isBanned = response.get("is_banned").toString().equals("true");
            Integer blitzCount = (Integer) response.get("blitzCount");
            String avatar = RequestURL.IP_ADDRESS + response.get("avatar").toString();

            final ImageView imageView = (ImageView) this.findViewById(R.id.profile_picture);
            ImageLoader imageLoader;

            imageLoader = RequestQueueSingleton.getInstance(this).getImageLoader();
            imageLoader.get(avatar, ImageLoader.getImageListener(imageView,
                    R.mipmap.ic_profile_avatar, R.mipmap.ic_profile_avatar));

            TextView blitzCountView = (TextView) findViewById(R.id.number_of_blitz);
            blitzCountView.setText(blitzCount.toString());

            TextView usernameView = (TextView) findViewById(R.id.profileName);
            usernameView.setText(username);

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
            String topic = response.get("name").toString();
            topicId = (Integer) response.get("id");
            TextView topicText = (TextView) findViewById(R.id.profile_toolbar_title);
            topicText.setText(topic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getChapters();
    }

    private void getChapters() {

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateChapters(response);
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
                RequestURL.CHAPTERS,
                Request.Method.POST,
                getChaptersParams(), //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);
    }

    private void updateChapters(JSONObject response) {

        try {
            photoChapters = new ArrayList<PhotoChapter>();
            chapters = new ArrayList<Chapter>();
            JSONArray chapterList = (JSONArray) response.get("chapters");
            int chapterListSize = chapterList.length();
            Bitmap bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.color.mint);
            PhotoChapter photoChapter1 = new PhotoChapter(bitmap1);
            for (int i = 0; i < chapterListSize; i++) {
                JSONObject jsonChapter = (JSONObject) chapterList.getJSONObject(i);
                Chapter chap = new Chapter((int) jsonChapter.get("id"), jsonChapter.get("name").toString());
                chapters.add(i, chap);
                photoChapters.add(i, photoChapter1);
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
                        requestCamera = CAMERA_CHAPTER_IMAGE_REQUEST;
                        chapterClicked = position;
                        captureImage();
                        Toast.makeText(Profile.this, "You clicked on : " + chapters.get(position).getName().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        requestGallery = UPLOAD_CHAPTER_IMAGE_FROM_GALLERY;
                        chapterClicked = position;
                        chooseImageFromGallery();
                    }
                })
        );
        profilePhotosDataSet.addChapters(chapters);
        profilePhotosDataSet.initPhotoChapters(photoChapters);
        mAdapter = new ProfileRecyclerviewAdapter(profilePhotosDataSet);
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
                getPhotoChapterParams(topicId), //Put the parameters of the request here (JSONObject format)
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
                Chapter chap = profilePhotosDataSet.getChapterById((int) jsonPhotoChapter.get("chapter"));
                PhotoChapter photoChapter1 = profilePhotosDataSet.getPhotoChapter(chap);
                photoChapter1.setUrl(jsonPhotoChapter.get("image").toString());

                String imageUrl = RequestURL.IP_ADDRESS + jsonPhotoChapter.get("image").toString();
                URL url = null;
                try {
                    url = new URL(imageUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    photoChapter1.setBitmap(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new ProfileRecyclerviewAdapter(profilePhotosDataSet);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

    }


    private JSONObject getChaptersParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("topic", topicId.toString());

        return new JSONObject(params);
    }

    private JSONObject getPhotoChapterParams(Chapter chap) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("chapter", chap.getId().toString());

        return new JSONObject(params);
    }

    private JSONObject getPhotoChapterParams(Integer topicId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("topic", topicId.toString());
        return new JSONObject(params);
    }
    
    public static class GetImageThumbnail {

        private static int getPowerOfTwoForSampleRatio(double ratio) {
            int k = Integer.highestOneBit((int) Math.floor(ratio));
            if (k == 0)
                return 1;
            else
                return k;
        }

        public Bitmap getThumbnail(Uri uri, Context context) throws FileNotFoundException, IOException {
            InputStream input = context.getContentResolver().openInputStream(uri);

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;// optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
                return null;

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
                    : onlyBoundsOptions.outWidth;

            double ratio = (originalSize > 400) ? (originalSize / 350) : 1.0;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
            bitmapOptions.inDither = true;// optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
            input = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            return bitmap;
        }
    }

    public void showAlertDialogWithListview() {
        List<String> uploadOptions = new ArrayList<String>();
        uploadOptions.add("Upload a photo from gallery");
        uploadOptions.add("Take photo");
        uploadOptions.add("Remove photo");
        uploadOptions.add("Log out");


        //Create sequence of items
        final CharSequence[] Options = uploadOptions.toArray(new String[uploadOptions.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Options");
        dialogBuilder.setItems(Options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0) {
                    requestGallery = UPLOAD_PROFILE_IMAGE_FROM_GALLERY;
                    chooseImageFromGallery();
                } else if (item == 1) {
                    requestCamera = CAMERA_PROFILE_IMAGE_REQUEST;
                    captureImage();
                } else if (item == 2) {
                    removeProfilePicture();
                } else if (item == 3) {
                    disconnectCallback();
                }
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

    public void profilePictureCallback(View view) {
        showAlertDialogWithListview();
    }

    public void optionsCallback(View view) {

        Intent intent = new Intent(this, Options.class);
        startActivity(intent);

    }

}

