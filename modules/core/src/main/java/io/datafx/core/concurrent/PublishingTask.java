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
 * DISCLAIMED. IN NO EVENT SHALL DataFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.core.concurrent;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.Arrays;

/**
 * A PublishingTask is used to obtain a number of values, and store them in an
 * ObservableList. The values that are already available can be obtained
 * using the {@link #getPublishedValues()} call.
 *
 * @param <T> The type of the values that are obtained.
 */
public abstract class PublishingTask<T> extends Task<ObservableList<T>> implements Publisher<T> {
    private final ObservableList<T> publishedValues;

    public PublishingTask() {
        this(new SimpleListProperty<T>(FXCollections.<T>observableArrayList()));
    }

    public PublishingTask(ObservableList<T> values) {
        this.publishedValues = values;
    }

    public ObservableList<T> getPublishedValues() {
        return publishedValues;
    }

    @Override
    protected final ObservableList<T> call() throws Exception {
        callTask();
        return publishedValues;
    }

    protected abstract void callTask() throws Exception;

    public void publish(final T... values) {
        if (values != null && values.length > 0) {
            Platform.runLater(() -> publishedValues.addAll(Arrays.asList(values)));
        }
    }
}