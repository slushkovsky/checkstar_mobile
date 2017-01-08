package com.dinamic.ivan.entities;


import com.google.gson.JsonObject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class Sale {
    private static DateTimeFormatter JSON_DATETIME_FORMAT = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");

    public Store store;
    public String name;
    public String description;
    public String region;
    public DateTime end;
    public String link;
    public DetectedIn detectedIn;

    public Sale(Store store, String name, String description, String region, DateTime end, String link,  DetectedIn detectedIn) {
        this.store = store;
        this.name = name;
        this.description = description;
        this.region = region;
        this.end = end;
        this.link = link;
        this.detectedIn = detectedIn;
    }

    public static Sale fromJsonObject(JsonObject json) {
        Store store = Store.fromJsonObject(json.get("store").getAsJsonObject());

        String name        = json.get("name")       .getAsString();
        String description = json.get("description").getAsString();

        String region = null;
        DateTime end = null;
        String link = null;

        if (!json.get("region").isJsonNull())
            region = json.get("region").getAsString();

        if (!json.get("end").isJsonNull())
            end = DateTime.parse(json.get("end").getAsString(), JSON_DATETIME_FORMAT); // TODO: recheck parsing

        if (!json.get("link").isJsonNull())
            link = json.get("link").getAsString();

        return new Sale(store, name, description, region, end, link, null);
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();

        json.add("store", this.store.toJsonObject());

        json.addProperty("name",        this.name);
        json.addProperty("description", this.description);
        json.addProperty("region",      this.region);
        json.addProperty("end",         JSON_DATETIME_FORMAT.print(this.end));
        json.addProperty("link",        this.link);

        return json;
    }
}
