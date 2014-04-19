package org.datafx.controller.flow.context;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.ControllerResourceConsumer;
import org.datafx.controller.flow.action.BackAction;

public class BackActionResourceConsumer implements ControllerResourceConsumer<BackAction, Object> {

    @Override
    public void consumeResource(BackAction annotation, Object resource, ViewContext<?> context) {
        FlowActionHandler actionHandler = context.getRegisteredObject(ViewFlowContext.class).getRegisteredObject(FlowActionHandler.class);
        if (resource != null) {
            if (resource != null) {
                if(resource instanceof MenuItem) {
                    actionHandler.attachBackEventHandler((MenuItem) resource);
                } else if(resource instanceof Node){
                    actionHandler.attachBackEventHandler((Node) resource);
                }
            }
        }
    }

    @Override
    public Class<BackAction> getSupportedAnnotation() {
        return BackAction.class;
    }
}
