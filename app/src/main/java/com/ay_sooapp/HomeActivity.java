package com.ay_sooapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ay_sooapp.Fragment.AlertListFragment;
import com.ay_sooapp.Model.WebSiteDetails;
import com.ay_sooapp.Response.Data;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.Response.WebSiteDetailsResponse;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Sql.DataBaseAdapter;
import com.ay_sooapp.Utils.DialogUtitlity;
import com.ay_sooapp.Utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String TAG = HomeActivity.class.getName();
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_FIRST_NAME = "firstName";
    public static final String KEY_USER_LAST_NAME = "lastName";

    private static final int REQUEST_CODE = 1000;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.icon_twitter)
    ImageView imageViewTwitter;


    @BindView(R.id.icon_facebook)
    ImageView imageViewFacebook;

    @BindView(R.id.icon_instagram)
    ImageView imageViewInstagram;

    @BindView(R.id.icon_pinterest)
    ImageView imageViewPinterest;

    @BindView(R.id.icon_youtube)
    ImageView imageViewYoutube;


    @BindView(R.id.icon_linked_in)
    ImageView imageViewLinkedIn;


    /* @BindView(R.id.fab)
     FloatingActionButton fab;
 */
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    android.support.v4.app.Fragment fragment = null;
    private SessionManager sessionManager;
    private Result resultResponse;
    private Data dataResponse;
    private WebSiteDetailsResponse webSiteDetailsResponse;
    private List<WebSiteDetails> webSiteDetailsList = new ArrayList<WebSiteDetails>();
    private DataBaseAdapter dataBaseAdapter;

    private HashMap<String, String> userDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        SessionManager.setContext(this);
        sessionManager = SessionManager.getInstance();
        dataBaseAdapter = new DataBaseAdapter(HomeActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");


        setClickListener();


       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();*//*
                startActivity(new Intent(HomeActivity.this, CreateAlertActivity.class));


            }


        });*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeader = navigationView.getHeaderView(0);
        userDetails = sessionManager.getProfileData();
        TextView userName = (TextView) navigationHeader.findViewById(R.id.tv_user_name);
        TextView userEmail = (TextView) navigationHeader.findViewById(R.id.tv_user_email);
        userName.setText(userDetails.get(KEY_USER_FIRST_NAME));
        userEmail.setText(userDetails.get(KEY_USER_EMAIL));


        if (dataBaseAdapter.getAllWebSiteDetails().size() == 0) {
            getWebsiteDetails();
        } else {


            loadFragment(new AlertListFragment());
        }


    }

    private void setClickListener() {
        imageViewTwitter.setOnClickListener(this);
        imageViewFacebook.setOnClickListener(this);
        imageViewInstagram.setOnClickListener(this);
        imageViewLinkedIn.setOnClickListener(this);
        imageViewPinterest.setOnClickListener(this);
        imageViewYoutube.setOnClickListener(this);
    }


    private Boolean loadFragment(android.support.v4.app.Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
            return true;
        }
        return false;

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            fragment = new AlertListFragment();
            toolbar.setTitle("Home");

            loadFragment(fragment);

        } else if (id == R.id.nav_contact) {
            startNewActivity(ContactActivity.class);
        } else if (id == R.id.nav_contactUs) {
            startNewActivity(ContactUsActivity.class);
        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(this, PrivacyPolicyActivity.class);
            intent.putExtra("IS_Privacy", true);
            startActivity(intent);
            // startNewActivity(PrivacyPolicyActivity.class);
        } else if (id == R.id.nav_terms) {
            startNewActivity(PrivacyPolicyActivity.class);
        } else if (id == R.id.nav_account_setting) {
            startNewActivity(ProfileActivity.class);

        } else if (id == R.id.nav_feedback) {
            startNewActivity(FeedBackActivity.class);
        } else if (id == R.id.nav_logout) {
            logoutApplication();


        } else if (id == R.id.nav_faq) {
            startNewActivity(FAQActivity.class);


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void logoutApplication() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getResources().getString(R.string.msg_logout_application)).setCancelable(false).setPositiveButton(getResources().getString(R.string.btn_alert_dialog_positave), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sessionManager = SessionManager.getInstance();
                sessionManager.logoutUser();
                DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(HomeActivity.this);
                dataBaseAdapter.openDataBase();
                dataBaseAdapter.dropTable();
                finish();
            }
        }).setNegativeButton(getResources().getString(R.string.btn_alert_dialog_negetive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });

        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }


    //for getWebSite details
    private void getWebsiteDetails() {
        DialogUtitlity.showLoading(this, getResources().getString(R.string.msg_getting_alerts));
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        final Call<WebSiteDetailsResponse> callClient = apiServices.getWebSiteDetails(ApiClient.getHeaders());
        callClient.enqueue(new Callback<WebSiteDetailsResponse>() {
            @Override
            public void onResponse(Call<WebSiteDetailsResponse> call, Response<WebSiteDetailsResponse> response) {
                DialogUtitlity.hideLoading();
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body() instanceof WebSiteDetailsResponse) {
                        webSiteDetailsResponse = response.body();
                        resultResponse = webSiteDetailsResponse.getResult();
                        webSiteDetailsList.add(0, new WebSiteDetails(0, "", "Select Company", ""));
                        webSiteDetailsList.addAll(webSiteDetailsResponse.getData());
                        dataBaseAdapter.openDataBase();

                        dataBaseAdapter.addAllWebsiteDetails(webSiteDetailsList);
                        loadFragment(new AlertListFragment());
                    }
                    if (resultResponse.isSuccess()) {

                        Log.v(TAG, String.valueOf(webSiteDetailsList.size()));
                    }
                }

            }

            @Override
            public void onFailure(Call<WebSiteDetailsResponse> call, Throwable t) {
                DialogUtitlity.hideLoading();


            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.icon_twitter:
                startActivity(getOpenTwitterIntent(this));
                break;
            case R.id.icon_facebook:
                startActivity(getOpenFacebookIntent(this));
                break;
            case R.id.icon_instagram:
                startActivity(getOpenInstagramIntent(this));
                break;
            case R.id.icon_linked_in:
                break;
            case R.id.icon_youtube:
                break;
            case R.id.icon_pinterest:
                break;
        }
    }

    private Intent getOpenTwitterIntent(Context context) {


        try {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=aysoo58007870"));

        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/aysoo58007870"));
        }

    }

    private Intent getOpenInstagramIntent(Context context) {

        Uri uri = Uri.parse("http://www.instagram.com/p/ay_soo_this_amazing_product/");


        //likeIng.setPackage("com.instagram.android");

        try {
            context.getPackageManager().getPackageInfo("com.instagram.android", 0);
            return new Intent(Intent.ACTION_VIEW, uri);

        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/ay_soo_this_amazing_product/"));
        }
    }


    public Intent getOpenFacebookIntent(Context context) {

        try {
            int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            ;
            if (versionCode >= 3002850) { //newer versions of fb app
                return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/Ay-soo-this-amazing-product-222627175032004"));
            } else { //older versions of fb app
                return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/Ay-soo-this-amazing-product-222627175032004"));

            }

        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Ay-soo-this-amazing-product-222627175032004"));
        }
    }
}
