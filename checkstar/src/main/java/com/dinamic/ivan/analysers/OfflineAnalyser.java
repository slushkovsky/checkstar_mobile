package com.dinamic.ivan.analysers;

import android.content.Context;

import com.dinamic.ivan.entities.Location;
import com.dinamic.ivan.entities.Receipt;
import com.dinamic.ivan.entities.Expense;
import com.dinamic.ivan.entities.Sale;
import com.dinamic.ivan.entities.SMS;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.koushikdutta.async.future.FutureCallback;

import java.util.List;



public class OfflineAnalyser extends BaseAnalyser {
    public OfflineAnalyser(Context context) {}

    @Override
    public void recognizeSMS(SMS sms, final FutureCallback<List<Expense>> onExpenses, final FutureCallback<List<Sale>> onSales) {}

    @Override
    public void recognizeReceipt(String photoPath, final FutureCallback<Receipt> onResponse) {}

    @Override
    public void getSalesNearLocation(Location location, String placesKeywords, final FutureCallback<List<Sale>> onResponse) {}

    @Override
    public void analyseGmail(GoogleSignInAccount account) {}
}
