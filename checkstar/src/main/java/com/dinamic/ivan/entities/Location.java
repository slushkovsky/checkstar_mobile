package com.dinamic.ivan.entities;


import com.google.gson.JsonObject;


// TODO: Values check (0 <= lat <= 90, 0 <= lng <= 180)

public class Location {
    public double lat;
    public double lng;

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public static Location fromJsonObject(JsonObject json) {
        double lat = json.get("lat").getAsDouble();
        double lng = json.get("lng").getAsDouble();

        return new Location(lat, lng);
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();

        json.addProperty("lat", this.lat);
        json.addProperty("lng", this.lng);

        return json;
    }
}
