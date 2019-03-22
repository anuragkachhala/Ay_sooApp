package com.ay_sooapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.ay_sooapp.Model.User;
import com.ay_sooapp.Model.UserProfileDetails;
import com.ay_sooapp.Response.LoginDataResponse;
import com.ay_sooapp.Response.LoginResponse;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Utils.AlertDialogManager;
import com.ay_sooapp.Utils.AySooApp;
import com.ay_sooapp.Utils.ConnectivityReceiver;
import com.ay_sooapp.Utils.DialogUtitlity;
import com.ay_sooapp.Utils.SessionManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginResponse>, ConnectivityReceiver.ConnectivityReceiverListener, CompoundButton.OnCheckedChangeListener {
    public static final String SHARED_PREF = "sharedPref";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    private final static String TAG = LoginActivity.class.getName();
    private final static String KEY_USER_PROFILE = "userProfile";
    private static final int RC_SIGN_IN = 1;

    @BindView(R.id.et_email_id)
    EditText editTextEmailId;

    @BindView(R.id.et_password)
    EditText editTextPassword;

    @BindView(R.id.widget_email_id)
    TextInputLayout textInputLayoutEmailId;

    @BindView(R.id.widget_password)
    TextInputLayout textInputLayoutPassword;

    @BindView(R.id.tv_forgot_password)
    TextView editTextForgotPassword;

    @BindView(R.id.tv_sign_up)
    TextView editTextSingUp;

    @BindView(R.id.btn_login)
    Button buttonLogin;

    @BindView(R.id.btn_login_facebook)
    Button buttonLoginFaceBook;

    @BindView(R.id.btn_login_google)
    Button buttonLoginWithGoogle;

    @BindView(R.id.checkbox_remember_me)
    AppCompatCheckBox checkBoxRememberMe;


    private Result resultResponse;
    private LoginDataResponse dataResponse;
    private SessionManager sessionManager;
    private CallbackManager callBackManager;
    private GoogleApiClient mGoogleApiClient;
    private String emailId;
    private String password;
    private User user;
    private UserProfileDetails userProfileDetails;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SessionManager.setContext(getApplicationContext());
        sessionManager = SessionManager.getInstance();
        setClickListener();
        setSignUpTextView();
        isRememberChecked();
        callBackManager = CallbackManager.Factory.create();

        //google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


    }

    private void isRememberChecked() {
        if (sessionManager.isRememberChecked()) {
            HashMap<String, String> userLoginDetails = new HashMap<String, String>();
            userLoginDetails = sessionManager.getUserLoginDetails();
            editTextEmailId.setText(userLoginDetails.get(KEY_USER_EMAIL));
            editTextPassword.setText(userLoginDetails.get(KEY_PASSWORD));
            checkBoxRememberMe.setChecked(true);
        } else {
            editTextEmailId.setText("");
            editTextPassword.setText("");
        }
    }

    private void setClickListener() {
        buttonLogin.setOnClickListener(this);
        buttonLoginFaceBook.setOnClickListener(this);
        buttonLoginWithGoogle.setOnClickListener(this);
        editTextForgotPassword.setOnClickListener(this);
        checkBoxRememberMe.setOnCheckedChangeListener(this);

        /*editTextEmailId.addTextChangedListener(new InputTextWatcher(editTextEmailId));
        editTextPassword.addTextChangedListener(new InputTextWatcher(editTextPassword));*/


    }

    private void setSignUpTextView() {
        String signUpText = getResources().getString(R.string.singup_text_signin);
        SpannableString spannableSignUpText = new SpannableString(signUpText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableSignUpText.setSpan(clickableSpan, 23, signUpText.indexOf("now") - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        editTextSingUp.setText(spannableSignUpText);
        editTextSingUp.setMovementMethod(LinkMovementMethod.getInstance());
        editTextSingUp.setHighlightColor(Color.TRANSPARENT);
    }

    private boolean submitLogin() {
        if (!validateEmailId()) {
            return false;
        }
        if (!validatePassWord()) {
            return false;
        }
        if (!checkNetWorKConnection()) {
            return false;
        }

        return true;


    }

    private boolean validatePassWord() {
        password = editTextPassword.getText().toString().trim();

        if (editTextPassword.getText().toString().trim().isEmpty()) {
            textInputLayoutPassword.setErrorEnabled(true);
            textInputLayoutPassword.setError(getResources().getString(R.string.err_enter_password));
            return false;
        }


        textInputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    private boolean validateEmailId() {

        emailId = editTextEmailId.getText().toString().trim();
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        if (editTextEmailId.getText().toString().trim().isEmpty()) {
            textInputLayoutEmailId.setErrorEnabled(true);
            textInputLayoutEmailId.setError(getResources().getString(R.string.err_enter_email_id));
            return false;
        } else if (!emailId.matches(emailPattern)) {
            textInputLayoutEmailId.setErrorEnabled(true);
            textInputLayoutEmailId.setError(getResources().getString(R.string.err_currect_email_id));
            return false;
        }

        textInputLayoutEmailId.setErrorEnabled(false);
        return true;
    }

    private void checkCredential() {
        checkRememberMe();
        DialogUtitlity.showLoading(LoginActivity.this, getResources().getString(R.string.message_loading_sign_in));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        user = new User();
        user.setEmail(emailId);
        user.setPassword(password);
        user.setPlatform("android");
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        user.setDeviceToken(pref.getString("regId", null));

        //user.setDeviceToken("d_DAFo1cvTw:APA91bGyZa5kyUsavEosJIz7Ltjta7wlgPXl7UhmBGn07QH8_zgOleal4XvdUjp9S2bOrpGB5jBfUr6qAHpmGHq_TPHBHiIc3q-SfpyFYGUD5eSshIwu4SlPoGQ_QNC09M_eyZUM2fma");
        final Call<LoginResponse> callClient = apiService.authenticateUser(user);
        callClient.enqueue(this);

        //Log.d(TAG, String.valueOf(isSuccess));
    }

    private void checkRememberMe() {
        if (checkBoxRememberMe.isChecked()) {
            sessionManager.addUserRemember(true, editTextEmailId.getText().toString().trim(), editTextPassword.getText().toString().trim());
        } else {
            sessionManager.addUserRemember(false, "", "");
        }
    }

    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        DialogUtitlity.hideLoading();
        int statusCode = response.code();
        if (statusCode == 200) {

            if (response.body() instanceof LoginResponse) {
                LoginResponse loginResponse = response.body();
                resultResponse = loginResponse.getResult();
                dataResponse = loginResponse.getData();
                if (!resultResponse.isSuccess()) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this, getResources().getString(R.string.alert_dialog_error_title), resultResponse.getMessage(), true);
                } else {
                    if(dataResponse.getProfile()!=null) {
                        userProfileDetails = dataResponse.getProfile().getUserData();
                        sessionManager = SessionManager.getInstance();
                        sessionManager.createLoginSession(user.getEmail(), user.getPassword(), dataResponse.getToken());
                        sessionManager.addProfileData(userProfileDetails.getId(), userProfileDetails.getEmail(), userProfileDetails.getUserName(), userProfileDetails.getUserName(), userProfileDetails.getActivation(), userProfileDetails.getActivationExp(), userProfileDetails.getStatus(), userProfileDetails.getActivation(), userProfileDetails.getActivationExp(), userProfileDetails.getDeviceToken(), userProfileDetails.getPlatform());
                        //Log.d(TAG, "Getting success response from server : " + response.body() + " " + user.getEmail() + " " + user.getPassword() + " " + dataResponse.getToken());
                        startNewActivity(HomeActivity.class);
                        finish();
                    }
                    else {
                        AlertDialogManager.showAlertDialog(LoginActivity.this, getResources().getString(R.string.alert_dialog_error_title), resultResponse.getMessage(), true);
                    }
                    //Toast.makeText(this, getResources().getString(R.string.show_success_login), Toast.LENGTH_SHORT).show();

                }

            }


        }
    }

    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {
        DialogUtitlity.hideLoading();
        AlertDialogManager.showAlertDialog(LoginActivity.this, getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.msg_login_not_success), false);


    }

    private String getDate(String time) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentTimeZone = new Date(((long) Long.parseLong(time)) * 1000);
        String date = sdf.format(currentTimeZone);
        return date;

    }

    private void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(LoginActivity.this, cls);
        startActivity(intent);
    }

    private boolean checkNetWorKConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            showSnack(isConnected);
        }
        return true;
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

    private void showSnack(boolean isConnected) {
        String message;
        int color = Color.WHITE;
        if (!isConnected) {
            message = getResources().getString(R.string.msg_check_connection);
            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_login), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                if (submitLogin()) {
                    checkCredential();

                }
                break;
            case R.id.tv_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.btn_login_facebook:

                LoginWithFaceBook();
                break;
            case R.id.btn_login_google:
                LoginWithGoogle();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callBackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                    // ...
                }

            }


        }
    }

    //login with facebook
    private void LoginWithFaceBook() {
        LoginManager.getInstance().registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                finish();
                Intent intent = new Intent(LoginActivity.this, CreateAlertActivity.class);
                intent.putExtra(KEY_USER_PROFILE, loginResult.toString());
                startActivity(intent);

                // getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));


    }

    private void getUserDetails(LoginResult loginResult) {

        GraphRequest data_request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject json_object, GraphResponse response) {
                Intent intent = new Intent(LoginActivity.this, CreateAlertActivity.class);
                intent.putExtra(KEY_USER_PROFILE, json_object.toString());
                finish();
                startActivity(intent);

            }

        });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,first_name,last_name");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    //LOGIN WITH GOOGLE
    private void LoginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    handleSignInResult(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    //  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    //updateUI(null);
                }

                // ...
            }
        });
    }


    private void handleSignInResult(FirebaseUser user) {
        Log.d(TAG, "handleSignInResult:" + user);

        // Signed in successfully, show authenticated UI.
        //GoogleSignInAccount acct = result.getSignInAccount();

        Log.e(TAG, "display name: " + user.getDisplayName());

        String personName = user.getDisplayName();
        String email = user.getEmail();

        Log.e(TAG, "Name: " + personName + ", email: " + email);

        startActivity(new Intent(LoginActivity.this, CreateAlertActivity.class));
        mGoogleApiClient.maybeSignOut();
        finish();


    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        /*if(isChecked){

            sessionManager.addUserRemember(true,editTextEmailId.getText().toString().trim(),editTextPassword.getText().toString().trim());


        }else {

            sessionManager.addUserRemember(false,"","");

        }*/

    }
}





   /* public class InputTextWatcher implements TextWatcher {

        View view;

        public InputTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_email_id:
                    validateEmailId();
                    break;
                case R.id.et_password:
                    validatePassWord();
                    break;
            }

        }
    }*/

