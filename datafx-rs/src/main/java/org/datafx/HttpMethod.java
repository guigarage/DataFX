package org.datafx;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PUT("PUT"),
    DELETE("DELETE"),
    TRACE("TRACE");

    private String name;

    private HttpMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
