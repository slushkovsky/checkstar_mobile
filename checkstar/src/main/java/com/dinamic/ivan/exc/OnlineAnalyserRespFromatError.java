package com.dinamic.ivan.exc;


public class OnlineAnalyserRespFromatError extends Error {
    public OnlineAnalyserRespFromatError() { OnlineAnalyserRespFromatError(""); }
    public OnlineAnalyserRespFromatError(String reason) {
        super("Bad server output format" + (reason.length() != 0 ? ":" : "") + reason);
    }
}
