package com.ay_sooapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ay_sooapp.Utils.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedBackActivity extends AppCompatActivity {
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_FIRST_NAME = "firstName";
    public static final String KEY_USER_LAST_NAME = "lastName";
    private static final String TAG = FeedBackActivity.class.getName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_user_name)
    TextInputEditText etUserName;

    @BindView(R.id.widget_user_email)
    TextInputLayout widgetUserEmail;

    @BindView(R.id.tv_user_email)
    TextInputEditText etUserEmail;

    @BindView(R.id.widget_user_feedback)
    TextInputLayout widgetUserFeedBack;

    @BindView(R.id.et_user_feedback)
    TextInputEditText etUserFeedBack;


    private HashMap<String, String> profile;


    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);

        SessionManager.setContext(getApplicationContext());
        sessionManager = SessionManager.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUserData();

    }

    private void setUserData() {
        profile = sessionManager.getProfileData();
        etUserName.setText(profile.get(KEY_USER_FIRST_NAME));
        etUserEmail.setText(profile.get(KEY_USER_EMAIL));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_send_feedback:
                getFeedbackData();
                break;
        }

        return super.onOptionsItemSelected(item);


    }

    private void getFeedbackData() {
        if (etUserFeedBack.getText().toString().trim().isEmpty()) {
            widgetUserFeedBack.setErrorEnabled(true);
            widgetUserFeedBack.setError(getResources().getString(R.string.err_please_fill_feedback));
        } else {
            widgetUserFeedBack.setErrorEnabled(false);
            openEmailIntent(etUserFeedBack.getText().toString().trim());

        }
    }

    private void openEmailIntent(String feedback) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.contact_us_mail)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        intent.putExtra(Intent.EXTRA_TEXT, feedback);

        startActivity(Intent.createChooser(intent, "Send feedback"));
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
