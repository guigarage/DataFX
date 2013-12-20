package org.datafx.controller.context.resource;

import org.datafx.controller.context.ApplicationContext;
import org.datafx.controller.context.FXMLApplicationContext;
import org.datafx.controller.context.ViewContext;

public class ApplicationContextResourceType implements AnnotatedControllerResourceType<FXMLApplicationContext, ApplicationContext>{

    @Override
    public ApplicationContext getResource(FXMLApplicationContext annotation, ViewContext<?> context) {
        return context.getApplicationContext();
    }

    @Override
    public Class<FXMLApplicationContext> getSupportedAnnotation() {
        return FXMLApplicationContext.class;
    }
}
