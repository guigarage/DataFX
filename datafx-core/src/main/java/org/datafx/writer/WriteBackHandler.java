package org.datafx.writer;

import org.datafx.reader.WritableDataReader;

/**
 * A WriteBackHandler is used when data retrieved with DataFX changes due to 
 * a client-action. 
 * @author johan
 */
public abstract class WriteBackHandler<T> {
    /**
     * createDataSource is called by DataFX when a previous retrieved Observable
     * has changed. Implementations of this method should provide the DataReader
     * instance that is capable of making the changes persistent to the 
     * original datastore.
     * 
     * @param observable the (observable) object that has changed
     * @return a DataReader instance, capable of writing the changes back to
     * {persistent/external} storage.
     */
    public abstract WritableDataReader createDataSource (T observable);
}
