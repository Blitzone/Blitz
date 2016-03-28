package com.example.bsaraci.blitzone.ServerComm;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.android.volley.Response.ErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mikel on 3/28/16.
 */
public class MRequest extends JsonObjectRequest {
    private final Map<String, String> headers;
    private final static String IP_ADDRESS = "http://10.0.2.2:8000";

    public MRequest(String url, Map<String, String> headers, JSONObject body, Listener listener, ErrorListener errorListener)
    {
        super(Request.Method.POST, IP_ADDRESS + url, body, listener, errorListener);
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        int statusCode = response.statusCode;
        JSONObject jsonObject = new JSONObject();
        try
        {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            jsonObject = new JSONObject(json);
            jsonObject.put("statusCode", statusCode);
        }

        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
        return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));

    }

}
