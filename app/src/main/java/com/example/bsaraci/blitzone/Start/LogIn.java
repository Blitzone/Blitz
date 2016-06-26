package com.example.bsaraci.blitzone.Start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
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


public class  LogIn  extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressBar spinner;
    String username;
    Integer blitzNumber;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginEnabled();
        getProfileData();
    }

    public void loginHomeButtonCallback(View view)
    {
        spinnerTurning();

        final Intent intent = new Intent(this, Profile.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //Build the request

            //Url
            //Function onResponse is executed after the server responds to the requests.
            Listener<JSONObject> listener = new Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    if (response.has("token"))
                    {
                        try
                        {
                            JWTManager jwtManager = new JWTManager(getApplicationContext());
                            jwtManager.setToken(response.getString("token"));
                            startActivity(intent);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            };

            //Function to be executed in case of an error
            ErrorListener errorListener = new ErrorListener()
            {

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    spinnerStop();
                    if (error.networkResponse.statusCode == HttpURLConnection.HTTP_BAD_REQUEST)
                        Toast.makeText(getApplicationContext(), "Username or password incorrect.", Toast.LENGTH_LONG).show();
                    else
                    Log.e("Error", error.toString());
                }
            };

            JWTManager jwtManager = new JWTManager(getApplicationContext());

            //Put everything in the request
            MRequest mRequest = new MRequest(
                    RequestURL.LOGIN,
                    Request.Method.POST,
                    getLoginParams(), //Put the parameters of the request here (JSONObject format)
                    listener,
                    errorListener,
                    jwtManager
            );

            //Send the request to execute
            RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);
        }


    private JSONObject getLoginParams()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", ((EditText) findViewById(R.id.username_login)).getText().toString());
        params.put("password", ((EditText) findViewById(R.id.password_login)).getText().toString());

        return new JSONObject(params);
    }

    private void loginCheck()
    {
        Button b = (Button)findViewById(R.id.log_in_home_button);

        if (usernameEditText.getText().toString().trim().length()>0 && passwordEditText.getText().toString().trim().length()>0)
        {
            b.setEnabled(true);
        }
        else{
            b.setEnabled(false);
        }

    }

    private void loginEnabled(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                loginCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        usernameEditText = (EditText)findViewById(R.id.username_login);
        passwordEditText = (EditText)findViewById(R.id.password_login);

        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);


    }

    public void signupButtonCallback(View view)
    {
        Intent intent = new Intent(this, SignUp.class);

        startActivity(intent);
    }

    public void spinnerTurning (){
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
    }

    public void spinnerStop (){
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
    }

    private void updateProfile(JSONObject response) {
        try {
            String username1 = response.get("user").toString();
            //Boolean isBanned = response.get("is_banned").toString().equals("true");
            Integer blitzCount = (Integer) response.get("blitzCount");
            //String avatar = RequestURL.IP_ADDRESS + response.get("avatar").toString();
            username=username1;
            blitzNumber=blitzCount;

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

}
