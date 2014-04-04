package org.datafx.controller.flow.context;

import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.ControllerResourceConsumer;
import org.datafx.controller.flow.action.FXMLFlowAction;

public class FXMLActionResourceConsumer implements ControllerResourceConsumer<FXMLFlowAction, Object>  {

    @Override
    public void consumeResource(FXMLFlowAction annotation, Object resource, ViewContext<?> context) {
        FlowActionHandler actionHandler = context.getRegisteredObject(ViewFlowContext.class).getRegisteredObject(FlowActionHandler.class);
        if (resource != null) {
            if (resource instanceof ButtonBase) {
                ((ButtonBase) resource).setOnAction((e) -> actionHandler.handle(annotation.value()));
            } else if(resource instanceof MenuItem) {
                ((MenuItem) resource).setOnAction((e) -> actionHandler.handle(annotation.value()));
            } else if(resource instanceof Node){
                ((Node) resource).setOnMouseClicked((e) -> {
                    if(e.getClickCount() > 1) {
                        actionHandler.handle(annotation.value());
                    }
                });
            }
        }
    }

    @Override
    public Class<FXMLFlowAction> getSupportedAnnotation() {
        return FXMLFlowAction.class;
    }
}
