package org.datafx.util;

@FunctionalInterface
public interface Call<S, T> {

    public T call(S input) throws Exception;
}
