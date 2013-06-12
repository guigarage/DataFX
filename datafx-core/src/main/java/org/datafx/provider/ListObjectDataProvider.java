package org.datafx.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.WritableListValue;
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
import org.datafx.writer.WriteBackHandler;
import org.datafx.writer.WriteTransient;

/**
 *
 * @author johan
 */
public class ListObjectDataProvider<T> implements DataProvider<ObservableList<T>> {

    private ListProperty<T> listProperty;
    private DataReader<T> reader;
    private Executor executor;

    private WriteBackHandler<T> writeBackHandler;
    
    public ListObjectDataProvider(DataReader<T> reader) {
        this(reader, null, null);
    }

    /**
     * Create a ListObjectDataProvider with a given Executor and an existing ListProperty
     * @param reader
     * @param executor
     * @param existingList the ListProperty that will be populated with the retrieved data. 
     * Note that we have to use a ListProperty rather than an ObservableList since we 
     * override the getData() method, which should return ObservableValue<T>
     */
    public ListObjectDataProvider(DataReader<T> reader, Executor executor, ListProperty<T> existingList) {
        this.reader = reader;
        this.executor = executor;
        if (existingList != null) {
            this.listProperty = existingList;
        } else {
            this.listProperty = new SimpleListProperty<T>(FXCollections.<T>observableArrayList());
        }
    }
    /**
     * This is a convenience method, allowing ObservableList instances (no ListProperties) to 
     * be synchronized with the result of the ListObjectDataProvider. 
     * 
     * @param ol The ObservableList instance that should be synchronized with the data
     * retrieved by this ListObjectDataProvider. Note that this ObservableList will be cleared
     * when calling this method -- as there is no data retrieved yet.
     */
    public void setResultObservableList (final ObservableList<T> ol) {
        ol.clear();
        listProperty.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> change) {
                while (change.next()) {
                    ol.addAll(change.getAddedSubList());
                    ol.removeAll(change.getRemoved());
                }
                
            }
        });
    }

    public void setReader(DataReader<T> reader) {
        this.reader = reader;
    }

    public DataReader<T> getReader() {
        return reader;
    }

    public Worker<ObservableList<T>> retrieve() {
        final Service<ObservableList<T>> retriever = createService(listProperty);
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

    protected Service<ObservableList<T>> createService(WritableListValue<T> value) {
        return new Service<ObservableList<T>>() {
            @Override
            protected Task<ObservableList<T>> createTask() {
                final Task<ObservableList<T>> task = createReceiverTask();
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent arg0) {
                        List<T> value = null;
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

                    }
                });
                return task;
            }
        };
    }

    protected PublishingTask<T> createPublishingReceiverTask() {

        PublishingTask<T> answer = new PublishingTask<T>() {
            @Override
            protected void callTask() throws Exception {
              
                while (getReader().next()) {
                    final T entry = getReader().get();
                    publish(entry);
                    if (writeBackHandler != null) {
                            checkProperties(entry);
                        }
                    
                }
            }
        };
        return answer;
    }

    protected final Task<ObservableList<T>> createReceiverTask() {
        PublishingTask<T> task = createPublishingReceiverTask();
        listProperty.bind(task.getPublishedValues());
        return task;
    }

    public Executor getExecutor() {
        return executor;
    }

    public ListProperty<T> getData() {
        return listProperty;
    }
    
    
    public void setWriteBackHandler(WriteBackHandler<T> handler) {
        this.writeBackHandler = handler;
    }

    private void checkProperties(final T target) {
        Class c = target.getClass();
        Field[] fields = c.getDeclaredFields();
        for (final Field field : fields) {
            Class clazz = field.getType();
            // Only Observable fields without a WriteTransient annotation are considered
            if ((Observable.class.isAssignableFrom(clazz)) &&(field.getAnnotation(WriteTransient.class)==null)) {
                try {
                    final Observable observable = AccessController.doPrivileged(new PrivilegedAction<Observable>() {
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
                    if (observable != null) {
                        observable.addListener(new InvalidationListener() {
                            @Override
                            public void invalidated(Observable o) {
                                System.out.println("DataFX, invalidated called due to "+observable);
                                DataReader reader = writeBackHandler.createDataSource(target);
                                Object response = reader.get();
                            }
                        });
//                        if (ObservableList.class.isAssignableFrom(observable.getClass())) {
//                            ObservableList observableList = (ObservableList)observable;
//                            observableList.addListener(new ListChangeListener(){
//
//                                @Override
//                                public void onChanged(ListChangeListener.Change change) {
//                                    System.out.println("LIST changed");
//                                    DataReader reader = writeBackHandler.createDataSource(objectProperty.get());
//                                    Object response = reader.get();
//                                    System.out.println("done getting response from listchange" + response);
//                                }
//                            });
//                        }
              //          System.out.println("added a listener to "+observable+", class = "+observable.getClass());
                    }

                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SingleObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
