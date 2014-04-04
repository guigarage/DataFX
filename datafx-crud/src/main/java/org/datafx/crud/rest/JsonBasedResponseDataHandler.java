package org.datafx.crud.rest;

import java.io.InputStream;
import java.util.function.Function;

public class JsonBasedResponseDataHandler<T> implements Function<InputStream, T> {

    @Override
    public T apply(InputStream inputStream) {
        //TODO
        return null;
    }
}
