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

import javafx.collections.ObservableList;
import io.datafx.io.DataReader;
import io.datafx.io.WriteBackHandler;

/**
 * 
 * Builder class for creating an {@link ListDataProvider}.
 * @author johan
 * @param <T> the type of data that the ListObjectDataProvider created by this builder will deliver
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
     * @param dataReader the DataReader instance that contains the data
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
