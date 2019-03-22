package com.ay_sooapp.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ay_sooapp.Interfacce.AlertDialogCallback;

import static android.content.DialogInterface.OnClickListener;

public class AlertDialogManager {

    public static void showAlertDialog(Context context, String title, String message, Boolean status) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(status);

        /*if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);*/

        alertDialog.setPositiveButton("ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = alertDialog.create();
        // Showing Alert Message
        alertDialog.show();
    }


    public static void showAlertDialog1(Context context, String title, String message, Boolean status, final AlertDialogCallback listener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(status);

        /*if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);*/

        alertDialog.setPositiveButton("ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (listener == null) {
                    dialogInterface.dismiss();
                } else {
                    listener.alertDialogCallbackOk();
                }
            }
        });

        AlertDialog dialog = alertDialog.create();
        // Showing Alert Message
        alertDialog.show();
    }

}

