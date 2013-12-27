package org.datafx.controller.injection.provider;

import org.datafx.controller.context.AbstractContext;
import org.datafx.controller.context.ViewContext;

import java.lang.annotation.Annotation;

public interface ContextProvider<T extends Annotation, U extends AbstractContext> {

    Class<T> supportedAnnotation();

    U getContext(ViewContext viewContext);
}
