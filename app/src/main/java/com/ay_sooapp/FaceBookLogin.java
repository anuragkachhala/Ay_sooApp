package com.ay_sooapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaceBookLogin extends AppCompatActivity {

    private static final String TAG = FaceBookLogin.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;


    @BindView(R.id.textview)
    TextView textView;

    @BindView(R.id.login_facebook)
    Button buttonLogin;

    SignInButton signInButton;
    //LoginButton loginButton;

    CallbackManager callBackManager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        super.onCreate(savedInstanceState);
//       FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_face_book_login);
        ButterKnife.bind(this);

        // loginButton  =(LoginButton)findViewById(R.id.login_button);
        signInButton = (SignInButton) findViewById(R.id.btn_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(FaceBookLogin.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //loginButton.setText("fb");
        callBackManager = CallbackManager.Factory.create();
       /*loginButton.registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
           @Override
           public void onSuccess(LoginResult loginResult) {

               textView.setText("Login success \n " + loginResult.getAccessToken().getUserId() +"\n"
               +loginResult.getAccessToken().getToken());

               getUserDetails(loginResult);



           }



           @Override
           public void onCancel() {
                   textView.setText("login canceled");
           }

           @Override
           public void onError(FacebookException error) {

           }
       });
*/

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginManager.getInstance().registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        getUserDetails(loginResult);

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

                LoginManager.getInstance().logInWithReadPermissions(FaceBookLogin.this, Arrays.asList("email"));

            }
        });


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            textView.setText(personName);
            ;
        }
    }


    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        Intent intent = new Intent(FaceBookLogin.this, FaceBooKLoginActivity.class);
                        intent.putExtra("userProfile", json_object.toString());
                        startActivity(intent);
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,first_name,last_name");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callBackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }


}
