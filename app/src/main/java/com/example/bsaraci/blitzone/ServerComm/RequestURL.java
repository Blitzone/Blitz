package com.example.bsaraci.blitzone.ServerComm;

/**
 * Created by mikel on 4/17/16.
 */
public class RequestURL {

    public final static String IP_ADDRESS = "http://52.38.241.194";

    public static final String REGISTER                 = IP_ADDRESS + "/accounts/register/";
    public static final String VERIFY_TOKEN             = IP_ADDRESS + "/accounts/verifyToken/";
    public static final String LOGIN                    = IP_ADDRESS + "/accounts/login/";
    public static final String AVATAR                   = IP_ADDRESS + "/accounts/avatar/";
    public static final String PROFILE                  = IP_ADDRESS + "/accounts/profile/";
    public static final String UPLOAD_USER_CHAPTER      = IP_ADDRESS + "/images/uploadUserChapter/";
    public static final String GET_USER_CHAPTERS        = IP_ADDRESS + "/images/getUserChapters/";
    public static final String TOPIC                    = IP_ADDRESS + "/images/topic/";
    public static final String CHAPTERS                 = IP_ADDRESS + "/images/chapters/";
    public static final String CHANGE_USERNAME          = IP_ADDRESS + "/accounts/changeUsername/";
    public static final String CHANGE_PASSWORD          = IP_ADDRESS + "/accounts/changePassword/";
    public static final String SEARCH_USER              = IP_ADDRESS + "/accounts/searchUser/";
    public static final String GRID_SEARCH_PHOTOS       = IP_ADDRESS + "/images/searchPhotoChapters/";
    public static final String FOLLOW_USER              = IP_ADDRESS + "/accounts/addFollow/";
    public static final String UNFOLLOW_USER            = IP_ADDRESS + "/accounts/delFollow/";
    public static final String BEST_USERS               = IP_ADDRESS + "/accounts/getFollowing/";
    public static final String DAILY_USERS              = IP_ADDRESS + "/images/daily/";
    public static final String LIKE_TOPIC               = IP_ADDRESS + "/images/likeTopic/";
    public static final String UNLIKE_TOPIC             = IP_ADDRESS + "/images/unlikeTopic/";
    public static final String DISLIKE_TOPIC            = IP_ADDRESS + "/images/dislikeTopic/";
    public static final String UNDISLIKE_TOPIC          = IP_ADDRESS + "/images/undislikeTopic/";
    public static final String SEND_BLITZ               = IP_ADDRESS + "/images/sendBlitz/";

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
            case GRID_SEARCH_PHOTOS:    return true;
            case FOLLOW_USER:           return true;
            case UNFOLLOW_USER:         return true;
            case BEST_USERS:            return true;
            case DAILY_USERS:           return true;
            case LIKE_TOPIC:            return true;
            case UNLIKE_TOPIC:          return true;
            case DISLIKE_TOPIC:         return true;
            case UNDISLIKE_TOPIC:       return true;
            case SEND_BLITZ:            return true;
            default:                    return false;
        }
    }

}
