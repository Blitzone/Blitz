package com.example.bsaraci.blitzone.ServerComm;

/**
 * Created by mikel on 4/17/16.
 */
public class RequestURL {

    public final static String IP_ADDRESS = "http://52.38.241.194";

    public static final String REGISTER     = IP_ADDRESS + "/accounts/register/";
    public static final String LOGIN        = IP_ADDRESS + "/accounts/login/";
    public static final String VERIFY_TOKEN = IP_ADDRESS + "/accounts/verifyToken/";
    public static final String AVATAR       = IP_ADDRESS + "/accounts/avatar/";
    public static final String PROFILE      = IP_ADDRESS + "/accounts/profile/";
    public static final String USER_CHAPTER = IP_ADDRESS + "/images/userChapter/";
    public static final String TOPIC        = IP_ADDRESS + "/images/topic/";

    public static boolean _needsAuth(String url)
    {
        switch(url) {
            case REGISTER:      return false;
            case LOGIN:         return false;
            case VERIFY_TOKEN:  return true;
            case AVATAR:        return true;
            case PROFILE:       return true;
            case USER_CHAPTER:  return true;
            case TOPIC:         return false;
            default:            return false;
        }
    }

}
