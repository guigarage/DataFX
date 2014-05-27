package org.datafx.util;

@FunctionalInterface
public interface Factory<T, R> {

    R create(T t) throws Exception;
}
