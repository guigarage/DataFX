package org.datafx.controller.flow.context;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.ControllerResourceConsumer;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.action.FXMLFlowAction;
import org.datafx.controller.util.VetoException;

public class FXMLActionResourceConsumer implements ControllerResourceConsumer<FXMLFlowAction, Object>  {

    @Override
    public void consumeResource(FXMLFlowAction annotation, Object resource, ViewContext<?> context) {
        FlowActionHandler actionHandler = context.getRegisteredObject(ViewFlowContext.class).getRegisteredObject(FlowActionHandler.class);
        if (resource != null) {
            if(resource instanceof MenuItem) {
                actionHandler.attachEventHandler((MenuItem) resource, annotation.value());
            } else if(resource instanceof Node){
                actionHandler.attachEventHandler((Node) resource, annotation.value());
            }
        }
    }

    @Override
    public Class<FXMLFlowAction> getSupportedAnnotation() {
        return FXMLFlowAction.class;
    }
}
