package com.example.bsaraci.blitzone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.example.bsaraci.blitzone.HLV.HLVAdapter;
import com.example.bsaraci.blitzone.HLV.HorizontalListView;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.PhotoUploadRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Profile extends AppCompatActivity
{
    Toolbar profileToolbar ;
    private HorizontalListView hlv;
    private HLVAdapter hlvAdapter;

    ArrayList<String> alName;
    ArrayList<Integer> alImage;
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


        String url = "/accounts/profile/";

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
                url,
                Request.Method.GET,
                null, //Put the parameters of the request here (JSONObject format)
                listener,
                errorListener,
                jwtManager
        );

        //Send the request to execute
        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);

        profileToolbar = (Toolbar) findViewById(R.id.toolbar_of_profile);
        toolbarTitle = (TextView)findViewById(R.id.profile_toolbar_title);
        titleFont= Typeface.createFromAsset(getAssets(), "fonts/AnkePrint.ttf");
        toolbarTitle.setTypeface(titleFont);

        alName = new ArrayList<>(Arrays.asList("Old travel throwback", "Travel friend", "Tickets to new adventure"));
//      Take your won images for your app and give drawable path as below to arraylist
        alImage = new ArrayList<>(Arrays.asList(R.color.lightGray, R.color.lightGray, R.color.lightGray));

        hlv = (HorizontalListView) findViewById(R.id.hlvProfile);
        hlvAdapter = new HLVAdapter(Profile.this, alName, alImage);
        hlv.setAdapter(hlvAdapter);

        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                captureImage();
                Toast.makeText(Profile.this, "You clicked on : " + alName.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateProfile(JSONObject response) {
        try {
            String username = response.get("user").toString();
            Boolean isBanned = response.get("is_banned").toString().equals("true");
            Integer blitzCount = (Integer)response.get("blitzCount");
            String avatar = MRequest.IP_ADDRESS + response.get("avatar").toString();

            final ImageView imageView = (ImageView) this.findViewById(R.id.profile_picture);
            ImageLoader imageLoader;

            imageLoader = RequestQueueSingleton.getInstance(this).getImageLoader();
            imageLoader.get(avatar, ImageLoader.getImageListener(imageView,
                    R.mipmap.ic_profile_avatar,R.mipmap.ic_profile_avatar));

            TextView blitzCountView = (TextView) findViewById(R.id.number_of_blitz);
            blitzCountView.setText(blitzCount.toString());

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CAMERA_IMAGE_REQUEST:

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

                    PhotoUploadRequest r = new PhotoUploadRequest(new JWTManager(getApplicationContext()));
                    r.execute(bitmap);

                    break;

                case UPLOAD_FROM_GALLERY :

                    Bitmap bitmap1 = null;
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    galleryFolderPath = cursor.getString(columnIndex);
                    cursor.close();

                    try {
                        bitmap1 = BitmapFactory.decodeFile(galleryFolderPath);

                  } catch (Exception e1) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                  }

                    ImageView imageView1 = (ImageView) this.findViewById(R.id.profile_picture);
                    // Set the Image in ImageView after decoding the String
                    imageView1.setImageBitmap(bitmap1);

                    PhotoUploadRequest r1 = new PhotoUploadRequest(new JWTManager(getApplicationContext()));
                    r1.execute(bitmap1);

                break;
            }

        }
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

    public static class GetImageThumbnail {

        private static int getPowerOfTwoForSampleRatio(double ratio) {
            int k = Integer.highestOneBit((int) Math.floor(ratio));
            if (k == 0)
                return 1;
            else
                return k;
        }

        public Bitmap getThumbnail(Uri uri, Context context)
                throws FileNotFoundException, IOException {
            InputStream input = context.getContentResolver().openInputStream(uri);

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;// optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            if ((onlyBoundsOptions.outWidth == -1)
                    || (onlyBoundsOptions.outHeight == -1))
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
}

