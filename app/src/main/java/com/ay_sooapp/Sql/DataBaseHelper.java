package com.ay_sooapp.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TAG = DataBaseHelper.class.getName();

    public DataBaseHelper(Context context) {
        super(context, DataBaseConstant.DATABASE_NAME, null, DataBaseConstant.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL(DataBaseConstant.CREATE_TABLE_WEBSITE_DETAILS);
            Log.e(TAG, DataBaseConstant.CREATE_TABLE_WEBSITE_DETAILS);

            sqLiteDatabase.execSQL(DataBaseConstant.CREATED_TABLE_USER_ALERTS);
            Log.e(TAG, DataBaseConstant.CREATED_TABLE_USER_ALERTS);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


        sqLiteDatabase.execSQL(DataBaseConstant.DROP_TABLE_WEBSITE_DETAILS);
        sqLiteDatabase.execSQL(DataBaseConstant.DROP_TABLE_USER_ALERTS);
        onCreate(sqLiteDatabase);


    }
}
