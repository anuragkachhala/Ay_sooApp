package com.ay_sooapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicyActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.webview)
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        String file = "file:///android_asset/terms.html";
        if (getIntent() != null && getIntent().getBooleanExtra("IS_Privacy", false)) {
            file = "file:///android_asset/privacy.html";
            toolbar.setTitle(getString(R.string.title_privacy_policy));
        }
        webView.loadUrl(file);

        webView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
