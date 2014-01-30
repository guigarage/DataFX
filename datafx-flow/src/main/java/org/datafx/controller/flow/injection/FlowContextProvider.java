package org.datafx.controller.flow.injection;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.injection.provider.ContextProvider;

public class FlowContextProvider implements ContextProvider<FlowScoped, ViewFlowContext> {

    @Override
    public Class<FlowScoped> supportedAnnotation() {
        return FlowScoped.class;
    }

    @Override
    public ViewFlowContext getContext(ViewContext viewContext) {
        return viewContext.getRegisteredObject(ViewFlowContext.class);
    }
}
