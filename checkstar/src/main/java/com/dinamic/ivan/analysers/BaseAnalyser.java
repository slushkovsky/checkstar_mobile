package com.dinamic.ivan.analysers;


import com.dinamic.ivan.entities.Location;
import com.dinamic.ivan.entities.Receipt;
import com.dinamic.ivan.entities.Expense;
import com.dinamic.ivan.entities.Sale;
import com.dinamic.ivan.entities.SMS;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.koushikdutta.async.future.FutureCallback;

import java.util.List;

public abstract class BaseAnalyser {
    abstract void recognizeSMS(SMS sms, final FutureCallback<List<Expense>> onResponse);
    abstract void recognizeReceipt(String photoPath, final FutureCallback<Receipt> onResponse);
    abstract void getSalesNearLocation(Location location, final FutureCallback<List<Sale>> onResponse);
    abstract void analyseGmail(GoogleSignInAccount account);
}
