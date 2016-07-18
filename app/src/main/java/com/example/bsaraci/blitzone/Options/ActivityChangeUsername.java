package com.example.bsaraci.blitzone.Options;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.Profile.Profile;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;
import com.example.bsaraci.blitzone.ServerComm.RequestURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bsaraci on 5/6/2016.
 */
public class ActivityChangeUsername extends AppCompatActivity {
    EditText oldUsername;
    EditText newUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_username_activity);

        Intent intent = getIntent();
        String toolbarTitle = intent.getExtras().getString("toolbarTitle");
        TextView toolbarTextView = (TextView)findViewById(R.id.username_change_toolbar_title);
        toolbarTextView.setText(toolbarTitle);

        String username = getIntent().getExtras().getString("username");

        oldUsername = (EditText)findViewById(R.id.oldUsername);
        oldUsername.setText(username);

    }

    public void optionsFromChangeUsername(View view){
        finish();
    }

    public void changeUsernameButtonCallback(View view)
    {
        if(_changeUsernameCheck()){

            //Build the request

            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    Toast.makeText(ActivityChangeUsername.this,"Username changed successfully",Toast.LENGTH_LONG).show();
                    finish();
                }
            };

            //Function to be executed in case of an error
            Response.ErrorListener errorListener = new Response.ErrorListener()
            {

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                    //Log.e("Error", error.toString());
                }
            };

            JWTManager jwtManager = new JWTManager(getApplicationContext());
            //Put everything in the request
            MRequest mRequest = new MRequest(
                    RequestURL.CHANGE_USERNAME,
                    Request.Method.POST,
                    getChangeUsernameParams(), //Put the parameters of the request here (JSONObject format)
                    listener,
                    errorListener,
                    jwtManager
            );

            //Send the request to execute
            RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);

        }
    }

    private JSONObject getChangeUsernameParams()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("newUsername", ((EditText) findViewById(R.id.newUsername)).getText().toString());

        return new JSONObject(params);
    }

    private boolean _changeUsernameCheck()
    {
        oldUsername = (EditText)findViewById(R.id.oldUsername);
        newUsername = (EditText)findViewById(R.id.newUsername);

        if(oldUsername.getText().toString().equals(newUsername.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Please choose a new username", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
}
