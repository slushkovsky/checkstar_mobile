package com.dinamic.ivan.analysers;


import android.content.Context;
import android.util.Log;

import com.dinamic.ivan.entities.Expense;
import com.dinamic.ivan.entities.Location;
import com.dinamic.ivan.entities.Receipt;
import com.dinamic.ivan.entities.SMS;
import com.dinamic.ivan.entities.Sale;
import com.dinamic.ivan.exc.GoogleAuthError;
import com.dinamic.ivan.exc.OnlineAnalyserRespFromatError;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OnlineAnalyser extends BaseAnalyser {
    public OnlineAnalyser(Context context) {}

    @Override
    public void recognizeSMS(SMS sms, final FutureCallback<List<Expense>> onResponse) {}

    @Override
    public void recognizeReceipt(String photoPath, final FutureCallback<Receipt> onResponse) {}

    @Override
    public void getSalesNearLocation(Location location, String placesKeywords, final FutureCallback<List<Sale>> onResponse) {}

    @Override
    public void analyseGmail(GoogleSignInAccount account) {}
}
