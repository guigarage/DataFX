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

import io.datafx.controller.context.ViewContext;
import io.datafx.controller.context.event.ContextPostConstructListener;
import io.datafx.core.DataFXUtils;
import io.datafx.core.concurrent.Async;
import io.datafx.core.concurrent.ObservableExecutor;
import io.datafx.core.concurrent.ThreadType;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A Plugin for the {@link io.datafx.controller.flow.Flow} API that adds support for the following annotations:
 * {@link io.datafx.messages.MessageProducer}
 * {@link io.datafx.messages.MessageTrigger}
 * {@link io.datafx.messages.OnMessage}
 * This class should not be used by a developer. The class will be loaded by SPI and added automatically as a Plugin.
 */
public class MessageBusFlowPlugin implements ContextPostConstructListener {

    /**
     * Register all message consumers in the controller class of the context
     * @param context the context
     * @param <T> type of the view controller
     */
    private <T> void registerMessageConsumer(ViewContext<T> context) {
        T controller = context.getController();

        List<Field> fields = DataFXUtils.getInheritedDeclaredFields(controller.getClass());
        fields.forEach(field -> {
            if (field.isAnnotationPresent(OnMessage.class)) {
                if (Consumer.class.isAssignableFrom(field.getType())) {
                    ThreadType threadType = ThreadType.PLATFORM;
                    if (field.isAnnotationPresent(Async.class)) {
                        threadType = ThreadType.EXECUTOR;
                    }
                    String adress = field.getAnnotation(OnMessage.class).value();
                    Consumer<Message> consumer = DataFXUtils.getPrivileged(field, controller);
                    MessageBus.getInstance().addReceiver(adress, consumer, threadType);
                    context.addContextDestroyedListener(c -> {
                        MessageBus.getInstance().removeReceiver(adress, consumer);
                    });
                } else {
                    throw new RuntimeException("Field can't be used as message receiver! " + field);
                }
            }
        });

        List<Method> methods = DataFXUtils.getInheritedDeclaredMethods(controller.getClass());
        methods.forEach(method -> {
            if (method.isAnnotationPresent(OnMessage.class)) {
                ThreadType threadType = ThreadType.PLATFORM;
                if (method.isAnnotationPresent(Async.class)) {
                    threadType = ThreadType.EXECUTOR;
                }
                String adress = method.getAnnotation(OnMessage.class).value();
                Consumer<Message> consumer = null;
                if (method.getParameterCount() == 0) {
                    consumer = m -> DataFXUtils.callPrivileged(method, controller);
                } else if (method.getParameterCount() == 1) {
                    if (Message.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        consumer = m -> DataFXUtils.callPrivileged(method, controller, m);
                    } else {
                        consumer = m -> DataFXUtils.callPrivileged(method, controller, m.getContent());
                    }
                } else {
                    throw new RuntimeException("Method can't be used as message receiver! " + method);
                }
                MessageBus.getInstance().addReceiver(adress, consumer, threadType);
                final Consumer<Message> finalConsumer = consumer;
                context.addContextDestroyedListener(c -> {
                    MessageBus.getInstance().removeReceiver(adress, finalConsumer);
                });
            }
        });
    }

