package com.dinamic.ivan.entities;


import com.google.gson.JsonObject;

public class Payment {
    double price;
    String currency;
    PaymentMethod type;

    public Payment(double price, String currency, PaymentMethod type) {
        this.price = price;
        this.currency = currency;
        this.type = type;
    }

    public static Payment fromJsonObject(JsonObject json) {
        double price = json.get("amount").getAsDouble();
        String currency = json.get("currency").getAsString();
        PaymentMethod type = PaymentMethod.valueOf(json.get("type").getAsString().toUpperCase());

        return new Payment(price, currency, type);
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();

        json.addProperty("amount",   this.price);
        json.addProperty("currency", this.currency);
        json.addProperty("type",     this.type.toString());

        return json;
    }
}
