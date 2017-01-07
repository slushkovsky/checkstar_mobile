package com.dinamic.ivan.exc;


public class OnlineAnalyserRespFormatError extends Error {
    public OnlineAnalyserRespFormatError() { throw new OnlineAnalyserRespFormatError(""); }
    public OnlineAnalyserRespFormatError(String reason) {
        super("Bad server output format" + (reason.length() != 0 ? ":" : "") + reason);
    }
}
