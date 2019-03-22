package com.ay_sooapp.FirebaseUtil;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ay_sooapp.ItemDetailActivity;
import com.ay_sooapp.R;
import com.ay_sooapp.Utils.NotificationConfig;
import com.ay_sooapp.Utils.NotificationUtils;
import com.ay_sooapp.Utils.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final String ALERT_ID = "alertId";
    private static final String WEBSITE = "website";
    private static final String SEARCH_URL = "search_url";
    private SessionManager sessionManager;
    private NotificationUtils notificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.v(TAG, "From: " + message.getFrom());

        SessionManager.setContext(getApplicationContext());
        sessionManager = SessionManager.getInstance();


        if (message == null)
            return;

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            Log.v(TAG, "Notification Body: " + message.getNotification().getBody());
            //sendMyNotification(message.getNotification().getBody());
            handleNotification(message.getNotification().getBody());

        }


        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            Log.v(TAG, "Data Payload: " + message.getData().toString());

            try {
                JSONObject json = new JSONObject(message.getData().toString());
                handleDataMessage(json, message.getNotification().getBody());
            } catch (Exception e) {
                Log.v(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json, String message) {

        Log.v(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String url = data.getString("url");
            String websiteId = data.getString("webSiteId");
            long alertId = data.getLong("alertId");

            Log.v(TAG, "title: " + title);
            Log.v(TAG, "message: " + message);
            Log.v(TAG, "url :" + url);
            Log.v(TAG, "WebsiteId :" + websiteId);
            Log.v(TAG, "alertID :" + alertId);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification inNotification Body: notification tray
                Intent resultIntent = new Intent(getApplicationContext(), ItemDetailActivity.class);
                resultIntent.putExtra(ALERT_ID, alertId);
                resultIntent.putExtra(SEARCH_URL, url);
                resultIntent.putExtra(WEBSITE, websiteId);


                showNotificationMessage(getApplicationContext(), getString(R.string.app_name), message, resultIntent);

            }
        } catch (JSONException e) {
            Log.v(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.v(TAG, "Exception: " + e.getMessage());
        }


    }


    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent("pushNotification");
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            sendMyNotification(message);
            /*NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();*/
        } else {
            // If the app is in background, firebase itself handles the notification
            sendMyNotification(message);
        }

    }


        private void sendMyNotification(String message) {

        //On click of notification it redirect to this Activity

        Intent intent = new Intent(this, ItemDetailActivity.class);
        /*String[] messageArray =message.split(" ");
        String message1 = messageArray[4];
        intent.putExtra(ALERT_ID,Integer.parseInt(message1));*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //showNotificationMessage(getApplicationContext(),getString(R.string.app_name),message,intent);


        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.aysoo)
                .setContentTitle(getString(R.string.app_name))
                 .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(sessionManager.isLoggedIn()) {

            notificationManager.notify(0, notificationBuilder.build());
        }else {
            notificationManager.cancelAll();
        }


    }


    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }
}