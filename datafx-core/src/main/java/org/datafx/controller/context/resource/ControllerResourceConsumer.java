package org.datafx.controller.context.resource;

import org.datafx.controller.context.ViewContext;

import java.lang.annotation.Annotation;

public interface ControllerResourceConsumer<S extends Annotation, T> {

    void consumeResource(S annotation, T resource, ViewContext<?> context);

    Class<S> getSupportedAnnotation();
}
