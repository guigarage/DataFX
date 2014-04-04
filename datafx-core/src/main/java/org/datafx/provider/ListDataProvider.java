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
package org.datafx.provider;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.datafx.concurrent.ConcurrentUtils;
import org.datafx.reader.DataReader;
import org.datafx.reader.WritableDataReader;
import org.datafx.util.ExceptionHandler;
import org.datafx.writer.WriteBackHandler;
import org.datafx.writer.WriteBackListProvider;
import org.datafx.writer.WriteBackProvider;
import org.datafx.writer.WriteTransient;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ListDataProvider is an implementation of {@link DataProvider} that allows
 * the retrieval and parsing of a list of data entities.
 * In case a single entity is expected to be retrieved, an {@link ObjectDataProvider}
 * should be used. Instances of this class are typically used to populate ObservableList
 * instances.
 * <p>
 * This class requires a {@link org.datafx.reader.DataReader} that either can be passed
 * with the constructor or using the {@link #setDataReader(org.datafx.reader.DataReader) } 
 * method.
 * <br/>
 * No external data will be retrieved until the {@link #retrieve()} method is called.
 * <p>
 * Developers that prefer the builder approach can choose to use the 
 * {@link ListDataProviderBuilder} class to create an
 * instance of ListDataProvider.
 * <p>
 * Example:
 * <br/>
 * <pre>
 *   JsonConverter jsonConverter = new JsonConverter("data", MyEntity.class);
 *   RestSourceBuilder restSourceBuilder = createRestSourceBuilder()
 *         .converter(jsonConverter)
 *         .path("part1").path("part2")
 *   RestSource restSource = restSourceBuilder.build();
 *   ListDataProvider<MyEntity> lodp = new ListDataProvider(restSource);
 *   lodp.setResultObservableList(model.allMyEntities());
 * </pre>
 * 
 */
public class ListDataProvider<T> implements DataProvider<ObservableList<T>>,
    WriteBackProvider<T>, WriteBackListProvider<T> {

    private ListProperty<T> listWrapper = new SimpleListProperty<>();
    private ObservableList<T> observableList;
    private DataReader<T> reader;
    private Executor executor;
    private WriteBackHandler<T> writeBackHandler;
    private WriteBackHandler<T> entryAddedHandler;

    public ListDataProvider() {
        this (null, null, null);
    }
    
    public ListDataProvider(DataReader<T> reader) {
        this(reader, null, null);
    }

    /**
     * Create a ListDataProvider with a given Executor and an existing
     * ListProperty
     *
     * @param reader the DataReader that will obtain the real data
     * @param executor an Executor that will make the (asynchronous) call.
     * @param existingList the ListProperty that will be populated with the
     * retrieved data. Note that in the past, we had to use a ListProperty
     * rather than an ObservableList since we override the getData() method,
     * which should return ObservableValue<T>
     */
    public ListDataProvider(DataReader<T> reader, Executor executor, ObservableList<T> existingList) {
        this.reader = reader;
        this.executor = executor;
        if (existingList != null) {
            this.observableList = existingList;
        } else {
            this.observableList = FXCollections.<T>observableArrayList();// = new SimpleListProperty<T>(FXCollections.<T>observableArrayList());
        }
        this.listWrapper.setValue(observableList);
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
        this.observableList = ol;
        this.listWrapper.setValue(this.observableList);
    }

    public void setDataReader(DataReader<T> reader) {
        this.reader = reader;
    }

    public DataReader<T> getDataReader() {
        return reader;
    }

    public Worker<ObservableList<T>> retrieve() {
        final Service<ObservableList<T>> retriever = createService(observableList);
        return ConcurrentUtils.executeService(executor, retriever);
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
        return (ListProperty<T>) listWrapper;
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

    @Override
    public void setResultProperty(Property<ObservableList<T>> result) {
        this.listWrapper = (ListProperty<T>) result;
    }

}
