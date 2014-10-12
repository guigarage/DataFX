package io.datafx.messages;

import io.datafx.controller.context.ViewContext;
import io.datafx.core.DataFXUtils;
import io.datafx.core.concurrent.Async;
import io.datafx.core.concurrent.ThreadType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by hendrikebbers on 12.10.14.
 */
public class MessageBusRegistration {

    public <T> void registerMessageConsumer(ViewContext<T> context) {
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
}
