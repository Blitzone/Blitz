package com.example.bsaraci.blitzone.ServerComm;

/**
 * Created by mikel on 4/17/16.
 */
public class RequestURL {

    public final static String IP_ADDRESS = "http://52.38.241.194";

    public static final String REGISTER                 = IP_ADDRESS + "/accounts/register/";
    public static final String LOGIN                    = IP_ADDRESS + "/accounts/login/";
    public static final String VERIFY_TOKEN             = IP_ADDRESS + "/accounts/verifyToken/";
    public static final String AVATAR                   = IP_ADDRESS + "/accounts/avatar/";
    public static final String PROFILE                  = IP_ADDRESS + "/accounts/profile/";
    public static final String UPLOAD_USER_CHAPTER      = IP_ADDRESS + "/images/uploadUserChapter/";
    public static final String GET_USER_CHAPTERS        = IP_ADDRESS + "/images/getUserChapters/";
    public static final String TOPIC                    = IP_ADDRESS + "/images/topic/";
    public static final String CHAPTERS                 = IP_ADDRESS + "/images/chapters/";
    public static final String CHANGE_USERNAME          = IP_ADDRESS + "/accounts/changeUsername/";
    public static final String CHANGE_PASSWORD          = IP_ADDRESS + "/accounts/changePassword/";
    public static final String SEARCH_USER              = IP_ADDRESS + "/accounts/searchUser/";

    public static boolean _needsAuth(String url)
    {
        switch(url) {
            case REGISTER:              return false;
            case LOGIN:                 return false;
            case VERIFY_TOKEN:          return true;
            case AVATAR:                return true;
            case PROFILE:               return true;
            case UPLOAD_USER_CHAPTER:   return true;
            case GET_USER_CHAPTERS:     return true;
            case TOPIC:                 return false;
            case CHAPTERS:              return false;
            case CHANGE_USERNAME:       return true;
            case CHANGE_PASSWORD:       return true;
            case SEARCH_USER:           return true;
            default:                    return false;
        }
    }

}
