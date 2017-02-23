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
package io.datafx.core;

import io.datafx.core.concurrent.ConcurrentUtils;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exception handler for DataFX. By default a handler is used that will log all exceptions.
 */
public class ExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(ExceptionHandler.class.getName());

    private static ChangeListener<Throwable> loggerListener;

    private static ExceptionHandler defaultInstance;

    private static boolean logException = false;

    private ObjectProperty<Throwable> exception;

    public ExceptionHandler() {
    }

    public static synchronized ExceptionHandler getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new ExceptionHandler();
            setExceptionLogging(DataFXConfiguration.getInstance().isExceptionLoggingActive());
        }
        return defaultInstance;
    }

    public static ChangeListener<Throwable> getLoggerListener() {
        if(loggerListener == null) {
            loggerListener = (ob, o, e) -> {
                if(e != null) {
                    if(e instanceof Exception) {
                        LOGGER.log(Level.SEVERE, "DataFX Exception Handler", e);
                    } else {
                        LOGGER.log(Level.SEVERE, "DataFX Exception Handler: " + e.getMessage());
                    }
                }
            };
        }
        return loggerListener;
    }

    public static void setExceptionLogging(boolean log) {
        if (log) {
            getDefaultInstance().exceptionProperty().addListener(getLoggerListener());
        } else {
            getDefaultInstance().exceptionProperty().removeListener(getLoggerListener());
        }
        logException = log;
    }

    public static boolean isLogException() {
        return logException;
    }

    public Throwable getException() {
        return exceptionProperty().get();
    }

    public void setException(Throwable exception) {
        if(Platform.isFxApplicationThread()) {
            exceptionProperty().set(exception);
        }   else {
            try {
                ConcurrentUtils.runAndWait(() -> exceptionProperty().set(exception));
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.log(Level.SEVERE, "Can't handle exception in JavaFX Application Thread!", e);
                LOGGER.log(Level.SEVERE, "Initial exception: ", exception);
            }
        }
    }

    public <T> void observeWorker(Worker<T> worker) {
        worker.exceptionProperty().addListener((ob, ol, e) -> setException(e));
    }

    public ObjectProperty<Throwable> exceptionProperty() {
        if (exception == null) {
            exception = new SimpleObjectProperty<>();
        }
        return exception;
    }
}
