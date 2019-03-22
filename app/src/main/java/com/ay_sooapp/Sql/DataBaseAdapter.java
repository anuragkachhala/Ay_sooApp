package com.ay_sooapp.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ay_sooapp.Model.AlertDetailsData;
import com.ay_sooapp.Model.WebSiteDetails;

import java.util.ArrayList;
import java.util.List;

public class DataBaseAdapter {
    public static final String TAG = DataBaseAdapter.class.getName();

    Context context;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public DataBaseAdapter(Context context) {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context);
    }

    // open DataBase
    public void openDataBase() {
        try {
            sqLiteDatabase = dataBaseHelper.getWritableDatabase();
            //sqLiteDatabase.beginTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //close DataBase
    public void closeDataBase() {

        try {
            dataBaseHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void dropTable() {
        openDataBase();

        sqLiteDatabase.execSQL("delete from " + DataBaseConstant.TABLE_USER_ALERTS);
        sqLiteDatabase.execSQL("delete from " + DataBaseConstant.TABLE_WEBSITE_DETAILS);

    }

    public void addAllWebsiteDetails(List<WebSiteDetails> webSiteDetailsList) {

        openDataBase();
        long rowId;
        ContentValues values;

        for (WebSiteDetails webSiteDetails : webSiteDetailsList) {
            values = new ContentValues();
            values.put(DataBaseConstant.COLUMN_WEBSITE_DETAILS_ID, webSiteDetails.getId());
            values.put(DataBaseConstant.COLUMN_WEBSITE_WEBSITE_NAME, webSiteDetails.getName());
            values.put(DataBaseConstant.COLUMN_WEBSITE_WEBSITE_URL, webSiteDetails.getURL());
            values.put(DataBaseConstant.COLUMN_WEBSITE_SEARCH_URL, webSiteDetails.getSearchURL());
            rowId = sqLiteDatabase.insert(DataBaseConstant.TABLE_WEBSITE_DETAILS, null, values);
            Log.v(TAG, values + " " + rowId);

        }
    }

    public List<WebSiteDetails> getAllWebSiteDetails() {
        List<WebSiteDetails> webSiteDetailsList = new ArrayList<>();
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(DataBaseConstant.SELECT_ALL_WEBSITE, null);
        if (cursor.moveToFirst()) {
            do {
                WebSiteDetails webSiteDetails = new WebSiteDetails();
                webSiteDetails.setId(cursor.getLong(cursor.getColumnIndex(DataBaseConstant.COLUMN_WEBSITE_DETAILS_ID)));
                webSiteDetails.setName(cursor.getString(cursor.getColumnIndex(DataBaseConstant.COLUMN_WEBSITE_WEBSITE_NAME)));
                webSiteDetails.setURL(cursor.getString(cursor.getColumnIndex(DataBaseConstant.COLUMN_WEBSITE_WEBSITE_URL)));
                webSiteDetails.setSearchURL(cursor.getString(cursor.getColumnIndex(DataBaseConstant.COLUMN_WEBSITE_SEARCH_URL)));
                webSiteDetailsList.add(webSiteDetails);
            } while (cursor.moveToNext());

        }

        return webSiteDetailsList;
    }


    public void addUsetAlerts(AlertDetailsData alertDetailsData) {
        openDataBase();
        long rowId;
        ContentValues values;
        values = new ContentValues();
        values.put(DataBaseConstant.COLUMN_ALERT_ID, alertDetailsData.getId());
        values.put(DataBaseConstant.COLUMN_ALERT_CREATED_ON, alertDetailsData.getCreatedOn());
        values.put(DataBaseConstant.COLUMN_ALERT_URL, alertDetailsData.getUrl());
        values.put(DataBaseConstant.COLUMN_ALERT_UPDATED_ON, alertDetailsData.getUpdatedOn());
        values.put(DataBaseConstant.COLUMN_ALERT_STATUS, alertDetailsData.getStatus());
        //values.put(DataBaseConstant.COLUMN_ALERT_ARTICLE_NO, alertDetailsData.getArticalNumber());
        //values.put(DataBaseConstant.COLUMN_ALERT_WEBSITE, alertDetailsData.getWebSite());
        rowId = sqLiteDatabase.insert(DataBaseConstant.TABLE_USER_ALERTS, null, values);
        Log.v(TAG, values + " " + rowId);

    }

    public void addUserAlerts(List<AlertDetailsData> alertDetailsDataList) {

        openDataBase();
        long rowId;
        ContentValues values;
        for (AlertDetailsData alertDetailsData : alertDetailsDataList) {
            values = new ContentValues();
            values.put(DataBaseConstant.COLUMN_ALERT_ID, alertDetailsData.getId());
            values.put(DataBaseConstant.COLUMN_ALERT_CREATED_ON, alertDetailsData.getCreatedOn());
            values.put(DataBaseConstant.COLUMN_ALERT_URL, alertDetailsData.getUrl());
            values.put(DataBaseConstant.COLUMN_ALERT_UPDATED_ON, alertDetailsData.getUpdatedOn());
            values.put(DataBaseConstant.COLUMN_ALERT_STATUS, alertDetailsData.getStatus());
            //values.put(DataBaseConstant.COLUMN_ALERT_ARTICLE_NO, alertDetailsData.getArticalNumber());
            //values.put(DataBaseConstant.COLUMN_ALERT_WEBSITE, alertDetailsData.getWebSite());
            rowId = sqLiteDatabase.insert(DataBaseConstant.TABLE_USER_ALERTS, null, values);
            Log.v(TAG, values + " " + rowId);

        }

    }

    public List<AlertDetailsData> getUserAlerts() {
        List<AlertDetailsData> alertList = new ArrayList<>();
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(DataBaseConstant.SELECT_ALL_ALERTS, null);
        if (cursor.moveToFirst()) {
            do {
                AlertDetailsData alertDetailsData = new AlertDetailsData();
                //alertDetailsData.setId(cursor.getLong(cursor.getColumnIndex(DataBaseConstant.COLUMN_ALERT_ID)));
                // alertDetailsData.setCreatedOn(cursor.getString(cursor.getColumnIndex(DataBaseConstant.COLUMN_ALERT_CREATED_ON)));
                alertDetailsData.setUrl(cursor.getString(cursor.getColumnIndex(DataBaseConstant.COLUMN_ALERT_URL)));
                alertDetailsData.setUpdatedOn(cursor.getString(cursor.getColumnIndex(DataBaseConstant.COLUMN_ALERT_UPDATED_ON)));
                //alertDetailsData.setStatus(cursor.getLong(cursor.getColumnIndex(DataBaseConstant.COLUMN_ALERT_STATUS)));
                //alertDetailsData.setArticalNumber(cursor.getString(cursor.getColumnIndex(DataBaseConstant.COLUMN_ALERT_ARTICLE_NO)));
                //alertDetailsData.setWebSite(cursor.getString(cursor.getColumnIndex(DataBaseConstant.COLUMN_ALERT_WEBSITE)));
                alertList.add(alertDetailsData);
            } while (cursor.moveToNext());
        }

        return alertList;

    }

    public boolean deleteUserAlert(long Id) {

        return sqLiteDatabase.delete(DataBaseConstant.TABLE_USER_ALERTS, DataBaseConstant.COLUMN_ALERT_ID + " = " + Id, null) > 0;

    }

}
