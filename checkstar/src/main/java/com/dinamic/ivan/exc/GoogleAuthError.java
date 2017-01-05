package com.dinamic.ivan.exc;


public class GoogleAuthError extends Error {
    public GoogleAuthError() {}
    public GoogleAuthError(String permission) {
        super(permission);
    }
}
