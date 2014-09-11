package io.datafx.core.concurrent;

@FunctionalInterface
public interface Publisher<T> {

    void publish(final T... values);
}
