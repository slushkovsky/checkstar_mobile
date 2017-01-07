package com.dinamic.ivan.parsers;


import android.content.Context;

import com.dinamic.ivan.entities.Expense;

import java.util.ArrayList;
import java.util.List;

public class ReceiptAnalyser {
    private Context context;

    public ReceiptAnalyser(Context context) {
        this.context = context;
    }

    public List<Expense> analyse(String photoFilePath) {
//            LogicApi.recognizeReceipt(photoFilePath);

        return new ArrayList<Expense>(); // NOTE: Hotfix for fast build.
    }
}
