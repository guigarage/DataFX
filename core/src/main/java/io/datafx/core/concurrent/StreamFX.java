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

import io.datafx.core.Assert;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Stream Helper class for JavaFX. Combines the Stream API with the JavaFX platform thread
 * @author Hendrik Ebbers
 *
 * @param <T> type of elements in Stream
 */
public class StreamFX<T> {

    private final Stream<T> stream;

    /**
     * Create a StreamFX as a wrapper of a Stream
     * @param stream the stream to wrap
     */
    public StreamFX(final Stream<T> stream) {
        this.stream = Assert.requireNonNull(stream, "stream");
    }

    /**
     * Performs an action for each element of this stream.
     * Each element is wrapped in a ObjectProperty and the action will run on the JavaFX Platform Thread
     * @param action action to perform on the elements
     */
    public void forEach(final Consumer<ObjectProperty<? super T>> action) {
        Assert.requireNonNull(action, "action");
        stream.forEach(t -> Platform.runLater(() -> action.accept(new SimpleObjectProperty<T>(t))));
    }

    /**
     * Performs an action for each element of this stream.
     * Each element is wrapped in a ObjectProperty and the action will run on the JavaFX Platform Thread
     * each element is processed in encounter order for streams that have a
     * defined encounter order
     * @param action action to perform on the elements
     */
    public void forEachOrdered(final Consumer<ObjectProperty<? super T>> action) {
        Assert.requireNonNull(action, "action");
        stream.forEachOrdered(t -> Platform.runLater(() -> action.accept(new SimpleObjectProperty<T>(t))));
    }

    /**
     * Publishes all elements of the stream to a ObservableList.
     * the action for each element will run on the JavaFX Platform Thread
     * @param list List to publish all elements to
     */
    public void publish(final ObservableList<T> list) {
        Assert.requireNonNull(list, "list");
        stream.forEach(t -> Platform.runLater(() -> list.add(t)));
    }

    /**
     * Publishes all elements of the stream to a ObservableList.
     * the action for each element will run on the JavaFX Platform Thread
     * each element is processed in encounter order for streams that have a
     * defined encounter order
     * @param list List to publish all elements to
     */
    public void publishOrderer(final ObservableList<T> list) {
        Assert.requireNonNull(list, "list");
        stream.forEachOrdered(t -> Platform.runLater(() -> list.add(t)));
    }

}
