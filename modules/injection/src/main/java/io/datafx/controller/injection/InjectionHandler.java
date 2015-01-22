/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
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
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.controller.injection;

import io.datafx.controller.context.AbstractContext;
import io.datafx.controller.context.ViewContext;
import io.datafx.controller.injection.provider.ContextProvider;
import io.datafx.core.DataFXUtils;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;


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
        for (final Field field : DataFXUtils.getInheritedDeclaredFields(cls)) {
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
