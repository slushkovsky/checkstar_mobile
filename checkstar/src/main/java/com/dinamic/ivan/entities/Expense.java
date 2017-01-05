package com.dinamic.ivan.entities;

import com.google.gson.JsonObject;


public class Expense {
    public double price;
    public String currency;
    public ExpenseCategory category;
    public String name;
    public ExpenseDetector detectedBy;

    public Expense(double price, String currency, ExpenseCategory category, String name, ExpenseDetector detectedBy) {
        this.price = price;
        this.currency = currency;
        this.category = category;
        this.name = (name == null ? "unknown": name);
        this.detectedBy = detectedBy;
    }

    public static Expense fromJsonObject(JsonObject json) {
        double price      = json.get("price")      .getAsDouble();
        String currency   = json.get("currency")   .getAsString();
        String category   = json.get("category")   .getAsString();
        String name       = json.get("name")       .getAsString();
        String detectedBy = json.get("detected_by").getAsString();

        return new Expense(price, currency, ExpenseCategory.valueOf(category), name, ExpenseDetector.valueOf(detectedBy));
    }

    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();

        obj.addProperty("price",       this.price);
        obj.addProperty("currency",    this.currency);
        obj.addProperty("category",    this.category.toString());
        obj.addProperty("name",        this.name);
        obj.addProperty("detected_by", this.detectedBy.toString());

        return obj;
    }
}
