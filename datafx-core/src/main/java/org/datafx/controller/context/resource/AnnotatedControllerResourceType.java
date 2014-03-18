package org.datafx.controller.context.resource;

import org.datafx.controller.context.ViewContext;

import java.lang.annotation.Annotation;

//TODO: Rename in ControllerResourceProvider
public interface AnnotatedControllerResourceType<S extends Annotation, T> {

	T getResource(S annotation, Class<T> resourceClass, ViewContext<?> context);
	
	Class<S> getSupportedAnnotation();
}
