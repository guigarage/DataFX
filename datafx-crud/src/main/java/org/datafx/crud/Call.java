package org.datafx.crud;

@FunctionalInterface
public interface Call<S, T> {

    public T call(S input) throws Exception;
}
