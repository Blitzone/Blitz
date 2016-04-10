package com.example.bsaraci.blitzone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private EditText username;
    private EditText pass;
    private EditText pass1;
    private EditText eMail;
    private ProgressBar spinner;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        signUpEnabled();
    }

    public void signupHomeButtonCallback(View view)
    {
        spinnerRotating();
        if(_signUpCheck()){

        final Intent intent = new Intent(this, Blitzone.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            //Build the request

            //Url
            String url = "/accounts/register/";


            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    try {
                        if (response.get("statusCode").equals(HttpURLConnection.HTTP_CREATED))
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

                            startActivity(intent);
                        }
                        else if (response.get("statusCode").equals(HttpURLConnection.HTTP_INTERNAL_ERROR))
                        {
                            Toast.makeText(getApplicationContext(),response.get("details").toString(), Toast.LENGTH_LONG).show();
                            spinner.setVisibility(View.GONE);
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
                    spinner.setVisibility(View.GONE);
                    Log.e("Error", error.toString());
                }
            };

            JWTManager jwtManager = new JWTManager(getApplicationContext());
            //Put everything in the request
            MRequest mRequest = new MRequest(
                    url,
                    Request.Method.POST,
                    getSignUpParams(), //Put the parameters of the request here (JSONObject format)
                    listener,
                    errorListener,
                    jwtManager
            );

            //Send the request to execute
            RequestQueueSingleton.getInstance(this).addToRequestQueue(mRequest);

        }
    }

    private JSONObject getSignUpParams()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", ((EditText) findViewById(R.id.username_signup)).getText().toString());
        params.put("password", ((EditText) findViewById(R.id.password_signup)).getText().toString());
        params.put("email", ((EditText) findViewById(R.id.email)).getText().toString());

        return new JSONObject(params);
    }

    private boolean _signUpCheck()
    {
        username = (EditText)findViewById(R.id.username_signup);
        pass = (EditText)findViewById(R.id.password_signup);
        pass1 = (EditText)findViewById(R.id.password_again_signup);
        eMail = (EditText)findViewById(R.id.email);

        if(!pass.getText().toString().equals(pass1.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Verify if your passwords match", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!isEmailValid(eMail.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Verify your email", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (pass.getText().toString().length()<=6)
        {
            Toast.makeText(getApplicationContext(),"Password is too short", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

    public void blankFieldsCheck (){
        Button b = (Button)findViewById(R.id.signup_home_button);
        if(username.getText().toString().trim().length()>0 &&
                pass.getText().toString().trim().length()>0 &&
                pass1.getText().toString().trim().length()>0 &&
                eMail.getText().toString().trim().length()>0 )
        {
            b.setEnabled(true);
        }
        else{
            b.setEnabled(false);
        }
    }

    public void signUpEnabled(){

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                blankFieldsCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        username = (EditText)findViewById(R.id.username_signup);
        pass = (EditText)findViewById(R.id.password_signup);
        pass1 = (EditText)findViewById(R.id.password_again_signup);
        eMail = (EditText)findViewById(R.id.email);

        username.addTextChangedListener(textWatcher);
        pass.addTextChangedListener(textWatcher);
        pass1.addTextChangedListener(textWatcher);
        eMail.addTextChangedListener(textWatcher);
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

    public void spinnerRotating(){

        spinner=(ProgressBar)findViewById(R.id.progressBar);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                spinner.setIndeterminate(true);
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (spinner!=null) {
                    spinner.setVisibility(View.GONE);
                }
            }

        };
        task.execute((Void[])null);

    }

    public void spinnerStop(){
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

    }
}