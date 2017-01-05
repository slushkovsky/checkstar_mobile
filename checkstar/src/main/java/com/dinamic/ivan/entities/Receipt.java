package com.dinamic.ivan.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class Receipt implements Serializable {
    private static DateTimeFormatter JSON_DATETIME_FORMAT = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    public String currency;
    public ExpenseCategory category;
    public Store store;
    public DateTime datetime;
    public List<ReceiptGood> goods;
    private double sumPrice;

    public Receipt(List<ReceiptGood> goods, Store store, String currency, ExpenseCategory category, DateTime datetime) {
        this.currency  = currency;
        this.goods     = goods;
        this.category  = category;
        this.store     = store;
        this.datetime  = datetime;

        this.sumPrice = 0.0;

        for (ReceiptGood good: goods)
            sumPrice += good.price;
    }

    public Expense getExpense() {
        return new Expense(this.sumPrice, this.currency, this.category, this.store.getName(), ExpenseDetector.RECEIPT);
    }

    public static Receipt fromJsonObject(JsonObject json) {
        String currency  = json.get("currency").getAsString();
        String category  = json.get("category").getAsString();

        Store store = Store.fromJsonObject(json.get("store").getAsJsonObject());

        DateTime datetime = DateTime.parse(json.get("datetime").getAsString(), JSON_DATETIME_FORMAT);

        List<ReceiptGood> goods = new ArrayList<ReceiptGood>();

        for (JsonElement good: json.getAsJsonArray("goods"))
            goods.add(ReceiptGood.fromJsonObject(good.getAsJsonObject()));

        return new Receipt(goods, store, currency, ExpenseCategory.valueOf(category), datetime);
    }

    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        JsonArray goods = new JsonArray();

        for (ReceiptGood good: this.goods)
            goods.add(good.toJsonObject());

        obj.add("goods", goods);
        obj.add("store", this.store.toJsonObject());

        obj.addProperty("currency",   this.currency);
        obj.addProperty("category",   this.category.toString());
        obj.addProperty("datetime",   JSON_DATETIME_FORMAT.print(this.datetime));

        return obj;
    }
}
