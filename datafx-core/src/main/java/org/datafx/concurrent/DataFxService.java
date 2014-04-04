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
package org.datafx.concurrent;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.concurrent.Executor;

/**
 * A Service that provides a cancelable property. While the value of this
 * property is false the servce can not be canceled.
 *
 * @author Hendrik Ebbers
 *
 * @param <V> the result type of method <tt>getValue</tt>
 */
public abstract class DataFxService<V> extends Service<V> {
    private BooleanProperty cancelable;

    /**
     * Default Constructor
     */
    public DataFxService() {
        cancelable = new SimpleBooleanProperty(true);
    }

    @Override protected void executeTask(Task<V> task) {
        cancelable.unbind();
        if (task instanceof DataFxTask) {
            cancelable.bind(((DataFxTask<V>) task).cancelableProperty());
        }
        Executor e = getExecutor();
        if (e != null) {
            e.execute(task);
        } else {
            ObservableExecutor.getDefaultInstance().execute(task);
        }
    }

    /**
     * This property wrapps the cancelableProperty of the task that is executed
     * by this service.
     *
     * @return the property
     */
    public ReadOnlyBooleanProperty cancelableProperty() {
        return cancelable;
    }

    /**
     * Returns the current value of the cancelableProperty
     *
     * @return true if this service can be canceled
     */
    public boolean isCancelable() {
        return cancelable.get();
    }
}
