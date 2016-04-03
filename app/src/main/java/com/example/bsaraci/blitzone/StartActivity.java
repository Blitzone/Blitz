package com.example.bsaraci.blitzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bsaraci on 4/3/2016.
 */
public class StartActivity extends AppCompatActivity {
    private final String verifyTokenUrl = "/accounts/verifyToken";

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Intent blitzoneIntent = new Intent(this, Blitzone.class);
        final Intent loginIntent = new Intent(this, LogIn.class);


        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                if (response.has("token"))
                {
                    try
                    {
                        JWTManager jwtManager = new JWTManager(getApplicationContext());
                        jwtManager.setToken(response.getString("token"));
                        startActivity(blitzoneIntent);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        //Function to be executed in case of an error
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                startActivity(loginIntent);
            }
        };

        JWTManager jwtManager = new JWTManager(getApplicationContext());
        String token;
        if (jwtManager._hasToken())
            token = jwtManager.getToken();
        else
            token = null;

        Log.i("Token", token);

        MRequest mRequest = new MRequest(
                verifyTokenUrl,
                Request.Method.POST,
                getVerifyTokenParams(),
                listener,
                errorListener,
                jwtManager
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);
    }

    private JSONObject getVerifyTokenParams()
    {
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
}
