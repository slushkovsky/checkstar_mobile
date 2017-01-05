package com.dinamic.ivan.server;


import android.content.Context;

import com.dinamic.ivan.entities.Receipt;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;


public class StorageApi {
    private static final String BASE_URL = "http://a-c-b.tech/checkstar/storage";
    private static final String LOG_TAG = "STORAGE_API";

    private Context context;
    private String userId;

    public StorageApi(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    public void addUserReceipt(Receipt receipt) {
        Ion.with(context)
           .load(BASE_URL + String.format("/users/%1$s/receipts", userId))
           .setJsonObjectBody(receipt.toJsonObject())
           .asJsonObject(); // TODO: Handle response
    }

    public void getUserReceipts(final FutureCallback<List<Receipt>> onResponse) {
        Ion.with(context)
           .load(BASE_URL + String.format("/users/%1$s/receipts", userId))
           .asJsonObject()
           .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject response) { // TODO
               if (response.get("status").getAsString().equals("Error")) {
//                   Log.w(TAG, "Error while getting answer from server");
                   return;
               }

               List<Receipt> receipts = new ArrayList<Receipt>();

               for (JsonElement receipt: response.get("items").getAsJsonArray())
                   receipts.add(Receipt.fromJsonObject(receipt.getAsJsonObject()));

               onResponse.onCompleted(null, receipts);
           }
        });
    }

}
