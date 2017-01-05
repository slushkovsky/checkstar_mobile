package com.dinamic.ivan.entities;


import com.google.gson.JsonObject;

public class Store {
    String name;
    String address;

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public static Store fromJsonObject(JsonObject json) {
        String name    = json.get("name")   .getAsString();
        String address = json.get("address").getAsString();

        return new Store(name, address);
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();

        json.addProperty("name",    this.name);
        json.addProperty("address", this.address);

        return json;
    }
}
