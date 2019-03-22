package com.ay_sooapp.FirebaseUtil;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static final String TAG = MyFirebaseInstanceIDService.class.getName();

    public static final String SHARED_PREF = "sharedPref";


    @Override
    public void onTokenRefresh() {

        //For registration of token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        // Saving reg id to shared preferences

        storeRegIdInPref(refreshedToken);


        //To displaying token on logcat
        Log.v(TAG, refreshedToken);

    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}
