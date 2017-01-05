package com.dinamic.ivan.exc;


import com.google.gson.JsonObject;

public class OnlineAnalyserError extends Error {
    public OnlineAnalyserError(JsonObject resp)  {
        new OnlineAnalyserError(resp.get("error_code").getAsString(), resp.get("message").getAsString());
    }
    public OnlineAnalyserError(String errorCode, String message) {
        super(String.format("%1$s: %2$s", errorCode, message));
    }
}
