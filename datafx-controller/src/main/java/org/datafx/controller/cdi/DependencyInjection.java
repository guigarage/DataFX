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
package org.datafx.controller.cdi;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.datafx.controller.FXMLController;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.util.FxmlLoadException;

public class DependencyInjection implements CDIRuntime {

    private static DependencyInjection instance;
    
    private static Map<Class, Object> models = new HashMap<>();
    private static List<Object> presenters = new ArrayList<>();

    private DependencyInjection() {
    }

    public static synchronized DependencyInjection getInstance() {
        if (instance == null) {
            instance = new DependencyInjection();
        }
        return instance;
    }

    // Order: 
    // 1. Create an instance of the Controller
    // 2. load the FXML and make sure the @FXML annotations are injected
    // 3. Resolve the @Inject points in the Controller
    // 4. Call the @PostConstruct method in the Controller
    public Region createByController(final Class<?> controllerClass) throws FxmlLoadException {
        Region answer = null;
        FXMLController controllerAnnotation = (FXMLController) controllerClass.getAnnotation(FXMLController.class);
        if (controllerAnnotation == null) {
            throw new FxmlLoadException("No FXMLController Annotation present!");
        }
        try {
            // 1. Create an instance of the Controller
            final Object object = controllerClass.newInstance();
            FXMLLoader fxmlLoader = new FXMLLoader(controllerClass.getResource(controllerAnnotation.value()));
            fxmlLoader.setController(object);
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {

                @Override public Object call(Class<?> arg0) {
                    return object;
                }
            });
            // 2. load the FXML and make sure the @FXML annotations are injected
            answer = (Region) fxmlLoader.load();
            // 3. Resolve the @Inject points in the Controller
            resolveInjects (object);
             // 4. Call the @PostConstruct method in the Controller
            initialize(object);
        } catch (Exception e) {
            throw new FxmlLoadException(e);
        }
        return answer;
    }
    

     static void resolveInjects(final Object instance) {
//        Class<? extends Object> aClass = instance.getClass();
//        Field[] fields = aClass.getDeclaredFields();
//        for (final Field field : fields) {
//            if (field.isAnnotationPresent(Inject.class)) {
//                Class<?> type = field.getType();
//                final Object target = instantiateModel(type);
//                AccessController.doPrivileged(new PrivilegedAction() {
//                    @Override
//                    public Object run() {
//                        boolean wasAccessible = field.isAccessible();
//                        try {
//                            field.setAccessible(true);
//                            field.set(instance, target);
//                            return null; // return nothing...
//                        } catch (IllegalArgumentException | IllegalAccessException ex) {
//                            throw new IllegalStateException("Cannot set field: " + field, ex);
//                        } finally {
//                            field.setAccessible(wasAccessible);
//                        }
//                    }
//                });
//            }
//        }
    }

     
    public static Object instantiateModel(Class clazz) {
        Object product = models.get(clazz);
        if (product == null) {
            try {
                product = injectAndInitialize(clazz.newInstance());
                models.put(clazz, product);
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new IllegalStateException("Cannot instantiate view: " + clazz, ex);
            }
        }
        return product;
    }

    
    static Object injectAndInitialize(Object product) {
        resolveInjects(product);
        initialize(product);
        return product;
    }
  static void initialize(Object instance) {
        invokeMethodWithAnnotation(instance, PostConstruct.class);
    }

    static void destroy(Object instance) {
        invokeMethodWithAnnotation(instance, PreDestroy.class);
    }

    static void invokeMethodWithAnnotation(final Object instance, final Class<? extends Annotation> annotationClass) throws IllegalStateException, SecurityException {
//        Class<? extends Object> aClass = instance.getClass();
//        Method[] declaredMethods = aClass.getDeclaredMethods();
//        for (final Method method : declaredMethods) {
//            if (method.isAnnotationPresent(annotationClass)) {
//                AccessController.doPrivileged(new PrivilegedAction() {
//                    @Override
//                    public Object run() {
//                        boolean wasAccessible = method.isAccessible();
//                        try {
//                            method.setAccessible(true);
//                            return method.invoke(instance, new Object[]{});
//                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//                            throw new IllegalStateException("Problem invoking " + annotationClass + " : " + method, ex);
//                        } finally {
//                            method.setAccessible(wasAccessible);
//                        }
//                    }
//                });
//            }
//        }
    }

    public void resolve(Object controller, ViewContext context) {
//     // 3. Resolve the @Inject points in the Controller
//        resolveInjects (controller);
//         // 4. Call the @PostConstruct method in the Controller
//        initialize(controller);
    }

}
