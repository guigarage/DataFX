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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

import java.util.function.Consumer;

/**
 * A special task that provides some additional features.
 *
 * @author Hendrik Ebbers
 *
 * @param <V> the result type of method <tt>getValue</tt>
 */
public abstract class DataFxTask<V> extends Task<V> implements TaskStateHandler {

    private final BooleanProperty cancelable;

    public DataFxTask() {
        cancelable = new SimpleBooleanProperty(true);
        updateTitle("Unknown task");
    }

    @Override public void updateTaskTitle(final String title) {
        updateTitle(title);
    }

    @Override public void updateTaskMessage(final String message) {
        updateMessage(message);
    }

    @Override public void updateTaskProgress(final double workDone, final double max) {
        updateProgress(workDone, max);
    }

    @Override public void updateTaskProgress(final long workDone, final long max) {
        updateProgress(workDone, max);
    }

    /**
     * Indicates that the task is currently cancelable.
     * @return true if this task is cancelable, false otherwise
     */
    public BooleanProperty cancelableProperty() {
        return cancelable;
    }

    /**
     * Checks if the task is cancelable
     *
     * @return true if the task is currently cancelable
     */
    public boolean isCancelable() {
        return cancelable.get();
    }

    public void setCancelable(final boolean cancelable) {
        this.cancelable.set(cancelable);
    }

    /* (non-Javadoc)
     * @see javafx.concurrent.Task#cancel(boolean)
     * 
     * throws a RuntimeException if the task is not cancelable
     */
    @Override public boolean cancel(final boolean mayInterruptIfRunning) {
        if (cancelable.get()) {
            return super.cancel(mayInterruptIfRunning);
        } else {
            throw new RuntimeException("Task is not cancelable!");
        }
    }

    public void then(final Consumer<V> consumer) {
        ConcurrentUtils.then(this, consumer);
    }
}
