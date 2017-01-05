package com.dinamic.ivan.parsers;


import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.dinamic.ivan.analysers.BaseAnalyser;
import com.dinamic.ivan.analysers.OfflineAnalyser;
import com.dinamic.ivan.analysers.OnlineAnalyser;
import com.dinamic.ivan.receiptsdk.BuildConfig;


public class BaseParser {
    protected Context context;
    protected ContentResolver contentResolver;
    protected BaseAnalyser analyser;

    public BaseParser(Context context) {
        this.context = context;
        this.contentResolver = context.getContentResolver();

        if (BuildConfig.ONLINE_MODE)
            this.analyser = new OnlineAnalyser();
        else
            this.analyser = new OfflineAnalyser();
    }

    protected boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}