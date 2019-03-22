package com.ay_sooapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ay_sooapp.Request.ForgotPasswordRequest;
import com.ay_sooapp.Response.Data;
import com.ay_sooapp.Response.ResponseBody;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Utils.AlertDialogManager;
import com.ay_sooapp.Utils.AySooApp;
import com.ay_sooapp.Utils.ConnectivityReceiver;
import com.ay_sooapp.Utils.DialogUtitlity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, Callback<ResponseBody>, ConnectivityReceiver.ConnectivityReceiverListener {

    @BindView(R.id.btn_reset_password)
    Button buttonResetPassword;

    @BindView(R.id.tv_back)
    TextView textViewBack;

    @BindView(R.id.tv_email_id)
    TextView textViewEmailId;
    TextView mBackButton;
    private Result resultResponse;
    private Data dataResponse;
    private String emailId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pssword);

        ButterKnife.bind(this);

        setClickListener();


    }

    private void setClickListener() {
        buttonResetPassword.setOnClickListener(this);
        textViewBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_reset_password:
                if (resetPassword()) {
                    requestForResetPassword();
                }
                break;
            case R.id.tv_back:
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
                break;
        }

    }

    private boolean resetPassword() {

        if (!validateEmailID()) {

            return false;

        }if (!checkNetWorKConnection()) {
            return false;
        }
            return true;

    }

    private boolean checkNetWorKConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            showSnack(isConnected);
        }

        return isConnected;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        AySooApp.getInstance().setConnectivityListener(this);
    }


    private boolean validateEmailID() {

        emailId = textViewEmailId.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (emailId.isEmpty()) {
            AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.error_msg_email_id_empty), true);
            return false;
        } else if (!emailId.matches(emailPattern)) {

            AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.error_msg_invalid_email), true);
            return false;
        }

        return true;


    }


    private void requestForResetPassword() {
        DialogUtitlity.showLoading(ForgotPasswordActivity.this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail(emailId);
        final Call<ResponseBody> callClient = apiService.forgotPassword(forgotPasswordRequest);
        callClient.enqueue(this);

    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        DialogUtitlity.hideLoading();
        int statusCode = response.code();
        if (statusCode == 200) {

            if (response.body() instanceof ResponseBody) {

                ResponseBody responseBody = response.body();
                resultResponse = responseBody.getResult();
                dataResponse = responseBody.getData();
                if (!resultResponse.isSuccess()) {
                    AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this, getResources().getString(R.string.alert_dialog_error_title), resultResponse.getMessage(), true);
                } else {
                    AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this, "", getResources().getString(R.string.error_msg_password_sent), false);

                }

            }

        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {


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
            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_forgot_password), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
}
