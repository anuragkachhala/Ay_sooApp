package com.ay_sooapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ay_sooapp.Utils.AlertDialogManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_name)
    EditText editTextName;

    @BindView(R.id.et_email)
    EditText editTextEmail;

    @BindView(R.id.et_website)
    EditText editTextWebsite;

    @BindView(R.id.et_message)
    EditText editTextMessage;

    @BindView(R.id.btn_send)
    Button buttonSend;

    private String name, emailId, websiteName, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = R.id.btn_send;
        if (id == R.id.btn_send) {
            //startActivity(new Intent(ContactActivity.this,HomeActivity.class));
            checkFields();
        }
    }

    private void checkFields() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String websitePattern = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";
        name = editTextName.getText().toString().trim();
        emailId = editTextEmail.getText().toString().trim();
        websiteName = editTextWebsite.getText().toString().trim();
        message = editTextMessage.getText().toString().trim();
        String errorTitle = getResources().getString(R.string.alert_dialog_error_title);
        if (name.isEmpty()) {
            AlertDialogManager.showAlertDialog(this, errorTitle, getResources().getString(R.string.err_enter_name), false);

        } else if (emailId.isEmpty() || !emailId.matches(emailPattern)) {
            AlertDialogManager.showAlertDialog(this, errorTitle, getResources().getString(R.string.err_currect_email_id), false);

        } else if (websiteName.isEmpty() || !websiteName.matches(websitePattern)) {
            AlertDialogManager.showAlertDialog(this, errorTitle, getResources().getString(R.string.err_enter_website), false);
        } else if (message.isEmpty()) {
            AlertDialogManager.showAlertDialog(this, errorTitle, getResources().getString(R.string.err_enter_query), false);
        } else {
            OpenEmailIntent();
        }

    }

    private void OpenEmailIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.contact_us_mail)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us ");
        intent.putExtra(Intent.EXTRA_TEXT, "Name :- " + name + "\n" + "My Website :- " + websiteName + "\n" + "Message :- " + message + "\n" + "Email ID :- " + emailId);
        startActivity(Intent.createChooser(intent, ""));
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
