package com.ay_sooapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ay_sooapp.Model.Profile;
import com.ay_sooapp.Model.UserProfileDetails;
import com.ay_sooapp.Request.UpdateProfileRequest;
import com.ay_sooapp.Response.Data;
import com.ay_sooapp.Response.ResponseBody;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.Response.UserProfileDataResponse;
import com.ay_sooapp.Response.UserProfileResponse;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Utils.AlertDialogManager;
import com.ay_sooapp.Utils.DialogUtitlity;
import com.ay_sooapp.Utils.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_FIRST_NAME = "firstName";
    public static final String KEY_USER_LAST_NAME = "lastName";
    static Result resultResponse;
    static Data dataResponse;
    static UserProfileDataResponse userProfileDataResponse;
    static UserProfileDetails userProfileDetails;
    private static SessionManager sessionManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_user_name)
    EditText editTextUserName;
    @BindView(R.id.tv_user_name)
    TextView textViewUserName;
    @BindView(R.id.tv_edit_user_name)
    TextView textViewEditUserName;
    @BindView(R.id.tv_email)
    TextView textViewEmailAddress;
    @BindView(R.id.tv_edit_email)
    TextView textViewEditEmail;
    @BindView(R.id.layout_change_email)
    LinearLayout linearLayoutChangeEmail;
    @BindView(R.id.et_new_email)
    EditText editTextNewEmail;
    @BindView(R.id.et_old_email)
    EditText editTextOldEmail;
    @BindView(R.id.tv_password)
    TextView textViewPassword;
    @BindView(R.id.tv_edit_password)
    TextView textViewEditPassword;
    @BindView(R.id.layout_change_password)
    LinearLayout linearLayoutChangePassword;
    @BindView(R.id.et_old_password)
    EditText editTextOldPassword;
    @BindView(R.id.et_new_password)
    EditText editTextNewPassword;
    @BindView(R.id.btn_apply_change)
    Button buttonApplyChange;
    UpdateProfileRequest updateProfileRequest;


    HashMap<String, String> profile = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setClickListener();


        //getUserProfile();
        getUserDetails();
        setUserData();

    }

    private void getUserDetails() {
        sessionManager = SessionManager.getInstance();
        profile = sessionManager.getProfileData();
    }

    private void setUserData() {

        textViewUserName.setText(profile.get(KEY_USER_FIRST_NAME) + " " + profile.get(KEY_USER_LAST_NAME));
        textViewEmailAddress.setText(profile.get(KEY_USER_EMAIL));


    }

    private void getUserProfile() {
        DialogUtitlity.showLoading(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<UserProfileResponse> callClient = apiService.getUserProfileDetails(ApiClient.getHeaders());
        callRetrofit(callClient, 1);
    }

    public <T> void callRetrofit(Call<T> call, final int i) {

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                DialogUtitlity.hideLoading();
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (i == 1) {
                        if (response.body() instanceof UserProfileResponse) {
                            UserProfileResponse userProfileResponse = (UserProfileResponse) response.body();
                            resultResponse = userProfileResponse.getResult();
                            userProfileDataResponse = userProfileResponse.getData();
                            Profile profile = userProfileDataResponse.getProfile();
                            UserProfileDetails userProfileDetails = profile.getUserProfileDetails();
                            sessionManager = SessionManager.getInstance();
                            sessionManager.addProfileData(userProfileDetails.getId(),
                                    userProfileDetails.getEmail(),
                                    userProfileDetails.getUserName(),
                                    userProfileDetails.getUserName(),
                                    userProfileDetails.getActivation(),
                                    userProfileDetails.getActivationExp(),
                                    userProfileDetails.getStatus(),
                                    userProfileDetails.getActivation(),
                                    userProfileDetails.getActivationExp(),
                                    userProfileDetails.getDeviceToken(),
                                    userProfileDetails.getPlatform());

                        }

                        // use the user object for the other fields
                    } else if (i == 2) {

                        if (response.body() instanceof ResponseBody) {
                            ResponseBody responseBody = (ResponseBody) response.body();
                            resultResponse = responseBody.getResult();
                            dataResponse = responseBody.getData();
                            if (!resultResponse.isSuccess()) {
                                AlertDialogManager.showAlertDialog(AccountActivity.this, getResources().getString(R.string.alert_dialog_error_title), resultResponse.getMessage(), true);
                            } else {
                                AlertDialogManager.showAlertDialog(AccountActivity.this, "", resultResponse.getMessage(), true);

                            }

                        }


                    }

                }


            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {

            }
        });

    }


    private void setClickListener() {
        textViewEditEmail.setOnClickListener(this);
        textViewEditPassword.setOnClickListener(this);
        textViewEditUserName.setOnClickListener(this);
        buttonApplyChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.tv_edit_user_name:
                editTextUserName.setVisibility(View.VISIBLE);
                editTextUserName.setText(textViewUserName.getText().toString().trim());
                textViewEditUserName.setVisibility(View.GONE);
                textViewUserName.setVisibility(View.GONE);
                break;
            case R.id.tv_edit_email:
                textViewEditEmail.setVisibility(View.GONE);
                linearLayoutChangeEmail.setVisibility(View.VISIBLE);
                editTextOldEmail.setText(textViewEmailAddress.getText().toString().trim());
                textViewEmailAddress.setVisibility(View.GONE);
                break;
            case R.id.tv_edit_password:
                textViewEditPassword.setVisibility(View.GONE);
                linearLayoutChangePassword.setVisibility(View.VISIBLE);
                textViewPassword.setVisibility(View.GONE);
                break;
            case R.id.btn_apply_change:
                UpdateUserProfile();
                break;

        }
    }

    private void UpdateUserProfile() {

        updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setFirstName("");
        updateProfileRequest.setLastName("");
        updateProfileRequest.setPassword("");
        updateProfileRequest.setStatus(1);
        DialogUtitlity.showLoading(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> callClient = apiService.updateUserProfileDetails(ApiClient.getHeaders(), updateProfileRequest);
        callRetrofit(callClient, 2);


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}