    /**
     * Returns a list of all message producers for the given producer id in the controller. By doing so a button action can trigger
     * several message producers
     * @param controller the controller
     * @param producerId the producer id
     * @param <T> type of the controller
     * @return list of all message producers
     */
    private <T> List<MessageProducerImpl> findMessageProducer(T controller, String producerId) {
        List<MessageProducerImpl> producers = new ArrayList<>();

        List<Field> fields = DataFXUtils.getInheritedDeclaredFields(controller.getClass());
        fields.forEach(field -> {
            if (field.isAnnotationPresent(MessageProducer.class) && field.getAnnotation(MessageProducer.class).id().equals(producerId)) {
                if (Supplier.class.isAssignableFrom(field.getType())) {
                    ThreadType threadType = ThreadType.PLATFORM;
                    if (field.isAnnotationPresent(Async.class)) {
                        threadType = ThreadType.EXECUTOR;
                    }
                    MessageProducerImpl producer = new MessageProducerImpl();
                    producer.setAdress(field.getAnnotation(MessageProducer.class).value());
                    producer.setThreadType(threadType);
                    producer.setContentSupplier(DataFXUtils.getPrivileged(field, controller));
                    producers.add(producer);
                } else {
                    throw new RuntimeException("Field can't be used as message producer! " + field);
                }
            }
        });

        List<Method> methods = DataFXUtils.getInheritedDeclaredMethods(controller.getClass());
        methods.forEach(method -> {
            if (method.isAnnotationPresent(MessageProducer.class) && method.getAnnotation(MessageProducer.class).id().equals(producerId)) {
                ThreadType threadType = ThreadType.PLATFORM;
                if (method.isAnnotationPresent(Async.class)) {
                    threadType = ThreadType.EXECUTOR;
                }
                MessageProducerImpl producer = new MessageProducerImpl();
                producer.setAdress(method.getAnnotation(MessageProducer.class).value());
                producer.setThreadType(threadType);
                producer.setContentSupplier(() -> DataFXUtils.callPrivileged(method, controller));
                producers.add(producer);
            }
        });

        return producers;
    }

    /**
     * Register all message producers in the controller class of the context
     * @param context the context
     * @param <T> type of the view controller
     */
    private <T> void registerMessageProducer(ViewContext<T> context) {
        T controller = context.getController();

        List<Field> fields = DataFXUtils.getInheritedDeclaredFields(controller.getClass());
        fields.forEach(field -> {
            if (field.isAnnotationPresent(MessageTrigger.class)) {
                if (Node.class.isAssignableFrom(field.getType()) || MenuItem.class.isAssignableFrom(field.getType())) {
                    String producerId = field.getAnnotation(MessageTrigger.class).id();
                    List<MessageProducerImpl> messageProducers = findMessageProducer(context.getController(), producerId);

                    Runnable action = () -> {
                        for (MessageProducerImpl messageProducer : messageProducers) {
                            if (messageProducer.getThreadType().equals(ThreadType.EXECUTOR)) {
                                ObservableExecutor.getDefaultInstance().execute(() -> {
                                    MessageBus.getInstance().send(messageProducer.getAdress(), messageProducer.getContentSupplier().get());
                                });
                            } else {
                                MessageBus.getInstance().send(messageProducer.getAdress(), messageProducer.getContentSupplier().get());
                            }
                        }
                    };

                    if (Node.class.isAssignableFrom(field.getType())) {
                        DataFXUtils.defineNodeAction(DataFXUtils.getPrivileged(field, controller), action);
                    } else if (MenuItem.class.isAssignableFrom(field.getType())) {
                        DataFXUtils.defineItemAction(DataFXUtils.getPrivileged(field, controller), action);
                    }
                } else {
                    throw new RuntimeException("Field can't be used as message producer! " + field);
                }
            }
        });
    }

    @Override
    public void postConstruct(ViewContext context) {
        registerMessageConsumer(context);
        registerMessageProducer(context);
    }

    /**
     * Helper class that defines all properties of a message producer
     */
    private class MessageProducerImpl {

        /**
         * the thread type of the producer
         */
        private ThreadType threadType;

        /**
         * the message adress
         */
        private String adress;

        /**
         * the message supplier
         */
        private Supplier contentSupplier;

        /**
         * Setter for the threadType
         * @param threadType the threadType
         */
        public void setThreadType(ThreadType threadType) {
            this.threadType = threadType;
        }

        /**
         * Setter for the adress
         * @param adress the adress
         */
        public void setAdress(String adress) {
            this.adress = adress;
        }

        /**
         * Setter for the supplier
         * @param contentSupplier the supplier
         */
        public void setContentSupplier(Supplier contentSupplier) {
            this.contentSupplier = contentSupplier;
        }

        /**
         * Getter for the thread type
         * @return the thread type
         */
        public ThreadType getThreadType() {
            return threadType;
        }

        /**
         * Getter for the adress
         * @return the adress
         */
        public String getAdress() {
            return adress;
        }

        /**
         * Getter for the supplier
         * @return the supplier
         */
        public Supplier getContentSupplier() {
            return contentSupplier;
        }
    }

}
