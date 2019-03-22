package com.ay_sooapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ay_sooapp.Interfacce.AlertDialogCallback;
import com.ay_sooapp.Model.WebSiteDetails;
import com.ay_sooapp.Request.CreateAlertRequest;
import com.ay_sooapp.Response.Data;
import com.ay_sooapp.Response.ResponseBody;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.Response.WebSiteDetailsResponse;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Sql.DataBaseAdapter;
import com.ay_sooapp.Utils.AlertDialogManager;
import com.ay_sooapp.Utils.ConnectivityReceiver;
import com.ay_sooapp.Utils.DialogUtitlity;
import com.ay_sooapp.Utils.SessionManager;
import com.ay_sooapp.Utils.SpinnerManager;
import com.google.android.gms.samples.vision.ocrreader.OcrCaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAlertActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, Callback<ResponseBody>, ConnectivityReceiver.ConnectivityReceiverListener, AlertDialogCallback {


    public static final String TAG = CreateAlertActivity.class.getName();

    private static final int OCR_REQUEST = 1001;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_create_alert)
    Button buttonCreateAlert;

    @BindView(R.id.descriptionTextView)
    EditText descriptionTextView;


    @BindView(R.id.spinner_company)
    Spinner spinnerCompanyList;

    @BindView(R.id.et_url)
    EditText editTextUrl;

    @BindView(R.id.et_article_code)
    EditText editTextArticleCode;


    @BindView(R.id.ocrRequestImageView)
    ImageView ocrRequestImageView;


    CreateAlertRequest createAlertRequest;

    String companyList[];

    String companyUrl;
    String articleCode;

    String companyName;
    long webSiteID;

    private Result resultResponse;
    private Data dataResponse;

    private HashMap<String, String> hashMap;
    private SessionManager sessionManager;

    private WebSiteDetailsResponse webSiteDetailsResponse;
    private List<WebSiteDetails> webSiteDetailsList = new ArrayList<>();
    private DataBaseAdapter dataBaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alert);
        ButterKnife.bind(this);

        SessionManager.setContext(getApplicationContext());

        dataBaseAdapter = new DataBaseAdapter(CreateAlertActivity.this);


        sessionManager = SessionManager.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getResources().getString(R.string.title_alert));
        webSiteDetailsList.clear();
        webSiteDetailsList.addAll(dataBaseAdapter.getAllWebSiteDetails());
        if (webSiteDetailsList.size() != 0) {
            /*webSiteDetailsList.set(0,new WebSiteDetails(0,"","Select Company"));*/
            companyList = new String[webSiteDetailsList.size()];
        } else {
            //
        }


        int i = 0;
        for (WebSiteDetails webSiteDetails : webSiteDetailsList) {
            companyList[i] = webSiteDetails.getName();
            i++;
        }

        setListener();

        /*companyList = getResources().getStringArray(R.array.company_list);*/


        spinnerCompanyList.setAdapter(SpinnerManager.setSpinner(this, companyList));
    }


    private void setListener() {
        buttonCreateAlert.setOnClickListener(this);
        ocrRequestImageView.setOnClickListener(this);
        spinnerCompanyList.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ocrRequestImageView:
                startActivityForResult(new Intent(CreateAlertActivity.this, OcrCaptureActivity.class), OCR_REQUEST);
                break;
            case R.id.btn_create_alert:

                if (checkValidEntry()) {

                    if (checkNetWorKConnection()) {

                        createAlert();
                    }

                }

                break;
        }

        //logOut();
    }

    private boolean checkValidEntry() {

        if (!editTextUrl.getText().toString().trim().isEmpty()) {

            companyUrl = editTextUrl.getText().toString().trim();

            companyName = getCompanyName(companyUrl);
            articleCode = null;
            return true;

        } else if (!editTextArticleCode.getText().toString().isEmpty()) {

            articleCode = editTextArticleCode.getText().toString().trim();
            companyName = spinnerCompanyList.getSelectedItem().toString().trim();
            if (!companyName.equals("Select Company")) {
                for (WebSiteDetails webSiteDetails : webSiteDetailsList) {
                    if (companyName.equals(webSiteDetails.getName())) {
                        companyUrl = webSiteDetails.getSearchURL().replace("articleNumber", "") + articleCode;
                        webSiteID = webSiteDetails.getId();
                        return true;

                    }

                }
            } else {
                AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.title_alert), getResources().getString(R.string.msg_select_company), true);

                return false;

            }

        } else if (!descriptionTextView.getText().toString().trim().isEmpty()) {
            articleCode = descriptionTextView.getText().toString().trim();
            companyName = spinnerCompanyList.getSelectedItem().toString().trim();
            if (!companyName.equals("Select Company")) {
                for (WebSiteDetails webSiteDetails : webSiteDetailsList) {
                    if (companyName.equals(webSiteDetails.getName())) {
                        companyUrl = webSiteDetails.getSearchURL().replace("articleNumber", "") + articleCode;
                        webSiteID = webSiteDetails.getId();
                        return true;

                    }

                }
            } else {
                AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.title_alert), getResources().getString(R.string.msg_select_company), true);

                return false;

            }


        } else {
            AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.title_alert), getResources().getString(R.string.msg_enter_url), true);

        }

        return false;


    }

    private String getCompanyName(String companyUrl) {
        for (WebSiteDetails webSiteDetails : webSiteDetailsList) {
            if (companyUrl.contains("hm") && webSiteDetails.getName().toLowerCase().equals("h&m")) {
                companyName = webSiteDetails.getName();
                webSiteID = webSiteDetails.getId();
            } else if (companyUrl.contains("net-a-porter") && webSiteDetails.getName().toLowerCase().equals("net a porter")) {

                companyName = webSiteDetails.getName();
                webSiteID = webSiteDetails.getId();
            } else if (companyUrl.contains(webSiteDetails.getName().toLowerCase())) {
                companyName = webSiteDetails.getName();
                webSiteID = webSiteDetails.getId();
                break;
            }
        }
        return companyName;
    }


    private void createAlert() {

        DialogUtitlity.showLoading(CreateAlertActivity.this, getResources().getString(R.string.msg_create_alert));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        createAlertRequest = new CreateAlertRequest();
        createAlertRequest.setUrl(companyUrl);
        createAlertRequest.setWebSite(companyName);
        createAlertRequest.setWebSiteID(webSiteID);

        final Call<ResponseBody> callClient = apiService.createAlert(ApiClient.getHeaders(), createAlertRequest);
        callClient.enqueue(this);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OCR_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                descriptionTextView.setText(data.getStringExtra("article"));
            }
        }
    }

   /* private void logOut() {
        // LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(CreateAlertActivity.this, LoginActivity.class));
    }*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                if (resultResponse.isSuccess()) {
                    AlertDialogManager.showAlertDialog1(CreateAlertActivity.this, "", getResources().getString(R.string.msg_alert_success), false, this);


                } else {

                    AlertDialogManager.showAlertDialog(CreateAlertActivity.this, getResources().getString(R.string.alert_dialog_error_title), resultResponse.getMessage(), true);
                }
            }
        }


    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        DialogUtitlity.hideLoading();
        AlertDialogManager.showAlertDialog(CreateAlertActivity.this, getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.msg_alert_not_sucess), true);

    }


    //for getWebSite details
    private void getWebsiteDetails() {
        DialogUtitlity.showLoading(this);
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
                        webSiteDetailsList.clear();
                        webSiteDetailsList.addAll(webSiteDetailsResponse.getData());
                        dataBaseAdapter.openDataBase();
                        dataBaseAdapter.addAllWebsiteDetails(webSiteDetailsList);


                    }
                    if (resultResponse.isSuccess()) {
                        Log.v(TAG, String.valueOf(webSiteDetailsList.size()));
                    }
                }

            }

            @Override
            public void onFailure(Call<WebSiteDetailsResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private boolean checkNetWorKConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            showSnack(isConnected);
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
            Snackbar snackbar = Snackbar.make(findViewById(R.id.create_alert_activity), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }


    @Override
    public void alertDialogCallbackOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();


    }
}
