package com.dinamic.ivan.parsers;


import android.content.Context;

import com.dinamic.ivan.entities.Expense;

import java.util.List;

public class ReceiptAnalyser {
    private Context context;

    public ReceiptAnalyser(Context context) {
        this.context = context;
    }

    public List<Expense> analyse(String photoFilePath) {
//            LogicApi.recognizeReceipt(photoFilePath);
    }
}
