package com.example.bsaraci.blitzone.ServerComm;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mikel on 4/10/16.
 */
public class PhotoUploadRequest extends AsyncTask<Bitmap, Void, JSONObject> {
    private static String attachmentName = "filedata";
    private static String attachmentFileName = "image.png"; //TODO get the real image name here
    private static String crlf = "\r\n";
    private static String twoHyphens = "--";
    private static String boundary = "*****";

    private String urlAddress;
    private JWTManager jwtManager;
    private JSONObject params;

    public PhotoUploadResponse delegate = null;

    public PhotoUploadRequest(String urlAddress, JWTManager jwtManager, PhotoUploadResponse delegate, JSONObject params)
    {
        //this.urlAddress = urlAddress;
        this.urlAddress = urlAddress;
        this.jwtManager = jwtManager;
        this.delegate = delegate;
        this.params = params;
    }

    @Override
    protected void onPostExecute(JSONObject responseCode) {
        delegate.uploadFinishedCallback(responseCode);
    }

    protected JSONObject doInBackground(Bitmap... bitmapSet) {
        JSONObject response = null;
        for (Bitmap bitmap : bitmapSet) {
            HttpURLConnection httpUrlConnection = null;
            URL url = null;
            try {
                url = new URL(this.urlAddress);

                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setUseCaches(false);
                httpUrlConnection.setDoOutput(true);

                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
                httpUrlConnection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                if (RequestURL._needsAuth(this.urlAddress) && this.jwtManager._hasToken())
                {
                    httpUrlConnection.setRequestProperty("Authorization", "Token " + jwtManager.getToken());
                }

                DataOutputStream request = new DataOutputStream(
                        httpUrlConnection.getOutputStream());
                if (params != null)
                {
                    //TODO implement a _needsParams?
                    request.writeBytes(twoHyphens + boundary + crlf);

                    request.writeBytes("Content-Disposition: form-data; name=\"params\"" + crlf);
                    //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    request.writeBytes(crlf);
                    request.writeBytes(params.toString()); // mobile_no is String variable
                    request.writeBytes(crlf);
                }

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        attachmentFileName + "\"" + crlf);
                request.writeBytes(crlf);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                byte[] pixels = byteArrayOutputStream.toByteArray();


                request.write(pixels);


                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary +
                        twoHyphens + crlf);
                request.flush();
                request.close();

                int responseCode = httpUrlConnection.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                httpUrlConnection.disconnect();

                response = new JSONObject(sb.toString());
                response.put("responseCode", responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return response;

    }
}