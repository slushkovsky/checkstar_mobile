package com.dinamic.ivan.parsers.notifications;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationService extends NotificationListenerService {

    Context context;

    final String TAG = "chechstar log";

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        String ticker ="";
        if(sbn.getNotification().tickerText !=null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();
        int id1 = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Bitmap id = sbn.getNotification().largeIcon;


        Log.i(TAG, "Package: " + pack);
        Log.i(TAG, "Ticker: " + ticker);
        Log.i(TAG, "Title: " + title);
        Log.i(TAG, "Text: " + text);

    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "Notification Removed");

    }
}
