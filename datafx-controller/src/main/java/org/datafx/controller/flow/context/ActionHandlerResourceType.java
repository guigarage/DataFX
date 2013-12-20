package org.datafx.controller.flow.context;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.AnnotatedControllerResourceType;

public class ActionHandlerResourceType implements AnnotatedControllerResourceType<ActionHandler, FlowActionHandler> {

    @Override
    public FlowActionHandler getResource(ActionHandler annotation,  Class<FlowActionHandler> cls, ViewContext<?> context) {
        return context.getRegisteredObject(ViewFlowContext.class).getRegisteredObject(FlowActionHandler.class);
    }

    @Override
    public Class<ActionHandler> getSupportedAnnotation() {
        return ActionHandler.class;
    }
}
