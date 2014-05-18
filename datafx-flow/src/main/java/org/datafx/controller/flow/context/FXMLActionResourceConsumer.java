package org.datafx.controller.flow.context;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.ControllerResourceConsumer;
import org.datafx.controller.flow.action.ActionTrigger;

public class FXMLActionResourceConsumer implements ControllerResourceConsumer<ActionTrigger, Object>  {

    @Override
    public void consumeResource(ActionTrigger annotation, Object resource, ViewContext<?> context) {
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
    public Class<ActionTrigger> getSupportedAnnotation() {
        return ActionTrigger.class;
    }
}
