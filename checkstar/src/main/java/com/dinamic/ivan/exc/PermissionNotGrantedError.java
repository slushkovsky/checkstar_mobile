package com.dinamic.ivan.exc;


public class PermissionNotGrantedError extends Error {
    public PermissionNotGrantedError(String permission) {
        super(permission);
    }
}
