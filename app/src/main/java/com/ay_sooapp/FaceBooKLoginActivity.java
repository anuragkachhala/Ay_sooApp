package com.ay_sooapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaceBooKLoginActivity extends AppCompatActivity {



    JSONObject response;
    TextView user_name;

    @BindView(R.id.btn_logout)
    Button buttonLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_boo_klogin);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("userProfile");
        Log.w("Jsondata", jsondata);
       user_name = (TextView) findViewById(R.id.textView1);
       buttonLogout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               LoginManager.getInstance().logOut();
               startActivity(new Intent(FaceBooKLoginActivity.this,FaceBookLogin.class));
               finish();
           }
       });

        try {
            response = new JSONObject(jsondata);
            if (response.has("email"))
            {
                user_name.setText(response.get("email").toString() + "  " + response.get("name").toString());
        }
        else
            {
                user_name.setText(response.get("name").toString() + response.get("id").toString());
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
