package org.datafx.util;

public enum HttpMethods {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PUT("PUT"),
    DELETE("DELETE"),
    TRACE("TRACE");

    private String name;

    private HttpMethods(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
