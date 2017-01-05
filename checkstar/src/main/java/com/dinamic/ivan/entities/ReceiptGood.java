package com.dinamic.ivan.entities;

import com.google.gson.JsonObject;

import java.io.Serializable;


public class ReceiptGood implements Serializable {
    public String name;
    public GoodUnit unit;
    public double count;
    public double price;

    public ReceiptGood(String name, GoodUnit unit, double count, double price) {
        this.name = name;
        this.unit = unit;
        this.count = count;
        this.price = price;
    }

    public static ReceiptGood fromJsonObject(JsonObject json) {
        String name  = json.get("name") .getAsString();
        String unit  = json.get("unit") .getAsString();
        double count = json.get("count").getAsDouble();
        double price = json.get("price").getAsDouble();

        return new ReceiptGood(name, GoodUnit.valueOf(unit.toUpperCase()), count, price);
    }

    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();

        obj.addProperty("name",  this.name);
        obj.addProperty("unit",  this.unit.toString());
        obj.addProperty("count", this.count);
        obj.addProperty("price", this.price);

        return obj;
    }
}
