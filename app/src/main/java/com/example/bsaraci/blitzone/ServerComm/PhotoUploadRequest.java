package com.example.bsaraci.blitzone.ServerComm;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by mikel on 4/10/16.
 */
public class PhotoUploadRequest extends AsyncTask<Bitmap, Void, String> {
    private static String attachmentName = "filedata";
    private static String attachmentFileName = "image.bmp";
    private static String crlf = "\r\n";
    private static String twoHyphens = "--";
    private static String boundary = "*****";
    private JWTManager jwtManager;

    public PhotoUploadRequest(JWTManager jwtManager)
    {
        this.jwtManager = jwtManager;
    }

    protected String doInBackground(Bitmap... bitmapSet) {
        for (Bitmap bitmap : bitmapSet) {
            HttpURLConnection httpUrlConnection = null;
            URL url = null;
            try {
                url = new URL(MRequest.IP_ADDRESS + "/accounts/avatar/");

                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setUseCaches(false);
                httpUrlConnection.setDoOutput(true);

                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
                httpUrlConnection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                httpUrlConnection.setRequestProperty("Authorization", "JWT " + jwtManager.getToken());

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
                Log.i("UploadGallery", "doing it");

                request.flush();
                request.close();


                InputStream responseStream = new
                        BufferedInputStream(httpUrlConnection.getInputStream());

                BufferedReader responseStreamReader =
                        new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();

                String response = stringBuilder.toString();
                responseStream.close();
                httpUrlConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return "hey";

    }
}