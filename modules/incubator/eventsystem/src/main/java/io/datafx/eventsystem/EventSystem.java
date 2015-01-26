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
package io.datafx.eventsystem;

import io.datafx.core.ExceptionHandler;
import io.datafx.core.concurrent.ConcurrentUtils;
import io.datafx.core.concurrent.ObservableExecutor;
import io.datafx.core.concurrent.ThreadType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * The DataFX Event System. The event system is defined as a singleton and therefore you can only have one instance
 * in an application. You can get the event system by calling {@link #getInstance()}.
 * The event system can be used to send events to other parts of the application an decouple a big app by doing so.
 * Events can be send synchron (on the JavaFX Application Thread) or asynchron (on a background thread). Each event
 * will contain content (any Java object) as data for the receiver. When sending an event an adress must be specified.
 * Several receiver can be registered for an adress. In this case all receivers will get the event.
 */
public final class EventSystem {

    /**
     * The map contains lists of receivers for defined adresses.
     * The reciever will get the event on the JavaFX Application Thread
     */
    private ObservableMap<String, ObservableList<Consumer<Event>>> consumers;

    /**
     * The map contains lists of receivers for defined adresses.
     * The reciever will get the event on a background thread
     */
    private ObservableMap<String, ObservableList<Consumer<Event>>> asyncConsumers;

    /**
     * The exception handler of the event system
     */
    private ExceptionHandler exceptionHandler;

    /**
     * The executor to send async events
     */
    private Executor executor;

    /**
     * the single instance
     */
    private static final EventSystem instance = new EventSystem();

    /**
     * default constructor
     */
    private EventSystem() {
        consumers = FXCollections.observableHashMap();
        asyncConsumers = FXCollections.observableHashMap();
        exceptionHandler = ExceptionHandler.getDefaultInstance();
        executor = ObservableExecutor.getDefaultInstance();
    }

    /**
     * Returns the event system instance. The event system is defined as singleton and therefor this will always
     * return the same instance.
     * @return the event system instance
     */
    public static EventSystem getInstance() {
        return instance;
    }

    /**
     * Send an event to all receivers that are registered for the given <tt>address</tt>.
     * The event will contain the given <tt>content</tt> of type <tt>T</tt>
     * @param address The adress
     * @param content the content
     * @param <T> content type
     * @return a worker that will be finished once the event was sent to all receivers.
     */
    public <T> Worker<Void> send(String address, T content) {
        return sendEvent(address, new Event<Object>(content));
    }

    /**
     * Send an event to all receivers that are registered for the given <tt>address</tt>.
     * @param address The adress
     * @param event the event
     * @param <T> content type
     * @return a worker that will be finished once the event was sent to all receivers.
     */
    public <T> Worker<Void> sendEvent(String address, Event<T> event) {
        Task<Void> runner = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateTitle("EventRunner for " + address);

                try {
                    ConcurrentUtils.runAndWait(() -> {
                        ObservableList<Consumer<Event>> eventConsumers = consumers.get(address);
                        if (eventConsumers != null) {
                            eventConsumers.forEach(c -> {
                                try {
                                    c.accept(event);
                                } catch (Exception e) {
                                    exceptionHandler.setException(e);
                                }
                            });
                        }
                    });

                    //TODO: Use ThreadPool
                    ObservableList<Consumer<Event>> eventConsumers = asyncConsumers.get(address);
                    if (eventConsumers != null) {
                        eventConsumers.forEach(c -> {
                            try {
                                c.accept(event);
                            } catch (Exception e) {
                                exceptionHandler.setException(e);
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    exceptionHandler.setException(e);
                } catch (ExecutionException e) {
                    exceptionHandler.setException(e);
                }
                return null;
            }
        };
        return ConcurrentUtils.executeService(executor, ConcurrentUtils.createService(runner));
    }

    /**
     * Send a broadcast event to all receivers that are registered to receive broadcast events.
     * This are all receivers that are registered for the adress ""
     * see {@link #send(String, Object)}
     * @param content the content
     * @param <T> content type
     * @return a worker that will be finished once the event was sent to all receivers.
     */
    public <T> Worker<Void> sendBroadcast(T content) {
        return sendBroadcastEvent(new Event<>(content));
    }

    /**
     * Send a broadcast event to all receivers that are registered to receive broadcast events.
     * This are all receivers that are registered for the adress ""
     * see {@link #sendBroadcast(Object)}
     * @param event the event
     * @param <T> the content type
     * @return a worker that will be finished once the event was sent to all receivers.
     */
    public <T> Worker<Void> sendBroadcastEvent(Event<T> event) {
        return sendEvent("", event);
    }

    /**
     * Registers a receiver for all events that will be send to the given <tt>adress</tt>. The receiver can get
     * the events on the JavaFX application thread or on a background thread.
     * @param address the adress
     * @param receiver the receiver
     * @param type defines if the receiver will get the events on the JavaFX application thread or on
     *             a background thread.
     */
    public void addReceiver(String address, Consumer<Event> receiver, ThreadType type) {
        if (type.equals(ThreadType.EXECUTOR)) {
            if (!asyncConsumers.containsKey(address)) {
                asyncConsumers.put(address, FXCollections.observableArrayList());
            }
            asyncConsumers.get(address).add(receiver);
        } else {
            if (!consumers.containsKey(address)) {
                consumers.put(address, FXCollections.observableArrayList());
            }
            consumers.get(address).add(receiver);
        }
    }

    /**
     * Registers a receiver for all broadcast events. The receiver can get
     * the events on the JavaFX application thread or on a background thread.
     * @param receiver the receiver
     * @param type defines if the receiver will get the events on the JavaFX application thread or on
     *             a background thread.
     */
    public void addBroadcastReceiver(Consumer<Event> receiver, ThreadType type) {
        addReceiver("", receiver, type);
    }

    /**
     * Deregisters the receiver for the given adress.
     * @param address the adress
     * @param receiver the receiver
     */
    public void removeReceiver(String address, Consumer<Event> receiver) {
        if (asyncConsumers.containsKey(address)) {
            asyncConsumers.get(address).remove(receiver);
        }
        if (consumers.containsKey(address)) {
            consumers.get(address).remove(receiver);
        }
    }

    /**
     * Deregisters the receiver
     * @param receiver the receiver
     */
    public void removeBroadcastReceiver(Consumer<Event> receiver) {
        removeReceiver("", receiver);
    }
}
