package com.ay_sooapp.Sql;

class DataBaseConstant {
    // Database Version
    static final int DATABASE_VERSION = 2;

    // Database Name
    static final String DATABASE_NAME = "Aysoo.db";

    static final String TABLE_WEBSITE_DETAILS = "WEBSITE_DETAILS";

    static final String COLUMN_WEBSITE_DETAILS_ID = "WEBSITE_ID";

    static final String COLUMN_WEBSITE_WEBSITE_URL = "WEBSITE_URL";

    static final String COLUMN_WEBSITE_WEBSITE_NAME = "WEBSITE_NAME";

    static final String COLUMN_WEBSITE_SEARCH_URL = "WEBSITE_SEARCH_URL";

    static final String CREATE_TABLE_WEBSITE_DETAILS = " CREATE TABLE " + TABLE_WEBSITE_DETAILS + " ( " +
            COLUMN_WEBSITE_DETAILS_ID + " INTEGER NOT NULL, " +
            COLUMN_WEBSITE_WEBSITE_URL + " TEXT NOT NULL, " +
            COLUMN_WEBSITE_WEBSITE_NAME + " TEXT NOT NULL, " +
            COLUMN_WEBSITE_SEARCH_URL + " TEXT NOT NULL " +
            "); ";


    static final String DROP_TABLE_WEBSITE_DETAILS = " DROP TABLE IF EXISTS " + TABLE_WEBSITE_DETAILS;


    static final String TABLE_USER_ALERTS = "USER_ALERTS";

    static final String COLUMN_ALERT_ID = "ALERT_ID";

    static final String COLUMN_ALERT_URL = "ALERT_URL";

    static final String COLUMN_ALERT_ARTICLE_NO = "ALERT_ARTICLE_NO";

    static final String COLUMN_ALERT_WEBSITE = "ALERT_WEBSITE";

    static final String COLUMN_ALERT_CREATED_ON = "ALERT_CREATED_ON";

    static final String COLUMN_ALERT_UPDATED_ON = "ALERT_UPDATED_ON";
    static final String COLUMN_ALERT_STATUS = "ALERT_STATUS";

    static final String CREATED_TABLE_USER_ALERTS = " CREATE TABLE " + TABLE_USER_ALERTS + " (" +
            COLUMN_ALERT_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            COLUMN_ALERT_URL + " TEXT NOT NULL, " +
            COLUMN_ALERT_WEBSITE + " TEXT NOT NULL, " +
            COLUMN_ALERT_ARTICLE_NO + " TEXT NOT NULL, " +
            COLUMN_ALERT_CREATED_ON + " TEXT NOT NULL," +
            COLUMN_ALERT_UPDATED_ON + " TEXT NOT NULL, " +
            COLUMN_ALERT_STATUS + " INTEGER NOT NULL " + ");";
    static final String DROP_TABLE_USER_ALERTS = " DROP TABLE IF EXISTS " + TABLE_USER_ALERTS;


    static final String SELECT_ALL_ALERTS = " SELECT * FROM " + TABLE_USER_ALERTS;


    static final String DROP_TABLE_ALERTS = " DROP TABLE IF EXIST " + TABLE_USER_ALERTS;


    static final String SELECT_ALL_WEBSITE = " SELECT * FROM " + TABLE_WEBSITE_DETAILS;


}
