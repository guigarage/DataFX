package org.datafx.provider;

import javafx.beans.property.ObjectProperty;
import org.datafx.reader.DataReader;
import org.datafx.writer.WriteBackHandler;

/**
 * Builder class for creating an {@link ObjectDataProvider}.
 * @author johan
 */
public final class ObjectDataProviderBuilder<T> {
    
    private final ObjectDataProvider objectDataProvider;
    
    /**
     * Creates the initial builder.
     * @param <T> the type of the data that is expected to be returned
     * @return the builder instance
     */
    public static<T> ObjectDataProviderBuilder create() {
        return new ObjectDataProviderBuilder();
    }
    
    private ObjectDataProviderBuilder() {
        this.objectDataProvider = new ObjectDataProvider();
    }
    
    /**
     * Provides the {@link DataReader} that contains the data
     * @param dataReader
     * @return this builder instance
     */
    public ObjectDataProviderBuilder dataReader (DataReader<T> dataReader) {
        this.objectDataProvider.setDataReader(dataReader);
        return this;
    }
    
    /**
     * Pass an ObjectProperty that should be filled with the retrieved value.
     * @param result the ObjectProperty that we want to be used for setting the data
     * @return this builder instance
     */
    public ObjectDataProviderBuilder resultProperty(ObjectProperty<T> result) {
        this.objectDataProvider.setResultObjectProperty(result);
        return this;
    }
    
    /**
     * Provide a handler that will be called when the retrieved data is changed
     * locally.
     * @param handler the {@link WriteBackHandler} instance
     * @return this builder instance
     */
    public ObjectDataProviderBuilder writeBackHandler(WriteBackHandler<T> handler) {
        this.objectDataProvider.setWriteBackHandler(handler);
        return this;
    }
    
    /**
     * Construct the ObjectDataProvider that will provide the data.
     * See {@link ObjectDataProvider} for information on how to retrieve the data.
     * @return the created {@link ObjectDataProvider}
     */
    public ObjectDataProvider build () {
        return this.objectDataProvider;
    }
    
}
