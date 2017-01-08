package com.dinamic.ivan.parsers.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ServiceReceiver extends BroadcastReceiver {

    final String TAG = "chechstar log";

    @Override
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                Log.i(TAG, "package: " + "");
                Log.i(TAG, "ticker: " + incomingNumber);
                Log.i(TAG, "title: " + incomingNumber);
                Log.i(TAG, "text: " + "");
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }
}