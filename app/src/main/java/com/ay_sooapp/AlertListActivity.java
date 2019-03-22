package com.ay_sooapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ay_sooapp.Adapter.AlertListAdapter;
import com.ay_sooapp.Model.AlertData;
import com.ay_sooapp.Model.AlertDetailsData;
import com.ay_sooapp.Model.WebSiteDetails;
import com.ay_sooapp.Response.AlertDataResponse;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Sql.DataBaseAdapter;
import com.ay_sooapp.Utils.AlertDialogManager;
import com.ay_sooapp.Utils.DialogUtitlity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertListActivity extends AppCompatActivity implements Callback<AlertDataResponse>, AlertListAdapter.AlertListAdapterListener {

    private static final String ALERT_ID = "alertId";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    AlertListAdapter alertListAdapter;

    List<AlertData> alertDataList = new ArrayList<>();

    List<AlertDetailsData> alertDetailsDataList = new ArrayList<>();
    List<WebSiteDetails> webSiteDetailsList = new ArrayList<>();

    Result resultResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_list);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //createAlertList();


        alertListAdapter = new AlertListAdapter(this, alertDetailsDataList, this, webSiteDetailsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(alertListAdapter);
        recyclerView.setHasFixedSize(true);

        getAllAlerts();


    }

    private void addAlerts() {
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.openDataBase();
        alertDetailsDataList.clear();
        alertDetailsDataList.addAll(dataBaseAdapter.getUserAlerts());
        alertListAdapter.notifyDataSetChanged();
    }

    private void getAllAlerts() {
        DialogUtitlity.showLoading(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<AlertDataResponse> callClient = apiService.getAllAlerts(ApiClient.getHeaders());
        callClient.enqueue(this);
    }


    @Override
    public void onResponse(Call<AlertDataResponse> call, Response<AlertDataResponse> response) {
        DialogUtitlity.hideLoading();
        int statusCode = response.code();
        if (statusCode == 200) {

            if (response.body() instanceof AlertDataResponse) {

                AlertDataResponse alertDataResponse = response.body();
                resultResponse = alertDataResponse.getResult();

                alertDetailsDataList.addAll(alertDataResponse.getData());
                if (!resultResponse.isSuccess()) {

                    AlertDialogManager.showAlertDialog(this, getResources().getString(R.string.alert_dialog_error_title),
                            resultResponse.getMessage(), true);
                } else {

                    DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(this);
                    dataBaseAdapter.openDataBase();
                    dataBaseAdapter.addUserAlerts(alertDetailsDataList);
                    //alertListAdapter.notifyDataSetChanged();

                    addAlerts();

                }

            }

        }
    }

    @Override
    public void onFailure(Call<AlertDataResponse> call, Throwable t) {

    }


    @Override
    public void onDeleteIconClick(AlertDetailsData alertDetailData) {

    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(AlertListActivity.this, ItemDetailActivity.class);
        intent.putExtra(ALERT_ID, alertDetailsDataList.get(position).getId());
        startActivity(intent);
        finish();

    }

    @Override
    public void onCheckKnowClicked(int position) {

    }

    /*private void createAlertList() {
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter.openDataBase();
        AlertDetailsData alertDetailsData = new AlertDetailsData();
        alertDetailsData.setArticalNumber("42442fsd");
        alertDetailsData.setId(15212212);
        alertDetailsData.setCreatedOn("1531545");
        alertDetailsData.setUrl("fsfs");
        alertDetailsData.setStatus(1);
        alertDetailsData.setUpdatedOn("12131313");
        alertDetailsData.setWebSite("h&m");
        dataBaseAdapter.addUsetAlerts(alertDetailsData);

    }*/


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
