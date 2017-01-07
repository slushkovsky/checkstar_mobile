package com.dinamic.ivan.entities;

import com.google.gson.JsonObject;


public class Expense {
    public Payment payment;
    public ExpenseCategory category;
    public String name;
    public DetectedIn detectedIn;

    public Expense(Payment payment, ExpenseCategory category, String name, DetectedIn detectedIn) {
        this.payment = payment;
        this.category = category;
        this.name = (name == null ? "unknown": name);
        this.detectedIn = detectedIn;
    }

    public static Expense fromJsonObject(JsonObject json) {
        Payment payment = Payment.fromJsonObject(json.get("payment").getAsJsonObject());

        String category   = json.get("category").getAsString();
        String name       = json.get("name")    .getAsString();

        return new Expense(payment, ExpenseCategory.valueOf(category.toUpperCase()), name, null);
    }

    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();

        obj.add("payment", this.payment.toJsonObject());

        obj.addProperty("category",    this.category.toString());
        obj.addProperty("name",        this.name);
        obj.addProperty("detected_by", this.detectedIn.toString());

        return obj;
    }
}
