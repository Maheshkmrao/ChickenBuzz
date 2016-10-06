package com.android.chickenbuzz.utils;

/**
 * Created by Mahesh on 8/26/2016.
 */

public class Constant {

    public static final String SHARED_PREFERENCE_NAME = "ChickenBuzzPreferences";

    //For crash Log using the PostMortemReportExceptionHandler
    public static final boolean POSTMARTEMREPORTLOG = true;
    public static final String PROJECT_NAME = "Chicken Buzz";
    public static final String PROJECT_TITLE = "Chicken Buzz";

    public static final String BASE_URL="http://jjracademy.com/buzz/api/v1/services/index.php";
    public static final String AUTHENTICATE_USER = BASE_URL+"?action=authenticateUser";
    public static final String REGISTER_USER = BASE_URL+"?action=registerUser";
    public static final String URL_UPDATE_ABOUTME_DETAILS = BASE_URL+"?action=aboutUpdate";
    public static final String URL_GET_ABOUTME_DETAILS = BASE_URL+"?action=aboutDetails";
    public static final String ADD_LOCATION_URL = BASE_URL+"?action=addLocation";

    //Google Maps Location URL to fetch Address from LatLang.
    public static String URL_GET_LOCATION_ADDRESS = "http://maps.googleapis.com/maps/api/geocode/json?address=";
    public static String  SENSOR_END = "&sensor=false";

    public static boolean IS_HOME_SCREEN_DISPLAYED = false;

    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_ISD_CODE = "ISDCODE";
    public static final String KEY_MOBILE_NO = "MOBILE_NO";
    public static final String KEY_EMAIL = "EMAIL_ADDRESS";
    public static final String KEY_CURRENT_LOCATION_LATITUDE = "CURRENT_LATITUDE";
    public static final String KEY_CURRENT_LOCATION_LONGITUDE = "CURRENT_LONGITUDE";

    public static final java.lang.String NORMAL_DATE_FORMAT = "yyyy-MM-dd";


}
