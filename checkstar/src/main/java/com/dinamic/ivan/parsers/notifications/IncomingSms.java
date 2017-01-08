package com.dinamic.ivan.parsers.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
public class IncomingSms  extends BroadcastReceiver {

    final String TAG = "chechstar log";

    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    Intent msgrcv = new Intent("Msg");
                    Log.i(TAG, "package: " + "");
                    Log.i(TAG, "ticker: " + senderNum);
                    Log.i(TAG, "title: " + senderNum);
                    Log.i(TAG, "text: " + message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}