package org.datafx.controller.injection.provider;

import org.datafx.controller.context.ApplicationContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.injection.FlowScoped;
import org.datafx.controller.injection.ApplicationScoped;

public class FlowContextProvider implements ContextProvider<FlowScoped, ViewFlowContext>{

    @Override
    public Class<FlowScoped> supportedAnnotation() {
        return FlowScoped.class;
    }

    @Override
    public ViewFlowContext getContext(ViewContext viewContext) {
        return viewContext.getRegisteredObject(ViewFlowContext.class);
    }
}
