package com.ay_sooapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ay_sooapp.Adapter.AlertDetailListHistoryAdapter;
import com.ay_sooapp.Model.AlertDetailData;
import com.ay_sooapp.Model.AlertItemDetailHistory;
import com.ay_sooapp.Model.WebSiteDetails;
import com.ay_sooapp.Response.AlertDetailResponse;
import com.ay_sooapp.Response.Data;
import com.ay_sooapp.Response.ResponseBody;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Sql.DataBaseAdapter;
import com.ay_sooapp.Utils.AlertDialogManager;
import com.ay_sooapp.Utils.ConnectivityReceiver;
import com.ay_sooapp.Utils.DialogUtitlity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity implements Callback<AlertDetailResponse>, View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = ItemDetailActivity.class.getName();
    private static final String ALERT_ID = "alertId";
    private static final String WEBSITE = "website";
    private static final String SEARCH_URL = "search_url";
    private static final String LAST_CHECKED_IN = "last_checked_in";
    private static final String ALERT_PRICE = "alert_price";
    private static final String FLAG = "BOOLEAN";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_articleNo)
    TextView textViewArticleNO;


    @BindView(R.id.tv_website)
    TextView textViewWebSite;

    @BindView(R.id.tv_pirce)
    TextView textViewPrice;

    @BindView(R.id.tv_checked_in)
    TextView textViewLastCheckedIn;

    @BindView(R.id.btn_check_button)
    Button buttonCheckPriceNow;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    AlertDetailListHistoryAdapter adapter;
    List<AlertItemDetailHistory> alertItemDetailHistoryList = new ArrayList<>();
    List<AlertDetailData> alertDetailDataList = new ArrayList<>();
    Result resultResponse;
    Data dataResponse;
    Boolean isAlertList;
    int alertId;
    private List<WebSiteDetails> webSiteDetailsList = new ArrayList<>();
    private DataBaseAdapter dataBaseAdapter;
    private String website;
    private String url;
    private String lastCheckedIn;
    private String alertPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);

        dataBaseAdapter = new DataBaseAdapter(ItemDetailActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        webSiteDetailsList.clear();
        webSiteDetailsList.addAll(dataBaseAdapter.getAllWebSiteDetails());

        buttonCheckPriceNow.setOnClickListener(this);

        Intent intent = getIntent();
        isAlertList = intent.getBooleanExtra(FLAG, false);
        if (isAlertList) {

            alertId = intent.getIntExtra(ALERT_ID, 0);
            website = intent.getStringExtra(WEBSITE);
            url = intent.getStringExtra(SEARCH_URL);
            alertPrice = intent.getStringExtra(ALERT_PRICE);
            lastCheckedIn = intent.getStringExtra(LAST_CHECKED_IN);
        } else {
            alertId = intent.getIntExtra(ALERT_ID, 0);
            url = intent.getStringExtra(SEARCH_URL);


        }


        getAlert(alertId);
        adapter = new AlertDetailListHistoryAdapter(this, alertDetailDataList, website);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        /*prepareData();*/
    }

    private void getAlert(int alertId) {
        DialogUtitlity.showLoading(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<AlertDetailResponse> callClient = apiService.getAlert(ApiClient.getHeaders(), alertId);
        callClient.enqueue(this);
    }


    @Override
    public void onResponse(Call<AlertDetailResponse> call, Response<AlertDetailResponse> response) {
        DialogUtitlity.hideLoading();
        int statusCode = response.code();
        if (statusCode == 200) {
            if (response.body() instanceof AlertDetailResponse) {
                AlertDetailResponse alertDetailResponse = response.body();
                alertDetailDataList.clear();
                alertDetailDataList.addAll(alertDetailResponse.getData());


                if (alertDetailDataList.size() == 0) {
                    /*buttonCheckPriceNow.setBackground(getResources().getDrawable(R.drawable.disable_btn_bg));
                    buttonCheckPriceNow.setTextColor(getResources().getColor(R.color.colorButtonBackground));
                    buttonCheckPriceNow.setEnabled(false);*/
                    setAlertDetails(null);
                    //AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.alert_dialog_error_title),"Sorry something went wrong..", true);

                } else {

                    adapter.notifyDataSetChanged();
                    setAlertDetails(alertDetailDataList.get(0));
                }

            }


        }
    }


    @Override
    public void onFailure(Call<AlertDetailResponse> call, Throwable t) {
        DialogUtitlity.hideLoading();
        AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.msg_login_not_success), false);

    }


    private String getCompanyName(long webSiteId) {
        String companyName = null;
        for (WebSiteDetails webSiteDetails : webSiteDetailsList) {
            if (webSiteDetails.getId() == webSiteId) {
                companyName = webSiteDetails.getName();
            }


        }
        return companyName;
    }


    private void setAlertDetails(AlertDetailData alertDetails) {
        if (alertDetails != null) {
            alertPrice = alertDetails.getPrice();
            lastCheckedIn = alertDetails.getCreatedOn();
            lastCheckedIn = lastCheckedIn.replace("T", ", ").substring(0, lastCheckedIn.indexOf(".") + 1);
        }
        textViewPrice.setText(alertPrice);
        textViewLastCheckedIn.setText(lastCheckedIn);
        textViewArticleNO.setText(url);
        textViewWebSite.setText(website);

    }


    private String getDate(String time) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss", Locale.getDefault());

        Date currentTimeZone = new Date(((long) Long.parseLong(time)) * 1000);
        String date = sdf.format(currentTimeZone);
        Log.e(TAG, date);
        return date;

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(ItemDetailActivity.this, HomeActivity.class));
        return true;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_check_button:
                if (checkNetWorKConnection()) {

                    checkPriceNow(alertId);
                }
                break;

        }
    }

    private void checkPriceNow(final int alertId) {
        DialogUtitlity.showLoading(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> callClient = apiService.getCurrentPrice(ApiClient.getHeaders(), alertId);
        callClient.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DialogUtitlity.hideLoading();
                int statusCode = response.code();
                if (statusCode == 200) {

                    if (response.body() instanceof ResponseBody) {

                        ResponseBody responseBody = response.body();
                        resultResponse = responseBody.getResult();
                        dataResponse = responseBody.getData();
                        /*currentPrice= dataResponse.getPrice();*/
                        getAlert(alertId);


                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DialogUtitlity.hideLoading();
                AlertDialogManager.showAlertDialog(ItemDetailActivity.this, getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.err_msg_checking_price), false);

            }
        });

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
            Snackbar snackbar = Snackbar.make(findViewById(R.id.item_detail_activity), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
}
