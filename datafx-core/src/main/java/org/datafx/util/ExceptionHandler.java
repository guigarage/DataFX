package org.datafx.util;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import org.datafx.DataFXConfiguration;
import org.datafx.concurrent.ConcurrentUtils;

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
                        LOGGER.log(Level.SEVERE, "DataFX Exception Handler", (Exception)e);
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
