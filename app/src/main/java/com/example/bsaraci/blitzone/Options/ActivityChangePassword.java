package com.example.bsaraci.blitzone.Options;

import android.content.Intent;
import android.graphics.Typeface;
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
public class ActivityChangePassword extends AppCompatActivity {
    EditText oldPassword;
    EditText newPassword;
    EditText newPasswordAgain;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);

        Intent intent = getIntent();
        String toolbarTitle = intent.getExtras().getString("toolbarTitle");
        TextView toolbarTextView = (TextView)findViewById(R.id.password_change_toolbar_title);
        toolbarTextView.setText(toolbarTitle);

        oldPassword = (EditText)findViewById(R.id.oldPassword);
        oldPassword.setTypeface(Typeface.DEFAULT);
        newPassword = (EditText)findViewById(R.id.newPassword);
        newPassword.setTypeface(Typeface.DEFAULT);
        newPasswordAgain = (EditText)findViewById(R.id.newPasswordAgain);
        newPasswordAgain.setTypeface(Typeface.DEFAULT);

    }


    public void optionsFromChangePassword(View view){
        finish();
    }

    public void changePasswordButtonCallback(View view)
    {
        if(_changePasswordCheck()){

            //Build the request

            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    try {
                        if (response.get("statusCode").equals(HttpURLConnection.HTTP_OK))
                        {

                            try
                            {
                                JWTManager jwtManager = new JWTManager(getApplicationContext());
                                jwtManager.setToken(response.getString("token"));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                            Toast.makeText(ActivityChangePassword.this,"Password changed successfully",Toast.LENGTH_LONG).show();
                            finish();

                        }
                        else if (response.get("statusCode").equals(HttpURLConnection.HTTP_BAD_REQUEST))
                        {
                            Toast.makeText(getApplicationContext(), response.get("details").toString(), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    RequestURL.CHANGE_PASSWORD,
                    Request.Method.POST,
                    getChangePasswordParams(), //Put the parameters of the request here (JSONObject format)
                    listener,
                    errorListener,
                    jwtManager
            );

            //Send the request to execute
            RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);

        }
    }

    private JSONObject getChangePasswordParams()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("newPassword", ((EditText) findViewById(R.id.newPassword)).getText().toString());
        params.put("oldPassword",((EditText) findViewById(R.id.oldPassword)).getText().toString());

        return new JSONObject(params);
    }

    private boolean _changePasswordCheck()
    {
        newPassword = (EditText)findViewById(R.id.newPassword);
        newPasswordAgain = (EditText)findViewById(R.id.newPasswordAgain);

        if(!newPassword.getText().toString().equals(newPasswordAgain.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Passwords should match", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (newPassword.getText().toString().length()<=6)
        {
            Toast.makeText(getApplicationContext(),"Password is too short", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
}