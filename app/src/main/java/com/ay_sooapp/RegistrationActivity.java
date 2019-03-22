package com.ay_sooapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ay_sooapp.Model.RegistrationData;
import com.ay_sooapp.Response.Data;
import com.ay_sooapp.Response.RegistrationResponse;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Utils.AySooApp;
import com.ay_sooapp.Utils.ConnectivityReceiver;
import com.ay_sooapp.Utils.DialogUtitlity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, Callback<RegistrationResponse>, ConnectivityReceiver.ConnectivityReceiverListener {
    @BindView(R.id.widget_email_id)
    TextInputLayout textInputLayoutEmailId;

    @BindView(R.id.widget_password)
    TextInputLayout textInputLayoutPassWord;

    @BindView(R.id.widget_first_name)
    TextInputLayout textInputLayoutFirstName;

    @BindView(R.id.widget_last_name)
    TextInputLayout textInputLayoutLastName;

    @BindView(R.id.widget_confirm_password)
    TextInputLayout textInputLayoutConfirmPassword;

    @BindView(R.id.et_email_id)
    EditText editTextEmailId;

    @BindView(R.id.et_password)
    EditText editTextPassword;

    @BindView(R.id.et_confirm_password)
    EditText editTextConfirmPassword;

    @BindView(R.id.tv_sign_up)
    TextView textViewSignIn;

    @BindView(R.id.et_first_name)
    EditText editTextFirstName;

    @BindView(R.id.et_last_name)
    EditText editTextLastName;

    @BindView(R.id.btn_sign_up)
    Button buttonSignUp;

    @BindView(R.id.btn_sign_up_google)
    Button buttonSignUpGoogle;

    @BindView(R.id.btn_sign_up_facebook)
    Button buttonSignUpFaceBook;
    boolean isSuccess;
    private String firstName;
    private String lastName;
    private String emailID;
    private String password;
    private String confirmPassword;
    private RegistrationData registrationData;
    private Result resultResponse;
    private Data dataResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);


        setSignInTextView();
        setClickListener();
    }

    private void setClickListener() {
        buttonSignUp.setOnClickListener(this);
        buttonSignUpFaceBook.setOnClickListener(this);
        buttonSignUpGoogle.setOnClickListener(this);
    }

    private void setSignInTextView() {

        String signInTextView = getResources().getString(R.string.signin_text_signup);
        SpannableString spannableStringSignIn = new SpannableString(signInTextView);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableStringSignIn.setSpan(clickableSpan, signInTextView.indexOf("Sign in"), signInTextView.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewSignIn.setText(spannableStringSignIn);
        textViewSignIn.setMovementMethod(LinkMovementMethod.getInstance());
        textViewSignIn.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_sign_up:
                if (submitSignUp()) {
                    checkRegistration();
                }
                break;
            case R.id.btn_sign_up_google:
                break;
            case R.id.btn_sign_up_facebook:
                break;
        }

    }


    public boolean submitSignUp() {
        getAllFieldData();
        if (!CheckFields()) {
            return false;
        }
        if (!validateEmailId()) {
            return false;
        }
        if (!validatePassword()) {
            return false;
        }
        if (!checkNetWorKConnection()) {
            return false;
        }


        return true;
    }

    private boolean checkNetWorKConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            showSnack(isConnected);
        }
        return true;
    }


    private void getAllFieldData() {
        firstName = editTextFirstName.getText().toString().trim();
        lastName = editTextLastName.getText().toString().trim();
        emailID = editTextEmailId.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        confirmPassword = editTextConfirmPassword.getText().toString().trim();
        textInputLayoutFirstName.setErrorEnabled(false);
        textInputLayoutLastName.setErrorEnabled(false);
        textInputLayoutEmailId.setErrorEnabled(false);
        textInputLayoutPassWord.setErrorEnabled(false);
        textInputLayoutLastName.setErrorEnabled(false);
    }


    private boolean CheckFields() {
        if (firstName.isEmpty()) {
            textInputLayoutFirstName.setErrorEnabled(true);
            textInputLayoutFirstName.setError(getResources().getString(R.string.err_enter_first_name));
            return false;
        } else if (lastName.isEmpty()) {
            textInputLayoutLastName.setErrorEnabled(true);
            textInputLayoutLastName.setError(getResources().getString(R.string.err_enter_last_name));
            return false;

        } else if (emailID.isEmpty()) {
            textInputLayoutEmailId.setErrorEnabled(true);
            textInputLayoutEmailId.setError(getResources().getString(R.string.err_enter_email_id));
            return false;
        } else if (password.isEmpty()) {
            textInputLayoutPassWord.setErrorEnabled(true);
            textInputLayoutPassWord.setError(getResources().getString(R.string.err_enter_password));
            return false;
        } else if (confirmPassword.isEmpty()) {
            textInputLayoutConfirmPassword.setErrorEnabled(true);
            textInputLayoutConfirmPassword.setError(getResources().getString(R.string.err_enter_password_again));
            return false;
        }
        return true;

    }


    private boolean validatePassword() {
        if (!password.equals(confirmPassword)) {
            textInputLayoutConfirmPassword.setErrorEnabled(true);
            textInputLayoutConfirmPassword.setError(getResources().getString(R.string.err_password_not_matched));
            return false;
        }
        return true;
    }


    private boolean validateEmailId() {

        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        if (!emailID.matches(emailPattern)) {
            textInputLayoutEmailId.setErrorEnabled(true);
            textInputLayoutEmailId.setError(getResources().getString(R.string.error_msg_invalid_email));
            return false;
        }
        return true;
    }

    private void checkRegistration() {
        DialogUtitlity.showLoading(RegistrationActivity.this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        registrationData = new RegistrationData();
        registrationData.setEmail(emailID);
        registrationData.setPassword(password);
        registrationData.setFirstName(firstName);
        registrationData.setLastName(lastName);
        final Call<RegistrationResponse> callClient = apiService.registerUser(registrationData);
        callClient.enqueue(this);
    }


    @Override
    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

        DialogUtitlity.hideLoading();
        int statusCode = response.code();
        if (statusCode == 200) {

            if (response.body() instanceof RegistrationResponse) {

                RegistrationResponse registrationResponse = response.body();
                resultResponse = registrationResponse.getResult();
                dataResponse = registrationResponse.getData();
                Toast.makeText(this, resultResponse.getMessage(), Toast.LENGTH_SHORT).show();

            }


            isSuccess = true;

        }
    }

    @Override
    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
        DialogUtitlity.hideLoading();


    }


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        AySooApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color = Color.WHITE;
        ;
        if (!isConnected) {
            message = getResources().getString(R.string.msg_check_connection);
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.activity_registration), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }


}

