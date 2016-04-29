package com.example.bsaraci.blitzone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.bsaraci.blitzone.HLV.HLVAdapter;
import com.example.bsaraci.blitzone.HLV.HorizontalListView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Profile extends AppCompatActivity
{
    Toolbar profileToolbar ;
    private HorizontalListView hlv;
    private HLVAdapter hlvAdapter;

    ArrayList<String> chapters;
    ArrayList<Bitmap> photoChapter;
    private Integer topicId;
    TextView toolbarTitle;
    Typeface titleFont;

    private static String root = null;
    private static String imageFolderPath = null;
    private static String galleryFolderPath = null;
    private String imageName = null;
    private static Uri fileUri = null;
    private static final int CAMERA_IMAGE_REQUEST=1;
    private static final int UPLOAD_FROM_GALLERY = 2;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        getProfileData();
        getTopic();

        profileToolbar = (Toolbar) findViewById(R.id.toolbar_of_profile);
        toolbarTitle = (TextView)findViewById(R.id.profile_toolbar_title);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_IMAGE_REQUEST) {

                    Bitmap bitmap = null;
                    try {
                        GetImageThumbnail getImageThumbnail = new GetImageThumbnail();
                        bitmap = getImageThumbnail.getThumbnail(fileUri, this);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    } catch (FileNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    // Setting image image icon on the imageview
                    ImageView imageView = (ImageView) this.findViewById(R.id.profile_picture);
                    imageView.setImageBitmap(bitmap);

                    uploadPicture(bitmap);

        }

        else if(requestCode == UPLOAD_FROM_GALLERY && resultCode == RESULT_OK && null != data && data.getData() != null){
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Setting the Bitmap to ImageView
            photoChapter.remove(0);
            photoChapter.add(0, bitmap);
            ImageView imageView1 = (ImageView) this.findViewById(R.id.profile_picture);
            imageView1.setImageBitmap(bitmap);


            uploadPicture(bitmap);

        }
    }

    private void uploadPicture(Bitmap bitmap) {
        PhotoUploadRequest r = new PhotoUploadRequest(
                RequestURL.AVATAR,
                new JWTManager(getApplicationContext()),
                new PhotoUploadResponse() {
                    @Override
                    public void uploadFinishedCallback(Integer responseCode) {
                        if (responseCode == HttpURLConnection.HTTP_ENTITY_TOO_LARGE)
                        {
                            Toast.makeText(Profile.this, "Photo size is too big.", Toast.LENGTH_SHORT).show();
                        }
                        else if (responseCode == HttpURLConnection.HTTP_OK)
                        {
                            Toast.makeText(Profile.this, "Upload complete.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                null
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
        startActivityForResult(takePictureIntent, CAMERA_IMAGE_REQUEST);

    }

    public void chooseImageFromGallery(){
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, UPLOAD_FROM_GALLERY);
    }

    public void removeProfilePicture(){
        ImageView imageView = (ImageView) findViewById(R.id.profile_picture);
        imageView.setImageResource(R.mipmap.ic_profile_avatar);
    }

    public void blitzoneFromProfileButtonCallback (View view)
    {
        Intent intent = new Intent(this, Blitzone.class);

        startActivity(intent);
    }

    public void notificationsFromProfileButtonCallback (View view)
    {
        Intent intent = new Intent(this, Notifications.class);

        startActivity(intent);
    }

    public void disconnectCallback ()
    {

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
            public void onResponse(JSONObject response)
            {
                updateProfile(response);
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
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
            Integer blitzCount = (Integer)response.get("blitzCount");
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

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void getTopic(){
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                updateTopic(response);
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
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

    private void updateTopic (JSONObject response) {
        try {
            String topic = response.get("name").toString();
            topicId = (Integer) response.get("id");
            TextView topicText = (TextView)findViewById(R.id.profile_toolbar_title);
            topicText.setText(topic);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        getChapters();
    }

    private void getChapters(){

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                updateChapter(response);
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("Error", error.toString());
            }
        };

        JWTManager jwtManager = new JWTManager(getApplicationContext());
        //Put everything in the request
        MRequest mRequest = new MRequest(
                RequestURL.CHAPTERS,
                Request.Method.GET,
                getChaptersParams(), //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);
    }

    private void updateChapter(JSONObject response){

        try {
            chapters = new ArrayList<>();
            Bitmap bitmap1= BitmapFactory.decodeResource(this.getResources(),R.color.mint);
            Bitmap bitmap2= BitmapFactory.decodeResource(this.getResources(),R.color.mint);
            Bitmap bitmap3= BitmapFactory.decodeResource(this.getResources(),R.color.mint);
            photoChapter = new ArrayList<>(Arrays.asList(bitmap1, bitmap2, bitmap3));

            JSONArray chapterList = (JSONArray)response.get("chapters");
            int chapterListSize = chapterList.length();

            for(int i = 0; i<chapterListSize; i++){
                JSONObject jsonChapter = (JSONObject) chapterList.get(i);
                chapters.add(i, jsonChapter.get("name").toString());
                Log.i("chapters", jsonChapter.get("name").toString());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        hlv = (HorizontalListView) findViewById(R.id.hlvProfile);
        hlvAdapter = new HLVAdapter(Profile.this, chapters, photoChapter);
        hlv.setAdapter(hlvAdapter);

        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                captureImage();
                Toast.makeText(Profile.this, "You clicked on : " + chapters.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JSONObject getChaptersParams()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("topic",  topicId.toString());

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


    public void showAlertDialogWithListview()
    {
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

                if(item == 0){
                    chooseImageFromGallery();
                }
                else if(item == 1){
                    captureImage();
                }
                else if(item == 2){
                    removeProfilePicture();
                }
                else if(item == 3){
                    disconnectCallback();
                }
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

    public void profilePictureCallback (View view){
        showAlertDialogWithListview();
    }

    public void optionsCallback (View view){

        Intent intent = new Intent(this, Options.class);
        startActivity(intent);

    }



    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}

