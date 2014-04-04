package org.datafx.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(ExceptionHandler.class.getName());

    private static ChangeListener<Exception> loggerListener;

    private static ExceptionHandler defaultInstance;

    private ObjectProperty<Exception> exception;

    public ExceptionHandler() {
    }

    public static ExceptionHandler getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new ExceptionHandler();
            setExceptionLogging(true);
        }
        return defaultInstance;
    }

    public static ChangeListener<Exception> getLoggerListener() {
        if(loggerListener == null) {
            loggerListener = (ob, o, e) -> LOGGER.log(Level.SEVERE, "DataFX Exception Handler", e);
        }
        return loggerListener;
    }

    public static void setExceptionLogging(boolean logException) {
        if (logException) {
            getDefaultInstance().exceptionProperty().addListener(loggerListener);
        } else {
            getDefaultInstance().exceptionProperty().removeListener(loggerListener);
        }
    }

    public Exception getException() {
        return exceptionProperty().get();
    }

    public void setException(Exception exception) {
        exceptionProperty().set(exception);
    }

    public ObjectProperty<Exception> exceptionProperty() {
        if (exception == null) {
            exception = new SimpleObjectProperty<>();
        }
        return exception;
    }
}
