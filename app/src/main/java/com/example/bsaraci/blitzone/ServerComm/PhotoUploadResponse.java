package com.example.bsaraci.blitzone.ServerComm;

import org.json.JSONObject;

/**
 * Created by mikel on 4/19/16.
 */
public interface PhotoUploadResponse {
    void uploadFinishedCallback(JSONObject responseCode);
}
