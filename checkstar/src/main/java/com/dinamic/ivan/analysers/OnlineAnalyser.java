package com.dinamic.ivan.analysers;


import android.content.Context;
import android.util.Log;

import com.dinamic.ivan.entities.DetectedIn;
import com.dinamic.ivan.entities.Expense;
import com.dinamic.ivan.entities.Location;
import com.dinamic.ivan.entities.Receipt;
import com.dinamic.ivan.entities.SMS;
import com.dinamic.ivan.entities.Sale;
import com.dinamic.ivan.exc.GoogleAuthError;
import com.dinamic.ivan.exc.OnlineAnalyserError;
import com.dinamic.ivan.exc.OnlineAnalyserRespFormatError;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OnlineAnalyser extends BaseAnalyser {
    private static final String BASE_URL = "http://a-c-b.tech/checkstar/api";
    private static final String LOG_TAG = "SERVER_API";

    private Context context;
    private String userId;

    public OnlineAnalyser(Context context) {
        this.context = context;
    }

    @Override
    public void recognizeSMS(SMS sms, final FutureCallback<List<Expense>> onExpenses, final FutureCallback<List<Sale>> onSales) {
        Ion.with(context)
           .load(BASE_URL + "/recognize/sms")
           .setJsonObjectBody(sms.toJsonObject())
           .asJsonObject()
           .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject response) {
                        throwIfBadResponse(e, response);

                        JsonArray jsonExpensesArray = response.get("result").getAsJsonObject().get("expenses").getAsJsonArray();
                        JsonArray jsonSalesArray    = response.get("result").getAsJsonObject().get("sales")   .getAsJsonArray();

                        List<Expense> expenses = new ArrayList<Expense>();
                        List<Sale> sales = new ArrayList<Sale>();

                        for (JsonElement el: jsonExpensesArray) {
                            Expense expense = Expense.fromJsonObject(el.getAsJsonObject());
                            expense.detectedIn = DetectedIn.SMS;

                            expenses.add(expense);
                        }

                        for (JsonElement el: jsonSalesArray) {
                            Sale sale = Sale.fromJsonObject(el.getAsJsonObject());
                            sale.detectedIn = DetectedIn.SMS;

                            sales.add(sale);
                        }

                        onExpenses.onCompleted(null, expenses);
                        onSales.onCompleted(null, sales);
                    }
                });
    }

    @Override
    public void recognizeReceipt(String photoPath, final FutureCallback<Receipt> onResponse) {
        Ion.with(context)
                .load(BASE_URL + "/recognize/receipt")
                .setMultipartFile("image", "image/jpeg", new File(photoPath))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject response) {
                        throwIfBadResponse(e, response);

                        JsonObject receiptJsonData = response.get("receipt_data").getAsJsonObject();
                        onResponse.onCompleted(null, Receipt.fromJsonObject(receiptJsonData));
                    }
                });
    }

    @Override
    public void getSalesNearLocation(Location location, String placesKeywords, final FutureCallback<List<Sale>> onResponse) {
        JsonObject req = new JsonObject();
        req.add("location", location.toJsonObject());
        req.addProperty("key_words", placesKeywords);

        Ion.with(context)
                .load(BASE_URL + "/recognize/location")
                .setJsonObjectBody(req)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject response) {
                        throwIfBadResponse(e, response);

                        if (!response.has("sales"))
                            throw new OnlineAnalyserRespFormatError("Missed field 'sales'");

                        List<Sale> sales = new ArrayList<Sale>();

                        for (JsonElement sale: response.get("sales").getAsJsonArray())
                            sales.add(Sale.fromJsonObject(sale.getAsJsonObject()));

                        onResponse.onCompleted(null, sales);
                    }
                });
    }

    @Override
    public void analyseGmail(GoogleSignInAccount account) {
        String serverCode = null;

        try {
            serverCode = account.getServerAuthCode();
        }
        catch (NullPointerException e) {
            Log.e(LOG_TAG, "Auth response haven't server code (onlineMode=true)");
            throw new GoogleAuthError();
        }

        JsonObject state = new JsonObject();
        state.addProperty("id", userId);

        Ion.with(context)
                .load(BASE_URL + String.format("/google_access?code=%1$s&state=%2$s", serverCode, state.toString()))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject response) {
                        throwIfBadResponse(e, response);
                    }
                });
    }

    private boolean throwIfBadResponse(Exception e, JsonObject resp) {
        if (e != null) {
            throw new Error(e.getMessage());
        }

        if (!resp.get("status").getAsString().equals("OK")) {
            if (resp.get("status").getAsString().equals("Error") && resp.has("error_code") && resp.has("message"))
                throw new OnlineAnalyserError(resp);
            else
                throw new OnlineAnalyserRespFormatError();
        }

        return true;
    }
}
