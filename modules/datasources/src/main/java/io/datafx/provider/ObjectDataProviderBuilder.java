/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DATAFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.provider;

import javafx.beans.property.ObjectProperty;
import io.datafx.io.DataReader;
import io.datafx.io.WriteBackHandler;

/**
 * Builder class for creating an {@link ObjectDataProvider}.
 * @author johan
 * @param <T> the type of data that the ObjectDataProvider created by this builder will deliver
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
     * Provides the {@link org.datafx.io.DataReader} that contains the data
     * @param dataReader the DataReader instance that should be used by the ObjectDataProvider
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
     * @param handler the {@link org.datafx.io.WriteBackHandler} instance
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
