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
 * Created by hendrikebbers on 12.10.14.
 */
public class MessageBusFlowPlugin implements ContextPostConstructListener {

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

    private <T> void registerMessageProducer(ViewContext<T> context) {
        T controller = context.getController();

        List<Field> fields = DataFXUtils.getInheritedDeclaredFields(controller.getClass());
        fields.forEach(field -> {
            if (field.isAnnotationPresent(MessageTrigger.class)) {
                if (Node.class.isAssignableFrom(field.getType()) || MenuItem.class.isAssignableFrom(field.getType())) {
                    String producerId = field.getAnnotation(MessageTrigger.class).id();
                    List<MessageProducerImpl> messageProducers = findMessageProducer(context, producerId);

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

    private class MessageProducerImpl {

        private ThreadType threadType;

        private String adress;

        private Supplier contentSupplier;

        public void setThreadType(ThreadType threadType) {
            this.threadType = threadType;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public void setContentSupplier(Supplier contentSupplier) {
            this.contentSupplier = contentSupplier;
        }

        public ThreadType getThreadType() {
            return threadType;
        }

        public String getAdress() {
            return adress;
        }

        public Supplier getContentSupplier() {
            return contentSupplier;
        }
    }

}
