package com.ay_sooapp.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ay_sooapp.Adapter.AlertListAdapter;
import com.ay_sooapp.CreateAlertActivity;
import com.ay_sooapp.ItemDetailActivity;
import com.ay_sooapp.Model.AlertDetailsData;
import com.ay_sooapp.Model.WebSiteDetails;
import com.ay_sooapp.R;
import com.ay_sooapp.Request.DeleteAlertRequest;
import com.ay_sooapp.Response.AlertDataResponse;
import com.ay_sooapp.Response.Data;
import com.ay_sooapp.Response.ResponseBody;
import com.ay_sooapp.Response.Result;
import com.ay_sooapp.RestApi.ApiClient;
import com.ay_sooapp.RestApi.ApiInterface;
import com.ay_sooapp.Sql.DataBaseAdapter;
import com.ay_sooapp.Utils.AlertDialogManager;
import com.ay_sooapp.Utils.DialogUtitlity;
import com.ay_sooapp.Utils.SessionManager;

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

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class
AlertListFragment extends Fragment implements AlertListAdapter.AlertListAdapterListener, Callback<AlertDataResponse>, View.OnClickListener {

    public static final int REQUEST_CODE = 1000;
    private static final String ALERT_ID = "alertId";
    private static final String WEBSITE = "website";
    private static final String ARTICLE_NO = "article_no";
    private static final String SEARCH_URL = "search_url";
    private static final String LAST_CHECKED_IN = "last_checked_in";
    private static final String ALERT_PRICE = "alert_price";
    private static final String FLAG = "BOOLEAN";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @BindView(R.id.fab)
    FloatingActionButton fab;


    AlertListAdapter alertListAdapter;

    List<AlertDetailsData> alertDetailsDataList = new ArrayList<>();
    Result resultResponse;
    Data dataResponse;
    private List<WebSiteDetails> webSiteDetailsList = new ArrayList<>();
    private DataBaseAdapter dataBaseAdapter;
    private SessionManager sessionManager;
    private String currentPrice;
    private DeleteAlertRequest deleteAlertRequest;


    public AlertListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alert_list, container, false);
        ButterKnife.bind(this, view);

        dataBaseAdapter = new DataBaseAdapter(getContext());
        dataBaseAdapter.openDataBase();
        webSiteDetailsList.clear();
        webSiteDetailsList.addAll(dataBaseAdapter.getAllWebSiteDetails());
        alertListAdapter = new AlertListAdapter(getContext(), alertDetailsDataList, this, webSiteDetailsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(alertListAdapter);
        recyclerView.setHasFixedSize(true);
        SessionManager.setContext(getApplicationContext());
        sessionManager = SessionManager.getInstance();


        setClickListener();

        getAllAlerts();
        return view;

    }

    private void setClickListener() {
        fab.setOnClickListener(this);
    }


    private void addAlerts() {
        dataBaseAdapter.openDataBase();
        alertDetailsDataList.clear();
        alertDetailsDataList.addAll(dataBaseAdapter.getUserAlerts());
        alertListAdapter.notifyDataSetChanged();
    }

    private void getAllAlerts() {
        DialogUtitlity.showLoading(getContext(), getResources().getString(R.string.msg_getting_alerts));
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
                if (getActivity() != null && !getActivity().isFinishing()) {
                    AlertDataResponse alertDataResponse = response.body();
                    resultResponse = alertDataResponse.getResult();
                    alertDetailsDataList.clear();
                    alertDetailsDataList.addAll(alertDataResponse.getData());
                    alertListAdapter.notifyDataSetChanged();
                    if (!resultResponse.isSuccess()) {
                        AlertDialogManager.showAlertDialog(getContext(), getResources().getString(R.string.alert_dialog_error_title),
                                resultResponse.getMessage(), true);


                    }


                }


            }


        } else {
            AlertDialogManager.showAlertDialog(getContext(), getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.msg_login_not_success), false);
        }

    }


    @Override
    public void onFailure(Call<AlertDataResponse> call, Throwable t) {
        DialogUtitlity.hideLoading();
        AlertDialogManager.showAlertDialog(getContext(), getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.msg_login_not_success), false);

    }


    @Override
    public void onDeleteIconClick(AlertDetailsData alertDetailData) {
        AlertDetailsData alertDetailsData = alertDetailData;
        deleteAlertRequest = new DeleteAlertRequest();
        deleteAlertRequest.setAlertId((int) (long) alertDetailsData.getId());
        deleteAlertRequest.setUrl(alertDetailsData.getUrl());
        deleteAlertRequest.setWebSite(getWebSite(alertDetailsData.getWebsiteID()));
        deleteAlertRequest.setArticalNumber("");
        //deleteAlertRequest.setEmail(sessionManager.getEmailID());
        deleteAlertRequest.setStatus(5);
        DialogUtitlity.showLoading(getContext(), getResources().getString(R.string.msg_alert_deleting_alert));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> callClient = apiService.deleteAlert(ApiClient.getHeaders(), deleteAlertRequest);
        callClient.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DialogUtitlity.hideLoading();
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body() instanceof ResponseBody) {
                        ResponseBody responseBody = response.body();

                        if (responseBody.getResult().isSuccess()) {
                            getAllAlerts();
                        } else {
                            AlertDialogManager.showAlertDialog(getContext(), getResources().getString(R.string.alert_dialog_error_title),
                                    getResources().getString(R.string.err_msg_deleting_alert), false);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DialogUtitlity.hideLoading();
                AlertDialogManager.showAlertDialog(getContext(), getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.err_msg_deleting_alert), false);

            }
        });

                /* alertDetailsDataList.remove(position);
         alertListAdapter.notifyDataSetChanged();
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(getContext());
        dataBaseAdapter.openDataBase();
        boolean isRemoved = dataBaseAdapter.deleteUserAlert(id);
        if (isRemoved) {
            Toast.makeText(getContext(), "removed", Toast.LENGTH_SHORT).show();
        }*/


        //addAlerts();


    }


    private String getWebSite(Integer websiteID) {
        for (WebSiteDetails webSiteDetails : webSiteDetailsList) {
            if (webSiteDetails.getId() == websiteID) {
                return webSiteDetails.getName();
            }
        }
        return null;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        AlertDetailsData alertDetailsData = alertDetailsDataList.get(position);
        intent.putExtra(FLAG, true);
        intent.putExtra(ALERT_ID, alertDetailsData.getId());
        intent.putExtra(WEBSITE, getWebSite(alertDetailsData.getWebsiteID()));
        intent.putExtra(SEARCH_URL, alertDetailsData.getUrl());
        String lastCheckedDate = alertDetailsData.getLastCheckedDate();
        intent.putExtra(LAST_CHECKED_IN, lastCheckedDate.replace("T", ", ").substring(0, lastCheckedDate.indexOf(".")));
        intent.putExtra(ALERT_PRICE, alertDetailsData.getPrice());
        /*if (alertDetailsDataList.get(position).getUrl() != null) {
            intent.putExtra(ARTICLE_NO, alertDetailsDataList.get(position).getUrl());
        } else {
            intent.putExtra(ARTICLE_NO, "");
        }
*/
        startActivity(intent);

    }


    @Override
    public void onCheckKnowClicked(final int position) {
        DialogUtitlity.showLoading(getContext(), getResources().getString(R.string.msg_alert_is_price));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> callClient = apiService.getCurrentPrice(ApiClient.getHeaders(), alertDetailsDataList.get(position).getId());
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


                        currentPrice = dataResponse.getPrice();
                        alertDetailsDataList.get(position).setPrice(currentPrice);

                        alertDetailsDataList.get(position).setLastCheckedDate(getDateCurrentDate());
                        alertListAdapter.notifyDataSetChanged();


                    } else {
                        AlertDialogManager.showAlertDialog(getContext(), getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.msg_login_not_success), false);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DialogUtitlity.hideLoading();
                AlertDialogManager.showAlertDialog(getContext(), getResources().getString(R.string.alert_dialog_error_title), getResources().getString(R.string.err_msg_checking_price), false);
            }
        });


    }

    private String getCurrentTimeStamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String currentTimeStamp = tsLong.toString();
        return currentTimeStamp;
    }


    private String getDateCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("etc/UTC");
        Long tsLong = System.currentTimeMillis() / 1000;
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss.", Locale.getDefault());
        Date currentTimeZone = new Date(tsLong * 1000);
        String date = sdf.format(currentTimeZone);

        return date;

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
                startActivityForResult(new Intent(getContext(), CreateAlertActivity.class), REQUEST_CODE);

        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1000:
                    //TODO get intent data
                    getAllAlerts();
                    break;


            }

        }
    }


}
