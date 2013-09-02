package org.datafx.provider;

import javafx.collections.ObservableList;
import org.datafx.reader.DataReader;
import org.datafx.writer.WriteBackHandler;

/**
 * 
 * Builder class for creating an {@link ListDataProvider}.
 * @author johan
 */
public final class ListDataProviderBuilder<T> {
    
    private final ListDataProvider listDataProvider;
    
    /**
     * Creates the initial builder.
     * @param <T> the type of the data that is expected to be returned
     * @return the builder instance
     */
    public static<T> ListDataProviderBuilder create() {
        return new ListDataProviderBuilder();
    }
    
    private ListDataProviderBuilder() {
        this.listDataProvider = new ListDataProvider();
    }
   /**
     * Provides the {@link DataReader} that contains the data
     * @param dataReader
     * @return this builder instance
     */
    public ListDataProviderBuilder dataReader (DataReader<T> dataReader) {
        this.listDataProvider.setDataReader(dataReader);
        return this;
    }
    
    /**
     * Pass an ObservableList that should be filled with the retrieved values.
     * @param result the ObservableList that we want to filled with the retrieved values
     * @return this builder instance
     */
    public ListDataProviderBuilder resultList(ObservableList<T> result) {
        this.listDataProvider.setResultObservableList(result);
        return this;
    }
    
    /**
     * Provide a handler that will be called when an entry of the retrieved data is changed
     * locally.
     * @param handler the {@link WriteBackHandler} instance
     * @return this builder instance
     */
    public ListDataProviderBuilder writeBackHandler(WriteBackHandler<T> handler) {
        this.listDataProvider.setWriteBackHandler(handler);
        return this;
    }
    
    /**
     * Provide a handler that will be called when a new entry is added to the
     * ObservableList instance passed via the {@link resultList} method by 
     * means of a local call (i.e. not because new external data is available).
     * @param handler the {@link WriteBackHandler} instance
     * @return this builder instance
     */
    public ListDataProviderBuilder addEntryHandler(WriteBackHandler<T> handler) {
        this.listDataProvider.setAddEntryHandler(handler);
        return this;
    }
    
    /**
     * Construct the ListDataProvider that will provide the data.
     * See {@link ListDataProvider} for information on how to retrieve the data.
     * @return the created {@link ListDataProvider}
     */
    public ListDataProvider build () {
        return this.listDataProvider;
    }
}
