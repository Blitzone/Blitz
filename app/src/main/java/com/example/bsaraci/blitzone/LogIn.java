package com.example.bsaraci.blitzone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import android.media.session.MediaSession.Token;


public class  LogIn  extends AppCompatActivity {
    private final String loginUrl = "/accounts/login/";

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

    }

    public void loginHomeButtonCallback(View view)
    {
        if (_loginCheck())
        {
            final Intent intent = new Intent(this, Blitzone.class);

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
                    if (error.networkResponse.statusCode == HttpURLConnection.HTTP_BAD_REQUEST)
                        Toast.makeText(getApplicationContext(), "Username or password incorrect.", Toast.LENGTH_LONG).show();
                    else
                        Log.e("Error", error.toString());
                }
            };

            JWTManager jwtManager = new JWTManager(getApplicationContext());

            //Put everything in the request
            MRequest mRequest = new MRequest(
                    loginUrl,
                    Request.Method.POST,
                    getLoginParams(), //Put the parameters of the request here (JSONObject format)
                    listener,
                    errorListener,
                    jwtManager
            );

            //Send the request to execute
            RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);
        }

    }

    private JSONObject getLoginParams()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", ((EditText) findViewById(R.id.username_login)).getText().toString());
        params.put("password", ((EditText) findViewById(R.id.password_login)).getText().toString());

        return new JSONObject(params);
    }

    private boolean _loginCheck()
    {
        EditText username = (EditText)findViewById(R.id.username_login);
        EditText pass = (EditText)findViewById(R.id.password_login);

        if (username.getText().toString().trim().length()==0 | pass.getText().toString().trim().length()==0)
        {
            Toast.makeText(getApplicationContext(), "Fill in the blank spaces", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }

    public void signupButtonCallback(View view)
    {
        Intent intent = new Intent(this, SignUp.class);

        startActivity(intent);
    }


}
