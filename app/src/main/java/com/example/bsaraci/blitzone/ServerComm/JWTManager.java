package com.example.bsaraci.blitzone.ServerComm;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mikel on 3/31/16.
 */
public class JWTManager
{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public JWTManager(Context context)
    {
        this.pref = context.getSharedPreferences("Authentication", 0);
        this.editor = pref.edit();
    }

    public void setToken(String token)
    {
        editor.putString("token", token);
        editor.commit();
    }

    public String getToken()
    {
        return this.pref.getString("token", null);
    }

    public void delToken()
    {
        editor.putString("token", null);
        editor.commit();
    }

    public boolean _hasToken()
    {
        return this.pref.getString("token", null) != null;
    }
}
