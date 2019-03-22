package com.ay_sooapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ay_sooapp.CreateAlertActivity;
import com.ay_sooapp.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_FIRST_NAME = "firstName";
    public static final String KEY_USER_LAST_NAME = "lastName";
    public static final String KEY_USER_CREATED_ON = "createdOn";
    public static final String KEY_USER_UPDATED_ON = "updatedOn";
    public static final String KEY_USER_STATUS = "status";
    public static final String KEY_USER_ACTIVATION = "activation";
    public static final String KEY_USER_ACTIVATION_EXP = "activationExp";
    public static final String KEY_USER_DEVICE_TOKEN = "device_token";
    public static final String KEY_USER_PLATFORM = "platform";
    public static final String KEY_IS_REMEMBER = "is_remember";
    private static final String PREF_NAME = "SharedPref";
    private static final String PREF_REMEMBER = "sharedPrefRemember";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static SessionManager sessionManager;
    private static Context context;
    private SharedPreferences preferences;
    private SharedPreferences sharedPreferences;
    private int status;


    private SessionManager() {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(PREF_REMEMBER, Context.MODE_PRIVATE);
    }


    public static SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        SessionManager.context = context;
    }



    /* Create login session*/


    public static String getUDID() {
        String id = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return id;
    }


    public void createLoginSession(String email, String password, String token) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }


    public void addProfileData(String id, String email, String firstName, String lastName, String createdOn, String updatedOn,
                               Integer status, String activation, String activationExp, String deviceToken, String platform) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_FIRST_NAME, firstName);
        editor.putString(KEY_USER_LAST_NAME, lastName);
        editor.putString(KEY_USER_CREATED_ON, createdOn);
        editor.putString(KEY_USER_UPDATED_ON, updatedOn);

        editor.putInt(KEY_USER_STATUS, checkNull(status));
        editor.putString(KEY_USER_ACTIVATION, activation);
        editor.putString(KEY_USER_ACTIVATION_EXP, activationExp);
        editor.putString(KEY_USER_DEVICE_TOKEN, deviceToken);
        editor.putString(KEY_USER_PLATFORM, platform);
        editor.commit();

    }

    private int checkNull(Integer value) {

        return value==null ? 1 : value;
    }


    public void addUserRemember(boolean isRemember, String userEmail, String userPassword) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_REMEMBER, isRemember);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_PASSWORD, userPassword);
        editor.commit();
    }

    public HashMap<String, String> getUserLoginDetails() {
        HashMap<String, String> userLoginDetails = new HashMap<String, String>();
        userLoginDetails.put(KEY_USER_EMAIL, sharedPreferences.getString(KEY_USER_EMAIL, null));
        userLoginDetails.put(KEY_PASSWORD, sharedPreferences.getString(KEY_PASSWORD, null));
        return userLoginDetails;


    }

    public Boolean isRememberChecked() {
        return sharedPreferences.getBoolean(KEY_IS_REMEMBER, false);
    }


    public HashMap<String, String> getProfileData() {
        HashMap<String, String> profile = new HashMap<String, String>();
        profile.put(KEY_USER_EMAIL, preferences.getString(KEY_USER_EMAIL, null));
        profile.put(KEY_USER_FIRST_NAME, preferences.getString(KEY_USER_FIRST_NAME, null));
        profile.put(KEY_USER_LAST_NAME, preferences.getString(KEY_USER_LAST_NAME, null));
        profile.put(KEY_PASSWORD, preferences.getString(KEY_PASSWORD, null));

        return profile;

    }

    /* Get stored session data*/

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD, preferences.getString(KEY_EMAIL, null));
        user.put(KEY_TOKEN, preferences.getString(KEY_TOKEN, null));
        return user;
    }


    public String getToken() {
        String token = null;
        if (preferences != null) {
            token = preferences.getString(KEY_TOKEN, "");

        }
        return token;
    }


    public int getStatus() {

        int status = -1;
        if (preferences != null) {
            status = preferences.getInt(KEY_USER_STATUS, 0);

        }
        return status;
    }


    public String getEmailID() {
        String emailID = null;
        if (preferences != null) {
            emailID = preferences.getString(KEY_USER_EMAIL, null);
        }
        return emailID;
    }


     /* Check login method wil check user login status
    If false it will redirect user to login page
      Else won't do anything*/

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, CreateAlertActivity.class);
            context.startActivity(intent);
        }


    }


      /*Clear session details
      Clearing all data from Shared Preferences*/

    public void logoutUser() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /*Quick check for login
  Get Login State*/
    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }


}
