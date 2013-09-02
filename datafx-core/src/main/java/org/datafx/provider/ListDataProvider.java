package org.datafx.provider;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.datafx.concurrent.ObservableExecutor;
import org.datafx.reader.DataReader;
import org.datafx.reader.WritableDataReader;
import org.datafx.writer.WriteBackHandler;
import org.datafx.writer.WriteBackListProvider;
import org.datafx.writer.WriteBackProvider;
import org.datafx.writer.WriteTransient;

/**
 *
 * @author johan
 */
public class ListDataProvider<T> implements DataProvider<ObservableList<T>>,
        WriteBackProvider<T>, WriteBackListProvider<T> {

    private ObservableList<T> resultList;
    private DataReader<T> reader;
    private Executor executor;
    private WriteBackHandler<T> writeBackHandler;
    private WriteBackHandler<T> entryAddedHandler;

    public ListDataProvider() {}
    
    public ListDataProvider(DataReader<T> reader) {
        this(reader, null, null);
    }

    /**
     * Create a ListDataProvider with a given Executor and an existing
     * ListProperty
     *
     * @param reader
     * @param executor
     * @param existingList the ListProperty that will be populated with the
     * retrieved data. Note that in the past, we had to use a ListProperty
     * rather than an ObservableList since we override the getData() method,
     * which should return ObservableValue<T>
     */
    public ListDataProvider(DataReader<T> reader, Executor executor, ObservableList<T> existingList) {
        this.reader = reader;
        this.executor = executor;
        if (existingList != null) {
            this.resultList = existingList;
        } else {
            this.resultList = FXCollections.<T>observableArrayList();// = new SimpleListProperty<T>(FXCollections.<T>observableArrayList());
        }
    }

    /**
     * This is a convenience method, allowing ObservableList instances (no
     * ListProperties) to be synchronized with the result of the
     * ListDataProvider.
     *
     * @param ol The ObservableList instance that should be synchronized with
     * the data retrieved by this ListDataProvider. Note that this
     * ObservableList will be cleared when calling this method -- as there is no
     * data retrieved yet.
     */
    public void setResultObservableList(final ObservableList<T> ol) {
        this.resultList = ol;
    }

    public void setDataReader(DataReader<T> reader) {
        this.reader = reader;
    }

    public DataReader<T> getDataReader() {
        return reader;
    }

    public Worker<ObservableList<T>> retrieve() {
        final Service<ObservableList<T>> retriever = createService(resultList);
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
    //   private static Map<ObservableList, ListChangeListener> addListeners = new HashMap<ObservableList, ListChangeListener>();
    final static Map<ObservableList, ListChangeListener> addListeners = new HashMap();
    final static List myListeners2 = new LinkedList();

    protected Service<ObservableList<T>> createService(final ObservableList<T> value) {

        return new Service<ObservableList<T>>() {
            @Override
            protected Task<ObservableList<T>> createTask() {
                // We don't want to call writeback handlers while retrieving external data
                ListChangeListener existingListener = null;
                if (entryAddedHandler != null) {
                    synchronized (addListeners) {
                        Set<Map.Entry<ObservableList, ListChangeListener>> entrySet = addListeners.entrySet();
                        for (Entry<ObservableList, ListChangeListener> entry : entrySet) {
                            if (entry.getKey().equals(value)) {
                                existingListener = entry.getValue();
                            }
                        }
                        if (existingListener != null) {
                            value.removeListener(existingListener);
                        }
                    }
                }
                final Task<ObservableList<T>> task = createReceiverTask(value);

                final ListChangeListener myListener = existingListener;
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent arg0) {
                        if (entryAddedHandler != null) {
                            if (myListener != null) {

                                value.addListener(myListener);
                            } else {
                                ListChangeListener<T> localListener =
                                        new ListChangeListener<T>() {
                                    @Override
                                    public void onChanged(final ListChangeListener.Change<? extends T> change) {
                                        while (change.next()) {
                                            Service service = new Service() {
                                                @Override
                                                protected Task createTask() {
                                                    Task task = new Task() {
                                                        @Override
                                                        protected Object call() throws Exception {

                                                            List<? extends T> addedSubList = change.getAddedSubList();
                                                            for (T entry : addedSubList) {
                                                                WritableDataReader dataReader = entryAddedHandler.createDataSource(entry);
                                                                dataReader.writeBack();
                                                            }
                                                            return addedSubList;

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

                                    }
                                };
                                synchronized (addListeners) {
                                    addListeners.put(value, localListener);


                                    value.addListener(localListener);
                                }
                            }
                            //        );
                        }
                    }
                });
                return task;
            }
        };
    }

    protected PublishingTask<T> createPublishingReceiverTask(ObservableList myResult) {
        PublishingTask<T> answer = new PublishingTask<T>(myResult) {
            @Override
            protected void callTask() throws Exception {
                while (getDataReader().next()) {
                    final T entry = getDataReader().get();
                    publish(entry);
                    if (writeBackHandler != null) {
                        checkProperties(entry);
                    }
                }
            }
        };
        return answer;
    }

    protected final Task<ObservableList<T>> createReceiverTask(ObservableList myResult) {
        PublishingTask<T> task = createPublishingReceiverTask(myResult);
        return task;
    }

    public Executor getExecutor() {
        return executor;
    }

    /**
     * returns the data obtained by this provider. This method has to return an
     * ObservableValue, so it wraps the resulting ObservableList into a
     * ListProperty
     *
     * @return
     */
    @Override
    public ListProperty<T> getData() {
        return new SimpleListProperty<T>(resultList);
    }

    @Override
    public void setAddEntryHandler(WriteBackHandler<T> handler) {
        this.entryAddedHandler = handler;
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
            // Only Observable fields without a WriteTransient annotation are considered
            if ((Observable.class.isAssignableFrom(clazz)) && (field.getAnnotation(WriteTransient.class) == null)) {
                try {
                    final Observable observable = AccessController.doPrivileged(new PrivilegedAction<Observable>() {
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
                                
                                Service service = new Service() {
                                    @Override
                                    protected Task createTask() {
                                        Task task = new Task() {
                                            @Override
                                            protected Object call() throws Exception {

                                                WritableDataReader reader = writeBackHandler.createDataSource(target);
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
