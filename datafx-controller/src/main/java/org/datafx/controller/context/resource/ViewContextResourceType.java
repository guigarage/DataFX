package org.datafx.controller.context.resource;

import org.datafx.controller.context.FXMLViewContext;
import org.datafx.controller.context.ViewContext;

public class ViewContextResourceType implements AnnotatedControllerResourceType<FXMLViewContext, ViewContext>{

    @Override
    public ViewContext getResource(FXMLViewContext annotation, Class<ViewContext> cls, ViewContext<?> context) {
        return context;
    }

    @Override
    public Class<FXMLViewContext> getSupportedAnnotation() {
        return FXMLViewContext.class;
    }
}
