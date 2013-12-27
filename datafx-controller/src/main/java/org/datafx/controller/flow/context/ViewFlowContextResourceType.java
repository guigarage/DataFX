package org.datafx.controller.flow.context;

import org.datafx.controller.context.*;
import org.datafx.controller.context.resource.AnnotatedControllerResourceType;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

public class ViewFlowContextResourceType implements AnnotatedControllerResourceType<FXMLViewFlowContext, ViewFlowContext> {

    @Override
    public ViewFlowContext getResource(FXMLViewFlowContext annotation, Class<ViewFlowContext> cls, ViewContext<?> context) {
        return context.getRegisteredObject(ViewFlowContext.class);
    }

    @Override
    public Class<FXMLViewFlowContext> getSupportedAnnotation() {
        return FXMLViewFlowContext.class;
    }
}
