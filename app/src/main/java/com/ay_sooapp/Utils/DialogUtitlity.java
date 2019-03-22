package com.ay_sooapp.Utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

public class DialogUtitlity {
    private static ProgressDialog progressDialog;

    public static void showLoading(Context context) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        int style;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            style = android.R.style.Theme_Material_Light_Dialog;
        } else {
            //noinspection deprecation
            style = ProgressDialog.THEME_HOLO_LIGHT;
        }

        progressDialog = new ProgressDialog(context, style);
        progressDialog.setMessage("Loading.......");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    public static void showLoading(Context context, String message) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        int style;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            style = android.R.style.Theme_Material_Light_Dialog;
        } else {
            //noinspection deprecation
            style = ProgressDialog.THEME_HOLO_LIGHT;
        }

        progressDialog = new ProgressDialog(context, style);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
