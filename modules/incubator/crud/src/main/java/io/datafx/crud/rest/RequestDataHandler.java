package io.datafx.crud.rest;

import java.io.OutputStream;

@FunctionalInterface
public interface RequestDataHandler<S> {

    void writeData(S data, OutputStream out);
}
