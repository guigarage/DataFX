package org.datafx.crud.jpa;

import java.util.function.Supplier;

public class CreateOnceSupplier<T> implements Supplier<T> {

    private Supplier<T> innerSupplier;

    private T createdInstance;

    public CreateOnceSupplier(Supplier<T> innerSupplier) {
        this.innerSupplier = innerSupplier;
    }

    public T getCreatedInstance() {
        return createdInstance;
    }

    @Override
    public synchronized T get() {
        if (createdInstance == null) {
            createdInstance = innerSupplier.get();
        }
        return createdInstance;
    }
}
