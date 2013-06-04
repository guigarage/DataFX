/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.provider;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.datafx.concurrent.ObservableExecutor;
import org.datafx.reader.DataReader;
import org.datafx.writer.WriteBackHandler;

/**
 *
 * @author johan
 */
public class SingleObjectDataProvider<T> implements DataProvider<T> {
    // we can't make this final since the result objectproperty can be set via setResultObjectProperty.

    private ObjectProperty<T> objectProperty;
    private Executor executor;
    private final DataReader<T> reader;
    private WriteBackHandler<T> writeBackHandler;
    private boolean enableWriteBack;

    public SingleObjectDataProvider(DataReader<T> reader) {
        this(reader, null);
    }

    public SingleObjectDataProvider(DataReader<T> reader, Executor executor) {
        this.reader = reader;
        this.executor = executor;
        this.objectProperty = new SimpleObjectProperty<T>();
    }

    /**
     * Sets the ObjectProperty that contains the result of the data retrieval.
     * This method should not be called once the
     * <code>retrieve</code> method has been called // TODO: enforce this
     *
     * @param result
     */
    public void setResultObjectProperty(ObjectProperty<T> result) {
        this.objectProperty = result;
    }

    public Worker<T> retrieve() {
        final Service<T> retriever = createService(objectProperty);
        retriever.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent arg0) {
                System.err.println("Default DataFX error handler:");
                retriever.getException().printStackTrace();
            }
        });
        if (executor != null && executor instanceof ObservableExecutor) {
            return ((ObservableExecutor) executor).submit(retriever);
        } else {
            if (executor != null) {
                retriever.setExecutor(executor);
            }
            retriever.start();
            return retriever;
        }
    }

    protected Service<T> createService(ObjectProperty<T> value) {
        return new Service<T>() {
            @Override
            protected Task<T> createTask() {
                final Task<T> task = createReceiverTask(reader);
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent arg0) {
                        T value = null;
                        try {
                            value = task.get();
                        } catch (InterruptedException e) {
                            // Execution of the task was not working. So we do
                            // not need
                            // to update the property
                            return;
                        } catch (ExecutionException e) {
                            // Execution of the task was not working. So we do
                            // not need
                            // to update the property
                            return;
                        }
                        objectProperty.set(value);
                        if (enableWriteBack) checkProperties(value);
                      
                    }
                });
                return task;
            }
        };
    }

    public void enableWriteBack(boolean v) {
        this.enableWriteBack = v;
    }
     
    protected Task<T> createReceiverTask(final DataReader<T> reader) {
        System.out.println("[JVDBG] createReceivertask called");
        Task<T> answer = new Task<T>() {
            @Override
            protected T call() throws Exception {
                T entry = reader.get();
                System.out.println("[JVDBG] RECEIVERTASK RETURNS " + entry);
                return entry;
            }
        };
        return answer;
    }

    @Override
    public ObjectProperty<T> getData() {
        return objectProperty;
    }

    public void setWriteBackHandler(WriteBackHandler<T> handler) {
        this.writeBackHandler = handler;
    }

    private void checkProperties(final T target) {
        Class c = target.getClass();
        Field[] fields = c.getDeclaredFields();
        for (final Field field : fields) {
            Class clazz = field.getType();

            if (Observable.class.isAssignableFrom(clazz)) {
                try {
                    Observable observable = AccessController.doPrivileged(new PrivilegedAction<Observable>() {
                        public Observable run() {
                            try {

                                field.setAccessible(true);
                                Object f = field.get(target);
                                Observable answer = (Observable) f;
                                return answer;
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(SingleObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(SingleObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return null;
                        }
                    });
                    observable.addListener(new InvalidationListener() {
                        @Override
                        public void invalidated(Observable o) {
                            DataReader reader = writeBackHandler.createDataSource(objectProperty.get());
                            Object response = reader.get();
                            System.out.println("done getting response " + response);
                        }
                    });

                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SingleObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
