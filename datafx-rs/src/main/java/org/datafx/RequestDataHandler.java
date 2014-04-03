package org.datafx;

import java.io.OutputStream;

@FunctionalInterface
public interface RequestDataHandler<S> {

    void writeData(S data, OutputStream out);
}
