package org.datafx.util;

/**
 * Defines as a call to any service (db, REST backend). The call is defined by an input parameter and a output parameter.
 * @param <S> type of the input parameter
 * @param <T> type of the output parameter
 */
@FunctionalInterface
public interface Call<S, T> {

    /**
     * execute the call
     * @param input the input parameter for teh call
     * @return the output parameter
     * @throws Exception the exception
     */
    public T call(S input) throws Exception;
}
