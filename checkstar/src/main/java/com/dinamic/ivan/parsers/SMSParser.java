package com.dinamic.ivan.parsers;


import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.dinamic.ivan.analysers.BaseAnalyser;
import com.dinamic.ivan.exc.PermissionNotGrantedError;
import com.dinamic.ivan.entities.Expense;
import com.dinamic.ivan.entities.Sale;
import com.dinamic.ivan.entities.SMS;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.List;


public class SMSParser extends BaseParser {
    private static class Options {
        boolean useHistory   = true;
        boolean useIncomming = true;

        FutureCallback<List<Expense>> onExpenses;
        FutureCallback<List<Sale>>    onSales;
    }

    public static class Builder {
        private Options opts = new Options();

        public Builder useHistory  (Boolean use) { opts.useHistory   = (use == null ? opts.useHistory   : use); return this; }
        public Builder useIncomming(Boolean use) { opts.useIncomming = (use == null ? opts.useIncomming : use); return this; }

        public Builder onExpenses(FutureCallback<List<Expense>> callback) { opts.onExpenses = callback; return this; }
        public Builder onSales   (FutureCallback<List<Sale>>    callback) { opts.onSales    = callback; return this; }

        public SMSParser build(Context context) {
            return new SMSParser(context, opts);
        }
    }

    private Options opts = new Options();

    public SMSParser(Context context, Options opts) {
        super(context);

        this.opts = (opts == null ? this.opts : opts);

        if (opts.useHistory) {
            analyseHistory();
        }

        if (opts.useIncomming) {
            // TODO
        }
    }

    public List<Expense> analyseNewIncommming() {
        throw new Error("Not implemented");
    }

    public List<Expense> analyseHistory(){
        final List<Expense> expenses = new ArrayList<Expense>();
        List<SMS> smsHistory = this.getHistory();

        for (SMS sms: smsHistory)
            analyser.recognizeSMS(sms, opts.onExpenses, opts.onSales);

        return expenses;
    }

    private List<SMS> getHistory() {
        if (!isPermissionGranted(Manifest.permission.READ_SMS))
            throw new PermissionNotGrantedError(Manifest.permission.READ_SMS);

        List<SMS> results = new ArrayList<SMS>();
        Cursor cursor = this.contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String from    = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("body"));

                results.add(new SMS(from, content));

            } while (cursor.moveToNext());
        }

        return results;
    }
}
