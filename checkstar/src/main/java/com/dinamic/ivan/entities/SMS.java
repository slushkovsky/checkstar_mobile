package com.dinamic.ivan.entities;


import com.google.gson.JsonObject;

public class SMS {
    public String from;
    public String content;

    public SMS(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public static SMS fromJsonObject(JsonObject json) {
        String from    = json.get("from")   .getAsString();
        String content = json.get("content").getAsString();

        return new SMS(from, content);
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();

        json.addProperty("from",    this.from);
        json.addProperty("content", this.content);

        return json;
    }
}
