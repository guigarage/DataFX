package org.datafx.crud;

public class QueryParameter<T> {

    private String name;

    private T value;

    public QueryParameter(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
