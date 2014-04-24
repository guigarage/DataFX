package org.datafx.controller.injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javax.inject.Inject;
import org.datafx.controller.context.AbstractContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.injection.provider.ContextProvider;
import org.datafx.util.DataFXUtils;


public class InjectionHandler<U> {

    private final static DependendContext DEPENDEND_CONTEXT = new DependendContext();
    private ViewContext<U> viewContext;

    public InjectionHandler(ViewContext<U> viewContext) {
        this.viewContext = viewContext;
    }

    private <T> T registerNewInstance(final Class<T> propertyClass, final AbstractContext context) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        for (Constructor<?> constructor : propertyClass.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                T instance = propertyClass.newInstance();
                context.register(instance);
                injectAllSupportedFields(instance);
                return instance;
            }
        }
        //TODO: Special Exception
        throw new RuntimeException("No default constructor present!");
    }

    public <T> T createProxy(final Class<T> propertyClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final AbstractContext context = getContextForClass(propertyClass);
        T registeredObject = context.getRegisteredObject(propertyClass);
        if (registeredObject == null) {
            registeredObject = registerNewInstance(propertyClass, context);
        }

        final T innerObject = registeredObject;

        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(propertyClass);


        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                AbstractContext context = getContextForClass(propertyClass);
                if (context.getRegisteredObject(propertyClass) == null) {
                    registerNewInstance(propertyClass, context);
                }
                return thisMethod.invoke(innerObject, args);
            }
        };

        return (T) factory.create(new Class<?>[0], new Object[0], handler);
    }

    private <T> void injectAllSupportedFields(T bean) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<T> cls = (Class<T>) bean.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (final Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                DataFXUtils.setPrivileged(field, bean, createProxy(field.getType()));
            }
        }
    }

    private AbstractContext getContextForClass(Class<?> cls) {
        ServiceLoader<ContextProvider> contextProvidersLoader = ServiceLoader.load(ContextProvider.class);
        Iterator<ContextProvider> iterator = contextProvidersLoader.iterator();

        while (iterator.hasNext()) {
            ContextProvider provider = iterator.next();
            if (cls.isAnnotationPresent(provider.supportedAnnotation())) {
                return provider.getContext(viewContext);
            }
        }

        return DEPENDEND_CONTEXT;
    }

}
