package com.dinamic.ivan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dinamic.ivan.entities.Expense;
import com.dinamic.ivan.entities.Sale;
import com.dinamic.ivan.parsers.GmailAnalyser;
import com.dinamic.ivan.parsers.LocationAnalyser;
import com.dinamic.ivan.parsers.NotificationAnayser;
import com.dinamic.ivan.entities.Receipt;
import com.dinamic.ivan.parsers.ReceiptAnalyser;
import com.dinamic.ivan.server.StorageApi;
import com.dinamic.ivan.parsers.SMSParser;
import com.koushikdutta.async.future.FutureCallback;

import java.util.List;

// TODO: Get notifications content
// TODO: Get new SMS's
// TODO: GCM

//        final int MESSAGE_NOTIFICATION_ID = 435345;


public class ReceiptSDK {
    private static class Options {
        boolean useSmsHistory     = true;
        boolean useSmsIncomming   = true;
        boolean useGmailHistory   = true;
        boolean useGmailIncomming = true;
        boolean useNotifications  = true;
        boolean useLocation       = true;

        boolean backupReceipts = true;
        boolean backupExpenses = true;
        boolean backupSales    = true;

        String userId = null;

        FutureCallback<List<Expense>> onExpenses;
        FutureCallback<List<Sale>>    onSales;
    }

    public static class Builder {
        Options opts = new Options();

        public Builder useSmsHisory     (Boolean use) { opts.useSmsHistory     = (use == null ? opts.useSmsHistory     : use); return this; }
        public Builder useSmsIncomming  (Boolean use) { opts.useSmsIncomming   = (use == null ? opts.useSmsIncomming   : use); return this; }
        public Builder useGmailHistory  (Boolean use) { opts.useGmailHistory   = (use == null ? opts.useGmailHistory   : use); return this; }
        public Builder useGmailIncomming(Boolean use) { opts.useGmailIncomming = (use == null ? opts.useGmailIncomming : use); return this; }
        public Builder useNotifications (Boolean use) { opts.useNotifications  = (use == null ? opts.useNotifications  : use); return this; }
        public Builder useLocation      (Boolean use) { opts.useLocation       = (use == null ? opts.useLocation       : use); return this; }

        public Builder backupReceipts(Boolean use) { opts.backupReceipts = (use == null ? opts.backupReceipts : use); return this; }
        public Builder backupExpenses(Boolean use) { opts.backupExpenses = (use == null ? opts.backupExpenses : use); return this; }
        public Builder backupSales   (Boolean use) { opts.backupSales    = (use == null ? opts.backupSales    : use); return this; }

        public Builder setUserId(String userId) { opts.userId = userId; return this; }

        public Builder onExpenses(FutureCallback<List<Expense>> callback) { opts.onExpenses = callback; return this; }
        public Builder onSales   (FutureCallback<List<Sale>>    callback) { opts.onSales    = callback; return this; }

        public ReceiptSDK build(Context context) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor prefsEditor = prefs.edit();

            boolean isFirstTimeRun = prefs.getBoolean("checksar_firstTimeRun", true);

            if (!isFirstTimeRun) {
                opts.useSmsHistory = false;
                opts.useGmailHistory = false;

                String savedUserId = prefs.getString("checkstar_userId", "");

                if (opts.userId != null && opts.userId != savedUserId) {
                    if (BuildConfig.DEBUG)
                        Log.w(LOG_TAG_OPTIONS, String.format("SDK loaded userId '%1$s' is differ with specified in options '%2$s' (will be overwrited)", savedUserId, opts.userId));

                    prefsEditor.putString("checkstar_userId", opts.userId);
                }
            }
            else {
                prefsEditor.putBoolean("checksar_firstTimeRun", false);
                prefsEditor.putString("userId", opts.userId);
            }

            prefsEditor.commit();

            return new ReceiptSDK(context, opts);
        }
    }

//    private LogicApi serverApi  = null;
    private StorageApi storageApi = null;

    private ReceiptAnalyser     receiptsAnalyser    = null;
    private SMSParser           smsAnalyser         = null;
    private GmailAnalyser       gmailAnalyser       = null;
    private NotificationAnayser notificationAnayser = null;
    private LocationAnalyser    locationAnalyser    = null;

    private final static String LOG_TAG_OPTIONS = "SDK_OPTIONS";


    public ReceiptSDK(final Context context, Options opts){
//        this.serverApi  = new LogicApi  (context, opts.userId);
        this.storageApi = new StorageApi(context, opts.userId);

        this.receiptsAnalyser = new ReceiptAnalyser(context);

        this.smsAnalyser = new SMSParser.Builder()
            .useHistory  (opts.useSmsHistory)
            .useIncomming(opts.useSmsIncomming)
            .onExpenses  (opts.onExpenses)
            .onSales     (opts.onSales)
            .build(context);

        /*
        this.gmailAnalyser = new GmailAnalyser.Builder()
            .useHistory          (opts.useGmailHistory)
            .useIncomming        (opts.useGmailIncomming)
            .onExpensesHistorical(opts.onExpensesHistory)
            .onExpensesNew       (opts.onExpensesNew)
            .onSalesHistorical   (opts.onSalesHistory)
            .onSalesNew          (opts.onSalesNew)
            .build(context);
           */

        if (opts.useNotifications)
            this.notificationAnayser = new NotificationAnayser();

//        if (opts.useLocation)
//            this.locationAnalyser = new LocationAnalyser(); // TODO: Sales shops
    }

    public void onActivityResuilt(int requestCode, int resultCode, final Intent intent) {
        this.gmailAnalyser.onActivityResult(requestCode, resultCode, intent);
    }

    public void getReсeiptsBackup(FutureCallback<List<Receipt>> onResult){
        this.storageApi.getUserReceipts(onResult);
    }

    public void getExpensesBackup() {
        // TODO
    }

    public void getSalesBackup() {
        // TODO
    }

}
