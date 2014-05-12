package org.datafx.controller.flow.context;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.ControllerResourceConsumer;
import org.datafx.controller.flow.action.LinkAction;

public class LinkActionResourceConsumer implements ControllerResourceConsumer<LinkAction, Object> {

    @Override
    public void consumeResource(LinkAction annotation, Object resource, ViewContext<?> context) {
        FlowActionHandler actionHandler = context.getRegisteredObject(ViewFlowContext.class).getRegisteredObject(FlowActionHandler.class);
        if (resource != null) {
            if (resource instanceof MenuItem) {
                actionHandler.attachLinkEventHandler((MenuItem) resource, annotation.value());
            } else if (resource instanceof Node) {
                actionHandler.attachLinkEventHandler((Node) resource, annotation.value());
            }
        }
    }

    @Override
    public Class<LinkAction> getSupportedAnnotation() {
        return LinkAction.class;
    }
}

