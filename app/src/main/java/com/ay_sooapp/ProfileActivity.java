package com.ay_sooapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ay_sooapp.Interfacce.AlertDialogCallback;
import com.ay_sooapp.Request.UpdateProfileRequest;
import com.ay_sooapp.Response.Data;
import com.ay_sooapp.Response.ResponseBody;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Sql.DataBaseAdapter;
import com.ay_sooapp.Utils.AlertDialogManager;
import com.ay_sooapp.Utils.ConnectivityReceiver;
import com.ay_sooapp.Utils.DialogUtitlity;
import com.ay_sooapp.Utils.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, Callback<ResponseBody>, ConnectivityReceiver.ConnectivityReceiverListener, AlertDialogCallback {

    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_FIRST_NAME = "firstName";
    public static final String KEY_USER_LAST_NAME = "lastName";
    public static final String KEY_PASSWORD = "password";
    private static final String TAG = ProfileActivity.class.getName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_first_name)
    EditText editTextFirstName;

    @BindView(R.id.et_password)
    EditText editTextPassword;

    @BindView(R.id.et_email)
    EditText editTextEmail;

    @BindView(R.id.tv_first_name)
    TextView textViewFirstName;

    @BindView(R.id.tv_password)
    TextView textViewPassword;

    @BindView(R.id.tv_email)
    TextView textViewEmail;

    @BindView(R.id.edit_first_name)
    ImageView editFirstName;

    @BindView(R.id.edit_email)
    ImageView emailEdit;

    @BindView(R.id.edit_password)
    ImageView passwordEdit;

    @BindView(R.id.btn_save_profile)
    Button buttonSaveProfile;

    @BindView(R.id.tv_last_name)
    TextView textViewLastName;

    @BindView(R.id.edit_last_name)
    ImageView editLastName;

    @BindView(R.id.et_last_name)
    EditText editTextLastName;

    @BindView(R.id.btn_cancel_change)
    Button buttonCancelChange;

    @BindView(R.id.show_password)
    ImageView imageViewShowPassword;

    @BindView(R.id.hide_password)
    ImageView imageViewHidePassword;

    String firstName, lastName, password, email;
    AlertDialogManager alertDialogManager;

    private Result resultResponse;
    private Data dataResponse;
    private HashMap<String, String> profile = new HashMap<String, String>();
    private SessionManager sessionManager;
    private UpdateProfileRequest updateProfileRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        SessionManager.setContext(getApplicationContext());


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setClickListener();

        getUserDetails();

        setUserDetails();
    }

    private void setUserDetails() {

        textViewFirstName.setText(profile.get(KEY_USER_FIRST_NAME));
        textViewLastName.setText(profile.get(KEY_USER_LAST_NAME));
        textViewEmail.setText(profile.get(KEY_USER_EMAIL));
        textViewPassword.setText(profile.get(KEY_PASSWORD));


    }

    private void setClickListener() {
        editFirstName.setOnClickListener(this);
        //emailEdit.setOnClickListener(this);
        passwordEdit.setOnClickListener(this);
        editLastName.setOnClickListener(this);
        buttonSaveProfile.setOnClickListener(this);
        buttonCancelChange.setOnClickListener(this);
        imageViewShowPassword.setOnClickListener(this);
        imageViewHidePassword.setOnClickListener(this);


    }

    private void getUserDetails() {
        sessionManager = SessionManager.getInstance();
        profile = sessionManager.getProfileData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.edit_email:
                email = textViewEmail.getText().toString().trim();
                editTextEmail.setText(email);
                textViewEmail.setVisibility(View.GONE);
                editTextEmail.setVisibility(View.VISIBLE);
                emailEdit.setVisibility(View.GONE);
                break;
            case R.id.edit_password:
                password = textViewPassword.getText().toString().trim();
                editTextPassword.setText(password);
                textViewPassword.setVisibility(View.GONE);
                editTextPassword.setVisibility(View.VISIBLE);
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordEdit.setVisibility(View.GONE);
                imageViewHidePassword.setVisibility(View.VISIBLE);

                break;
            case R.id.edit_first_name:
                firstName = textViewFirstName.getText().toString().trim();
                editTextFirstName.setText(firstName);
                textViewFirstName.setVisibility(View.GONE);
                editTextFirstName.setVisibility(View.VISIBLE);
                editFirstName.setVisibility(View.GONE);
                break;
            case R.id.edit_last_name:
                lastName = textViewLastName.getText().toString().trim();
                editTextLastName.setText(lastName);
                textViewLastName.setVisibility(View.GONE);
                editTextLastName.setVisibility(View.VISIBLE);
                editLastName.setVisibility(View.GONE);
                break;
            case R.id.btn_cancel_change:
                cancelChange();
                break;
            case R.id.btn_save_profile:
                if (checkValidation()) {

                    UpdateUserProfile();
                }
                break;
            case R.id.show_password:

                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imageViewHidePassword.setVisibility(View.VISIBLE);
                imageViewShowPassword.setVisibility(View.GONE);
                break;
            case R.id.hide_password:
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                imageViewShowPassword.setVisibility(View.VISIBLE);
                imageViewHidePassword.setVisibility(View.GONE);
                break;


        }

    }

    private boolean checkValidation() {
        if (editTextFirstName.getText().toString().trim().isEmpty()) {
            AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.alert_dialog_error_title),
                    getResources().getString(R.string.err_enter_first_name), true);
            return false;
        } else if (editTextLastName.getText().toString().trim().isEmpty()) {
            AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.alert_dialog_error_title),
                    getResources().getString(R.string.err_enter_last_name), true);

            return false;
        } else if (editTextPassword.getText().toString().trim().isEmpty()) {
            AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.alert_dialog_error_title),
                    getResources().getString(R.string.err_enter_password), true);
            return false;

        } else if (editTextPassword.getText().toString().trim().length() < 5) {
            AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.alert_dialog_error_title)
                    , getResources().getString(R.string.err_password_length), true);
            return false;
        } else if (!checkNetWorKConnection()) {
            return false;
        } else {
            return true;
        }
    }

    private void cancelChange() {
        editTextFirstName.setVisibility(View.GONE);
        editTextLastName.setVisibility(View.GONE);
        editTextPassword.setVisibility(View.GONE);
        textViewFirstName.setText(profile.get(KEY_USER_FIRST_NAME));
        textViewLastName.setText(profile.get(KEY_USER_LAST_NAME));
        textViewEmail.setText(profile.get(KEY_USER_EMAIL));
        textViewPassword.setText("");
        textViewFirstName.setVisibility(View.VISIBLE);
        textViewLastName.setVisibility(View.VISIBLE);
        textViewPassword.setVisibility(View.VISIBLE);
        editFirstName.setVisibility(View.VISIBLE);
        editLastName.setVisibility(View.VISIBLE);
        passwordEdit.setVisibility(View.VISIBLE);
        imageViewShowPassword.setVisibility(View.GONE);
        imageViewHidePassword.setVisibility(View.GONE);
    }


    private void UpdateUserProfile() {

        updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setFirstName(editTextFirstName.getText().toString().trim());
        updateProfileRequest.setLastName(editTextLastName.getText().toString().trim());
        updateProfileRequest.setPassword(editTextPassword.getText().toString().trim());
        updateProfileRequest.setEmail(textViewEmail.getText().toString().trim());
        updateProfileRequest.setStatus((int) (long) sessionManager.getStatus());

        DialogUtitlity.showLoading(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> callClient = apiService.updateUserProfileDetails(ApiClient.getHeaders(), updateProfileRequest);
        callClient.enqueue(this);


    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        DialogUtitlity.hideLoading();
        int statusCode = response.code();
        if (statusCode == 200) {

            if (response.body() instanceof ResponseBody) {
                ResponseBody responseBody = (ResponseBody) response.body();
                resultResponse = responseBody.getResult();
                dataResponse = responseBody.getData();
                if (!resultResponse.isSuccess()) {
                    AlertDialogManager.showAlertDialog(ProfileActivity.this, getResources().getString(R.string.alert_dialog_error_title), resultResponse.getMessage(), true);
                } else {
                    AlertDialogManager.showAlertDialog1(ProfileActivity.this, "", resultResponse.getMessage(), true,this);

                }

            }
        }
    }

    private void startLoginActivity() {
        sessionManager = SessionManager.getInstance();
        sessionManager.logoutUser();
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(ProfileActivity.this);
        dataBaseAdapter.openDataBase();
        dataBaseAdapter.dropTable();
        finish();
    }


    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }


    private boolean checkNetWorKConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            showSnack(isConnected);
            return false;
        }
        return true;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showSnack(isConnected);

    }

    private void showSnack(boolean isConnected) {
        String message;
        int color = Color.WHITE;
        if (!isConnected) {
            message = getResources().getString(R.string.msg_check_connection);
            Snackbar snackbar = Snackbar.make(findViewById(R.id.profile_activity), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }

    @Override
    public void alertDialogCallbackOk() {
        startLoginActivity();
    }
}
