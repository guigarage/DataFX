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
package org.datafx.controller.context;

import org.datafx.controller.context.resource.AnnotatedControllerResourceType;
import org.datafx.controller.context.resource.ControllerResourceConsumer;
import org.datafx.util.DataFXUtils;

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
        for (final Field field : DataFXUtils.getInheritedPrivateFields(cls)) {
            List<Annotation> fieldAnnotations = Arrays.asList(field.getAnnotations());
            if(fieldAnnotations != null && !fieldAnnotations.isEmpty()) {
                boolean injected = false;
                for(AnnotatedControllerResourceType currentResourceType : allResourceTypes) {
                    if(field.getAnnotation(currentResourceType.getSupportedAnnotation()) != null) {
                        if(injected) {
                            //TODO: Custom Exception
                            throw new RuntimeException("TODO: double injection of field");
                        }
                        DataFXUtils.setPrivileged(field, object, currentResourceType.getResource(field.getAnnotation(currentResourceType.getSupportedAnnotation()), field.getType(), context));
                        injected = true;
                    }
                }

                for(ControllerResourceConsumer consumer : resourceConsumers) {
                    if(field.getAnnotation(consumer.getSupportedAnnotation()) != null) {
                        consumer.consumeResource(field.getAnnotation(consumer.getSupportedAnnotation()), DataFXUtils.getPrivileged(field, object), context);
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
