package com.ay_sooapp.Utils;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class AySooApp extends Application {

    private static AySooApp mInstance;
    private SessionManager sessionManager;

    public static synchronized AySooApp getInstance() {
        return mInstance;
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Stetho.initializeWithDefaults(this);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
