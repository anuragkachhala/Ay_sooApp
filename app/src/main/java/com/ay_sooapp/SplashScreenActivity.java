package com.ay_sooapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.ay_sooapp.Utils.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String KEY_FIRST_RUN = "firstRun";
    private static final String PREF_NAME = "SharedPrefS";
    private int SLEEP_TIMER = 3;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager.setContext(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        android.content.SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        new LogoLuncher().start();

    }

    public void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

    private class LogoLuncher extends Thread {

        public void run() {

            try {

                sleep(1000 * SLEEP_TIMER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (SessionManager.getInstance().isLoggedIn()) {
                startNewActivity(HomeActivity.class);
            } else {
                startNewActivity(LoginActivity.class);

            }
        }
    }
}
