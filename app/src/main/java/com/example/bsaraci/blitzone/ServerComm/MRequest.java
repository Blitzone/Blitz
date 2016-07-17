package com.example.bsaraci.blitzone.ServerComm;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response.ErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mikel on 3/28/16.
 */
public class MRequest extends JsonObjectRequest {
    private Map<String, String> headers = new HashMap<String, String>();
    private JSONObject response;
    private boolean _hasResponse = false;

    public MRequest(String url, int method, JSONObject body, Listener listener, ErrorListener errorListener, JWTManager jwtManager)
    {
        super(method, url, body, listener, errorListener);

        headers.put("Content-Type", "application/json");

        if (RequestURL._needsAuth(url) && jwtManager._hasToken())
        {
            headers.put("Authorization", "Token " + jwtManager.getToken());
        }

    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        JSONObject jsonObject = new JSONObject();
        try
        {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            jsonObject = new JSONObject(json);
        }

        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
        this.response = jsonObject;
        this._hasResponse = true;
        return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
    }

    public boolean _hasResponse()
    {
        return this._hasResponse;
    }
    public JSONObject getResponse()
    {
        return this.response;
    }

}
