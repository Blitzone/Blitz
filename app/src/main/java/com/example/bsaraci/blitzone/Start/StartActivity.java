package com.example.bsaraci.blitzone.Start;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.Blitzone.Blitzone;
import com.example.bsaraci.blitzone.Profile.Profile;
import com.example.bsaraci.blitzone.Profile.Topic;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;
import com.example.bsaraci.blitzone.ServerComm.RequestURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


public class StartActivity extends Activity {
    private int durationOfAnimation =0;
    private static final int SPLASH_SCREEN_DURATION = 5000;
    private static final int FADE_CHAPTERS_INTERVALL = 500;
    private Topic topic;
    TextView topicText;
    TextView chapter1;
    TextView chapter2;
    TextView chapter3;
    TextView chapter4;
    TextView chapter5;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getTopic();

    }


    private void initiateSplashScreen(){
        try {
            topicText = (TextView) findViewById(R.id.topicStart);
            chapter1 = (TextView) findViewById(R.id.chapter1);
            chapter1.setText(topic.getPhotoChapterFromPosition(0).getChapterName());
            chapter2 = (TextView) findViewById(R.id.chapter2);
            chapter2.setText(topic.getPhotoChapterFromPosition(1).getChapterName());
            chapter3 = (TextView) findViewById(R.id.chapter3);
            chapter3.setText(topic.getPhotoChapterFromPosition(2).getChapterName());
            chapter4 = (TextView) findViewById(R.id.chapter4);
            chapter4.setText(topic.getPhotoChapterFromPosition(3).getChapterName());
            chapter5 = (TextView) findViewById(R.id.chapter5);
            chapter5.setText(topic.getPhotoChapterFromPosition(4).getChapterName());
        } catch (IndexOutOfBoundsException e)
        {

        }

        animateTextViews(topicText);
        animateTextViews(chapter1);
        animateTextViews(chapter2);
        animateTextViews(chapter3);
        animateTextViews(chapter4);
        animateTextViews(chapter5);

        splashTimeOut();
    }

    private JSONObject getVerifyTokenParams() {
        String token;

        JWTManager jwtManager = new JWTManager(getApplicationContext());
        if (jwtManager._hasToken())
            token = jwtManager.getToken();
        else
            token = null;

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);

        return new JSONObject(params);

    }

    private void getToken (){
        final Intent blitzoneIntent = new Intent(this, Blitzone.class);
        blitzoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        blitzoneIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final Intent loginIntent = new Intent(this, LogIn.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("statusCode") == HttpURLConnection.HTTP_OK) {
                            startActivity(blitzoneIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                startActivity(loginIntent);
            }
        };

        JWTManager jwtManager = new JWTManager(getApplicationContext());

        MRequest mRequest = new MRequest(
                RequestURL.VERIFY_TOKEN,
                Request.Method.POST,
                getVerifyTokenParams(),
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(mRequest);
    }

    private void animateWithFade(final TextView textView){
        textView.animate()
                .alpha(3.0f)
                .setDuration(durationOfAnimation)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        textView.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void animateTextViews (TextView textView){
        topicText = (TextView) findViewById(R.id.topicStart);
        chapter1 = (TextView) findViewById(R.id.chapter1);
        chapter2 = (TextView) findViewById(R.id.chapter2);
        chapter3 = (TextView) findViewById(R.id.chapter3);
        chapter4 = (TextView) findViewById(R.id.chapter4);
        chapter5 = (TextView) findViewById(R.id.chapter5);

        if(textView==topicText){
            durationOfAnimation=getNextAnimationTime(durationOfAnimation);
            animateWithFade(textView);
        }
        else if (textView==chapter1){
            durationOfAnimation=getNextAnimationTime(durationOfAnimation);
            animateWithFade(textView);
        }

        else if (textView==chapter2){
            durationOfAnimation=getNextAnimationTime(durationOfAnimation);
            animateWithFade(textView);
        }

        else if (textView==chapter3){
            durationOfAnimation=getNextAnimationTime(durationOfAnimation);
            animateWithFade(textView);
        }

        else if (textView==chapter4){
            durationOfAnimation=getNextAnimationTime(durationOfAnimation);
            animateWithFade(textView);
        }

        else if (textView==chapter5){
            durationOfAnimation=getNextAnimationTime(durationOfAnimation);
            animateWithFade(textView);
        }

    }

    public int getNextAnimationTime(int previousAnimationTime){
        return previousAnimationTime + FADE_CHAPTERS_INTERVALL;
    }

    private void splashTimeOut(){
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_SCREEN_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    getToken();
                }
            }
        };
        timerThread.start();
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

            TextView topicText = (TextView) findViewById(R.id.topicStart);
            topicText.setText(topic.getName());

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
        initiateSplashScreen();
    }
}
