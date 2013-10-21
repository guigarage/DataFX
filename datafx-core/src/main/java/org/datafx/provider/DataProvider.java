/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
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
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.provider;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;

/**
 *
 * The DataProvider interface defines how applications interact with the data
 * that is retrieved via a {@link org.datafx.reader.DataReader}.
 * <p>
 * Retrieved data is stored in a {@link javafx.beans.property.Property}.
 * There are three different ways for obtaining the retrieved data:
 * <ol>
 * <li>Call {@link getData()} on the DataProvider. This will return an 
 * {@link javafx.beans.value.ObservableValue} containing the data.
 * <li>Provide a property to the DataProvider, by calling
 * {@link setResultProperty()}. The passed argument is a Property, that
 * will be set when data is retrieved.
 * <li>Obtain the {@link javafx.concurrent.Worker} that is retrieving
 * the data, and observe the {@link javafx.concurrent.Worker#valueProperty()}
 * </ol>
 * @author johan
 * @param <T> the (Java) type of data that will be provided by this DataProvider
 */
public interface DataProvider<T> {
    /**
     * A ObservableValue that wraps the provided Data. When calling this method,
     * the data might or might not be available. In case data is not yet available,
     * adding an {@link javafx.beans.InvalidationListener} or a 
     * {@link javafx.beans.value.ChangeListener} to the result of this
     * method will make sure that you will be notified once data is available.
     *
     * @return An {@link javafx.beans.value.ObservableValue} that contains or will
     * contain the data obtained by this DataProvider.
     */
    ObservableValue<T> getData();
    
    /**
     * Provide an {@link javafx.beans.property.Property} that wraps the to-be-retrieved
     * data.
     * @param result a {@link javafx.beans.property} that already exists in the
     * application, and that will be used by this DataProvider to store the retrieved
     * data.
     */
    void setResultProperty(Property<T> result);

    /**
     * Starts to retrieve the data in a background thread and returns the Worker for
     * monitoring. The Worker has the generic type T. 
     * The incoming data can be observed using the {@link Worker.valueProperty()} property
     *
     * @return Worker that retrieves the data
     * 
     */
    public Worker<T> retrieve();
}
