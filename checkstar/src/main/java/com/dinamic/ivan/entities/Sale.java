package com.dinamic.ivan.entities;


import com.google.gson.JsonObject;


public class Sale {
    Store store;
    String saleName;
    String description;

    public Sale(Store store, String saleName, String description) {
        this.store = store;
        this.saleName = saleName;
        this.description = description;
    }

    public static Sale fromJsonObject(JsonObject json) {
        Store store = Store.fromJsonObject(json.get("store").getAsJsonObject());
        String saleName    = json.get("store_name") .getAsString();
        String description = json.get("description").getAsString();

        return new Sale(store, saleName, description);
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();

        json.add("store", this.store.toJsonObject());
        json.addProperty("sale_name",   this.saleName);
        json.addProperty("description", this.description);

        return json;
    }
}
