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
package io.datafx.messages;

import io.datafx.core.ExceptionHandler;
import io.datafx.core.concurrent.ConcurrentUtils;
import io.datafx.core.concurrent.DataFxRunnable;
import io.datafx.core.concurrent.ObservableExecutor;
import io.datafx.core.concurrent.ThreadType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Worker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * The DataFX message bus. The message bus is defined as a singleton and therefore you can only have one instance
 * in an application. You can get the message bus by calling {@link #getInstance()}.
 * The message bus can be used to send messages to other parts of the application an decouple a big app by doing so.
 * Messages can be send synchron (on the JavaFX Application Thread) or asynchron (on a background thread). Each message
 * will contain content (any Java object) as data for the receiver. When sending content an adress must be specified.
 * Several receiver can be registered for an adress. In this case all receivers will get the message.
 */
public final class MessageBus {

    /**
     * The map contains lists of receivers for defined adresses.
     * The reciever will get the messages on the JavaFX Application Thread
     */
    private ObservableMap<String, ObservableList<Consumer<Message>>> consumers;

    /**
     * The map contains lists of receivers for defined adresses.
     * The reciever will get the messages on a background thread
     */
    private ObservableMap<String, ObservableList<Consumer<Message>>> asyncConsumers;

    /**
     * The exception handler of the message bus
     */
    private ExceptionHandler exceptionHandler;

    /**
     * The executor to send async messages
     */
    private Executor executor;

    /**
     * the single instance
     */
    private static final MessageBus instance = new MessageBus();

    /**
     * default constructor
     */
    private MessageBus() {
        consumers = FXCollections.observableHashMap();
        asyncConsumers = FXCollections.observableHashMap();
        exceptionHandler = ExceptionHandler.getDefaultInstance();
        executor = ObservableExecutor.getDefaultInstance();
    }

    /**
     * Returns the message bus instance. The message bus is defined as singleton and therefor this will always
     * return the same instance.
     * @return the message bus instance
     */
    public static MessageBus getInstance() {
        return instance;
    }

    /**
     * Send a message to all receivers that are registered for the given <tt>address</tt>.
     * The message will contain the given <tt>content</tt> of type <tt>T</tt>
     * @param address The adress
     * @param content the content
     * @param <T> content type
     * @return a worker that will be finished once the messages was sent to all receivers.
     */
    public <T> Worker<Void> send(String address, T content) {
        return sendMessage(address, new Message<Object>(content));
    }

    /**
     * Send a message to all receivers that are registered for the given <tt>address</tt>.
     * @param address The adress
     * @param message the message
     * @param <T> content type
     * @return a worker that will be finished once the messages was sent to all receivers.
     */
    public <T> Worker<Void> sendMessage(String address, Message<T> message) {
        DataFxRunnable runner = handler -> {
            handler.updateTaskTitle("MessageRunner for " + address);

            try {
                ConcurrentUtils.runAndWait(() -> {
                    ObservableList<Consumer<Message>> messageConsumers = consumers.get(address);
                    if (messageConsumers != null) {
                        messageConsumers.forEach(c -> {
                            try {
                                c.accept(message);
                            } catch (Exception e) {
                                exceptionHandler.setException(e);
                            }
                        });
                    }
                });

                //TODO: Use ThreadPool
                ObservableList<Consumer<Message>> messageConsumers = asyncConsumers.get(address);
                if (messageConsumers != null) {
                    messageConsumers.forEach(c -> {
                        try {
                            c.accept(message);
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

        };
        return ConcurrentUtils.executeService(executor, ConcurrentUtils.createService(runner));
    }

    /**
     * Send a broadcast message to all receivers that are registered to receive broadcast messages.
     * This are all receivers that are registered for the adress ""
     * see {@link #send(String, Object)}
     * @param content the content
     * @param <T> content type
     * @return a worker that will be finished once the messages was sent to all receivers.
     */
    public <T> Worker<Void> sendBroadcast(T content) {
        return sendBroadcastMessage(new Message<>(content));
    }

    /**
     * Send a broadcast message to all receivers that are registered to receive broadcast messages.
     * This are all receivers that are registered for the adress ""
     * see {@link #sendBroadcast(Object)}
     * @param message the message
     * @param <T> the content type
     * @return a worker that will be finished once the messages was sent to all receivers.
     */
    public <T> Worker<Void> sendBroadcastMessage(Message<T> message) {
        return sendMessage("", message);
    }

    /**
     * Registers a receiver for all messages that will be send to the given <tt>adress</tt>. The receiver can get
     * the messages on the JavaFX application thread or on a background thread.
     * @param address the adress
     * @param receiver the receiver
     * @param type defines if the receiver will get the messages on the JavaFX application thread or on
     *             a background thread.
     */
    public void addReceiver(String address, Consumer<Message> receiver, ThreadType type) {
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
     * Registers a receiver for all broadcast messages. The receiver can get
     * the messages on the JavaFX application thread or on a background thread.
     * @param receiver the receiver
     * @param type defines if the receiver will get the messages on the JavaFX application thread or on
     *             a background thread.
     */
    public void addBroadcastReceiver(Consumer<Message> receiver, ThreadType type) {
        addReceiver("", receiver, type);
    }

    /**
     * Deregisters the receiver for the given adress.
     * @param address the adress
     * @param receiver the receiver
     */
    public void removeReceiver(String address, Consumer<Message> receiver) {
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
    public void removeBroadcastReceiver(Consumer<Message> receiver) {
        removeReceiver("", receiver);
    }
}
