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
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.datafx.concurrent.ObservableExecutor;
import org.datafx.reader.DataReader;
import org.datafx.reader.ServerSentEventReader;
import org.datafx.reader.WritableDataReader;
import org.datafx.writer.WriteBackHandler;
import org.datafx.writer.WriteBackProvider;

/**
 *
 * @author johan
 */
public class ObjectDataProvider<T> implements DataProvider<T>,
        WriteBackProvider<T>{
    private ObjectProperty<T> objectProperty;
    private Executor executor;
    private DataReader<T> reader;
    private WriteBackHandler<T> writeBackHandler;
    private static final Logger LOGGER = Logger.getLogger(ObjectDataProvider.class.getName());

    public ObjectDataProvider () {  
        this (null, null);
    }
    
    public ObjectDataProvider(DataReader<T> reader) {
        this(reader, null);
    }

    public ObjectDataProvider(DataReader<T> reader, Executor executor) {
        this.reader = reader;
        this.executor = executor;
        this.objectProperty = new SimpleObjectProperty<T>();
    }

    /**
     * Set the DataReader that contains the data that will be provided by this
     * ObjectDataProvider
     * @param reader 
     */
    public void setDataReader (DataReader<T> reader) {
        this.reader = reader;
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

    @Override
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
                            LOGGER.fine("get the value of the task");
                            value = task.get();
                            LOGGER.fine("task returned value "+value);
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
                        LOGGER.log(Level.FINER, "I will set the value of {0} to {1}", new Object[]{objectProperty, value});
                        objectProperty.set(value);
                        if (writeBackHandler != null) {
                            checkProperties(value);
                        }
                        if (reader instanceof ServerSentEventReader){
                            handleKeepReading ((ServerSentEventReader)reader);
//                            createKeepReadingTask((ServerSentEventReader)reader);
                        }
                    }
                });
                return task;
            }
        };
    }
     
    private void handleKeepReading (final ServerSentEventReader reader) {
        Service retriever = createKeepReadingService(reader);
           if (executor != null && executor instanceof ObservableExecutor) {
             ((ObservableExecutor) executor).submit(retriever);
        } else {
            if (executor != null) {
                retriever.setExecutor(executor);
            }
            retriever.start();
        }
    }
    
    private Service createKeepReadingService (final ServerSentEventReader reader) {
        return new Service() {

            @Override
            protected Task createTask() {
                return createKeepReadingTask(reader);
            }
        };
    }
    private Task createKeepReadingTask(final ServerSentEventReader reader) {
        Task answer = new Task() {
            @Override
            protected Object call() throws Exception {
                reader.keepReading();
                return null;
            }
        };
        return answer;
    }
    
    protected Task<T> createReceiverTask(final DataReader<T> reader) {
        Task<T> answer = new Task<T>() {
            @Override
            protected T call() throws Exception {
                T entry = reader.get();
                LOGGER.log(Level.FINE, "reader did read entry {0}", entry);
                return entry;
            }
        };
        return answer;
    }

    @Override
    public ObjectProperty<T> getData() {
        return objectProperty;
    }

    @Override
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
                                Logger.getLogger(ObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(ObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return null;
                        }
                    });
                    if (observable != null) {
                        observable.addListener(new InvalidationListener() {
                            @Override
                            public void invalidated(final Observable o) {
                                final WritableDataReader reader = writeBackHandler.createDataSource(objectProperty.get());
                                Service service = new Service() {
                                    @Override
                                    protected Task createTask() {
                                        Task task = new Task() {
                                            @Override
                                            protected Object call() throws Exception {
                                                reader.writeBack();
                                                return o;
                                            }
                                        };
                                        return task;
                                    }
                                };
                                if (executor != null) {
                                    service.setExecutor(executor);
                                }
                                service.start();
                            }
                        });
                    }

                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(ObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
