package com.example.bsaraci.blitzone.ServerComm;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mikel on 4/10/16.
 */
public class PhotoUploadRequest extends AsyncTask<Bitmap, Void, Integer> {
    private static String attachmentName = "filedata";
    private static String attachmentFileName = "image.png"; //TODO get the real image name here
    private static String crlf = "\r\n";
    private static String twoHyphens = "--";
    private static String boundary = "*****";

    private String urlAddress;
    private JWTManager jwtManager;

    public PhotoUploadResponse delegate = null;

    public PhotoUploadRequest(String urlAddress, JWTManager jwtManager, PhotoUploadResponse delegate)
    {
        this.urlAddress = urlAddress;
        this.jwtManager = jwtManager;
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        delegate.uploadFinishedCallback(responseCode);
    }

    protected Integer doInBackground(Bitmap... bitmapSet) {
        Integer responseCode = 0;
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
                    httpUrlConnection.setRequestProperty("Authorization", "JWT " + jwtManager.getToken());
                }

                DataOutputStream request = new DataOutputStream(
                        httpUrlConnection.getOutputStream());

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

                responseCode = httpUrlConnection.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return responseCode;

    }
}