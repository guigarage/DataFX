package org.datafx.controller.context;

import org.datafx.controller.context.resource.AnnotatedControllerResourceType;
import org.datafx.controller.context.resource.ControllerResourceConsumer;
import org.datafx.util.PrivilegedReflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class ContextResolver<U> {

    private ViewContext<U> context;

    public ContextResolver(ViewContext<U> context) {
        this.context = context;
    }

    public void injectResources(Object object) {

        List<AnnotatedControllerResourceType> allResourceTypes = getAnnotatedControllerResourceTypes();
        List<ControllerResourceConsumer> resourceConsumers = getControllerResourceConsumer();

        Class<? extends Object> cls = object.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (final Field field : fields) {
            List<Annotation> fieldAnnotations = Arrays.asList(field.getAnnotations());
            if(fieldAnnotations != null && !fieldAnnotations.isEmpty()) {
                boolean injected = false;
                for(AnnotatedControllerResourceType currentResourceType : allResourceTypes) {
                    if(field.getAnnotation(currentResourceType.getSupportedAnnotation()) != null) {
                        if(injected) {
                            //TODO: Custom Exception
                            throw new RuntimeException("TODO: double injection of field");
                        }
                        PrivilegedReflection.setPrivileged(field, object, currentResourceType.getResource(field.getAnnotation(currentResourceType.getSupportedAnnotation()), field.getType(), context));
                        injected = true;
                    }
                }

                for(ControllerResourceConsumer consumer : resourceConsumers) {
                    if(field.getAnnotation(consumer.getSupportedAnnotation()) != null) {
                        consumer.consumeResource(field.getAnnotation(consumer.getSupportedAnnotation()), PrivilegedReflection.getPrivileged(field, object), context);
                    }
                }
            }
        }

    }

    private List<AnnotatedControllerResourceType> getAnnotatedControllerResourceTypes() {
        ServiceLoader<AnnotatedControllerResourceType> serviceLoader = ServiceLoader.load(AnnotatedControllerResourceType.class);

        //Check if a Annotation is defined with two resourceTypes
        Iterator<AnnotatedControllerResourceType> allResourceTypesIterator = serviceLoader.iterator();
        List<Class<Annotation>> supportedAnnotations = new ArrayList<>();
        List<AnnotatedControllerResourceType> allResourceTypes = new ArrayList<>();
        while(allResourceTypesIterator.hasNext()) {
            AnnotatedControllerResourceType currentResourceType = allResourceTypesIterator.next();
            if(supportedAnnotations.contains(currentResourceType.getSupportedAnnotation())) {
                //TODO: Custom Exception
                throw new RuntimeException("TODO: Annotation wird doppelt belegt");
            }
            supportedAnnotations.add(currentResourceType.getSupportedAnnotation());
            allResourceTypes.add(currentResourceType);
        }
        return allResourceTypes;
    }

    private List<ControllerResourceConsumer> getControllerResourceConsumer() {
        ServiceLoader<ControllerResourceConsumer> serviceLoader = ServiceLoader.load(ControllerResourceConsumer.class);
        Iterator<ControllerResourceConsumer> iterator = serviceLoader.iterator();
        List<ControllerResourceConsumer> ret = new ArrayList<>();
        while(iterator.hasNext()) {
            ret.add(iterator.next());
        }
        return ret;
    }

    public <T> T createInstanceWithInjections(Class<T> cls)
            throws InstantiationException, IllegalAccessException {
        T instance = cls.newInstance();
        injectResources(instance);
        return instance;
    }
}
