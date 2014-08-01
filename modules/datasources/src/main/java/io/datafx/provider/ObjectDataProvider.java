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
 * DISCLAIMED. IN NO EVENT SHALL DATAFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.provider;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import io.datafx.io.DataReader;
import io.datafx.io.ServerSentEventReader;
import io.datafx.io.WritableDataReader;
import io.datafx.io.WriteBackHandler;
import io.datafx.io.WriteBackProvider;
import io.datafx.core.concurrent.ConcurrentUtils;
import io.datafx.core.concurrent.ObservableExecutor;

/**
 *
 * The ObjectDataProvider is an implementation of {@link DataProvider} that allows
 * the retrieval and parsing of data that is represented as a single Java instance.
 * In case a list of entities are expected to be retrieved, a {@link ListDataProvider}
 * should be used. Instances of this class are typically used to populate Observable
 * objects.
 * <p>
 * This class requires a {@link org.datafx.io.DataReader} that either can be passed
 * with the constructor or using the {@link #setDataReader(org.datafx.io.DataReader) } 
 * method.
 * <br>
 * No external data will be retrieved until the {@link #retrieve()} method is called.
 * <p>
 * Developers that prefer the builder approach can choose to use the 
 * {@link ObjectDataProviderBuilder} class to create an
 * instance of ObjectDataProvider.
 * <p>
 * Example:<br>
 * <pre>
 *   XmlConverter xmlConverter = new XmlConverter(MyEntity.class);
 *   RestSource restSource = new RestSource(HOST, xmlConverter);
 *   restSource.setPath("somepath");
 *   ObjectDataProvider&lt;MyEntity&gt; sodp = new ObjectDataProvider(restSource);
 *   sodp.setResultObjectProperty(model.myActiveEntity());
 * </pre>
 * @author johan
 * @param <T> the type of data this provider will deliver
 */
public class ObjectDataProvider<T> implements DataProvider<T>, WriteBackProvider<T>{
    private ObjectProperty<T> objectProperty;
    private Executor executor;
    private DataReader<T> reader;
    private WriteBackHandler<T> writeBackHandler;
    private static final Logger LOGGER = Logger.getLogger(ObjectDataProvider.class.getName());

    /**
     * 
     * Create an ObjectDataProvider. Before calling the {@link #retrieve()} method, a
     * {@link org.datafx.io.DataReader} instance should be set using 
     * {@link #setDataReader(org.datafx.io.DataReader) }.
     */
    public ObjectDataProvider () {  
        this (null, null);
    }
    
    /**
     * Create an ObjectDataProvider that will use the passed <code>reader</code> for
     * retrieving the data
     * @param reader the source of the data.
     */
    public ObjectDataProvider(DataReader<T> reader) {
        this(reader, null);
    }

    /**
     * Create an ObjectDataProvider that will use the passed <code>reader</code> for
     * retrieving the data and the <code>executor</code> for executing the request
     * @param reader the source of the data.
     * @param executor the Executor that will be used for doing the call to the
     * data source. In case this parameter is <code>null</code>, a new Thread will be used.
     */
    public ObjectDataProvider(DataReader<T> reader, Executor executor) {
        this.reader = reader;
        this.executor = executor;
        this.objectProperty = new SimpleObjectProperty<T>();
    }

    /**
     * Set the DataReader that contains the data that will be provided by this
     * ObjectDataProvider
     * @param reader the source of the data.
     */
    public void setDataReader (DataReader<T> reader) {
        this.reader = reader;
    }
    
    /**
     *
     * Explicitly set the {@link java.util.concurrent.Executor} that should be
     * used for retrieving the data.
     * @param executor the Executor instance that should be used when retrieving the data
     */
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
    
    /**
     * Sets the ObjectProperty that contains the result of the data retrieval.
     * This method should not be called once the
     * <code>retrieve</code> method has been called // TODO: enforce this
     *
     * @param result the Property that should be filled with the retrieved value
     */
    @Override
    public void setResultProperty(Property<T> result) {
        setResultObjectProperty((ObjectProperty<T>) result);
    }

    /**
     * Convenience (backward compatible) method. This method is the
     * same as calling {@link #setResultProperty(javafx.beans.property.Property) }.
     * @param result  the Property that should be filled with the retrieved value
     */
    public void setResultObjectProperty(ObjectProperty<T> result) {
        this.objectProperty = result;
    }
    
    @Override
    public Worker<T> retrieve() {
        final Service<T> retriever = createService(objectProperty);
        return ConcurrentUtils.executeService(executor, retriever);
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
                final T entry = reader.get();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        objectProperty.set(entry);
                    }
                });
                LOGGER.log(Level.FINE, "[datafx] Reader did read entry {0}", entry);
                return entry;
            }
        };
        return answer;
    }

    protected Service<T> createService(ObjectProperty<T> value) {
        return new Service<T>() {
            @Override
            protected Task<T> createTask() {
                LOGGER.fine("[datafx] create Receiver task");
                final Task<T> task = createReceiverTask(reader);
                
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent arg0) {
                        T value = null;
                        try {
                            LOGGER.fine("[datafx] get the value of the task");
                            value = task.get();
                            LOGGER.log(Level.FINE, "[datafx] task returned value {0}", value);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            // Execution of the task was not working. So we do
                            // not need
                            // to update the property
                            return;
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            // Execution of the task was not working. So we do
                            // not need
                            // to update the property
                            return;
                        }
                        LOGGER.log(Level.FINER, "[datafx] I will set the value of {0} to {1}", new Object[]{objectProperty, value});
                     //   objectProperty.set(value);
                        System.out.println("DONE settting value");
                        LOGGER.log(Level.FINER, "Do we have a writeBackHandler? {0}", writeBackHandler);
                        if (writeBackHandler != null) {
                            checkProperties(value);
                        }
                        if (reader instanceof ServerSentEventReader){
                            handleKeepReading ((ServerSentEventReader)reader);
                        }
                    }
                });
                return task;
            }
        };
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
