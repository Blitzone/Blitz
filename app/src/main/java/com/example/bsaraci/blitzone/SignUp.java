package com.example.bsaraci.blitzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.bsaraci.blitzone.ServerComm.MRequest;
import com.example.bsaraci.blitzone.ServerComm.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

    }

    public void signupHomeButtonCallback(View view)
    {
        final Intent intent = new Intent(this, Blitzone.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //Build the request

        //Url
        String url = "/accounts/register/";

        EditText username = (EditText)findViewById(R.id.username_signup);
        EditText pass = (EditText)findViewById(R.id.password_signup);
        EditText pass1 = (EditText)findViewById(R.id.password_again_signup);
        EditText eMail = (EditText)findViewById(R.id.email);

        if(username.getText().toString().trim().length()==0 | pass.getText().toString().trim().length()==0 | pass1.getText().toString().trim().length()==0 | eMail.getText().toString().trim().length()==0 )
        {
            Toast.makeText(getApplicationContext(),"Fill in the blank spaces", Toast.LENGTH_LONG).show();
        }
        if(!pass.getText().toString().equals(pass1.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Verify if your passwords match", Toast.LENGTH_LONG).show();
        }
        else if (!isEmailValid(eMail.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Verify your email", Toast.LENGTH_LONG).show();
        }
        else if (pass.getText().toString().length()<=6)
        {
            Toast.makeText(getApplicationContext(),"Password is too short", Toast.LENGTH_LONG).show();
        }
        else{

            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    Log.i("Response", response.toString());
                    try {
                        if (response.get("statusCode").equals(HttpURLConnection.HTTP_CREATED))
                        {
                            startActivity(intent);
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
                    Log.e("Error:", "error " + error.toString());

                }
            };

            //Put everything in the request
            MRequest mRequest = new MRequest(
                    url,
                    null, //Headers of the request. Leave null for now.
                    getLoginParams(), //Put the parameters of the request here (JSONObject format)
                    listener,
                    errorListener
            );

            //Send the request to execute
            RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);

        }
    }

    private JSONObject getLoginParams()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", ((EditText) findViewById(R.id.username_signup)).getText().toString());
        params.put("password", ((EditText) findViewById(R.id.password_signup)).getText().toString());
        params.put("email", ((EditText) findViewById(R.id.email)).getText().toString());

        return new JSONObject(params);
    }

    public void loginButtonCallback(View view)
    {
        finish();
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}