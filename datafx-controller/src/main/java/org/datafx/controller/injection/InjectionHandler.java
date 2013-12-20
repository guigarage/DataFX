package org.datafx.controller.injection;

import javassist.Modifier;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.datafx.controller.context.AbstractContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.injection.FlowScoped;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.datafx.controller.util.PrivilegedReflection.setPrivileged;

public class InjectionHandler<U> {

    private final static DependendContext DEPENDEND_CONTEXT = new DependendContext();

    private ViewContext<U> viewContext;

    public InjectionHandler(ViewContext<U> viewContext) {
        this.viewContext = viewContext;
    }

    private <T> void registerNewInstance(final Class<T> propertyClass, final AbstractContext context) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        T instance = propertyClass.newInstance();
        context.register(instance);
        injectAllSupportedFields(instance);
    }

    public <T> T createProxy(final Class<T> propertyClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final AbstractContext context = getContextForClass(propertyClass);
        if (context.getRegisteredObject(propertyClass) == null) {
            registerNewInstance(propertyClass, context);
        }

        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(propertyClass);
        factory.setFilter(
                new MethodFilter() {
                    @Override
                    public boolean isHandled(Method method) {
                        return true;
                    }
                }
        );

        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                T innerObject =       context.getRegisteredObject(propertyClass);
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
                setPrivileged(field, bean, createProxy(field.getType()));
            }
        }
    }

    private AbstractContext getContextForClass(Class<?> cls) {
        if (cls.isAnnotationPresent(ViewScoped.class)) {
            return viewContext;
        } else if (cls.isAnnotationPresent(FlowScoped.class)) {
            return viewContext.getRegisteredObject(ViewFlowContext.class);
        } else if (cls.isAnnotationPresent(ApplicationScoped.class)) {
            return viewContext.getApplicationContext();
        } else if (cls.isAnnotationPresent(Singleton.class)) {
            return viewContext.getApplicationContext();
        }
        return DEPENDEND_CONTEXT;
    }

}
