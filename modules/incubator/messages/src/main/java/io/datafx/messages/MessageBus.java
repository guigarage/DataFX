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
 * Created by hendrikebbers on 10.10.14.
 */
public final class MessageBus {

    private ObservableMap<String, ObservableList<Consumer<Message>>> consumers;

    private ObservableMap<String, ObservableList<Consumer<Message>>> asyncConsumers;

    private ExceptionHandler exceptionHandler;

    private Executor executor;

    private static final MessageBus instance = new MessageBus();

    private MessageBus() {
        consumers = FXCollections.observableHashMap();
        asyncConsumers = FXCollections.observableHashMap();
        exceptionHandler = ExceptionHandler.getDefaultInstance();
        executor = ObservableExecutor.getDefaultInstance();
    }

    public static MessageBus getInstance() {
        return instance;
    }

    public <T> Worker<Void> send(String address, T content) {
        return sendMessage(address, new Message<Object>(content));
    }

    private <T> Worker<Void> sendMessage(String address, Message<T> message) {
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

    public <T> Worker<Void> sendBroadcast(T content) {
        return sendBroadcastMessage(new Message<>(content));
    }

    private <T> Worker<Void> sendBroadcastMessage(Message<T> message) {
        return sendMessage("", message);
    }

    public void addConsumer(String address, Consumer<Message> consumer, ThreadType type) {
        if (type.equals(ThreadType.EXECUTOR)) {
            if (!asyncConsumers.containsKey(address)) {
                asyncConsumers.put(address, FXCollections.observableArrayList());
            }
            asyncConsumers.get(address).add(consumer);
        } else {
            if (!consumers.containsKey(address)) {
                consumers.put(address, FXCollections.observableArrayList());
            }
            consumers.get(address).add(consumer);
        }
    }

    public void addGlobalConsumer(Consumer<Message> consumer, ThreadType type) {
        addConsumer("", consumer, type);
    }

    public void removeConsumer(String address, Consumer<Message> consumer) {
        if (asyncConsumers.containsKey(address)) {
            asyncConsumers.get(address).remove(consumer);
        }
        if (consumers.containsKey(address)) {
            consumers.get(address).remove(consumer);
        }
    }

    public void removeGlobalConsumer(Consumer<Message> consumer) {
        removeConsumer("", consumer);
    }
}
