package org.datafx.controller.flow.context;

import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
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
            if (resource instanceof ButtonBase) {
                ((ButtonBase) resource).setOnAction((e) -> handleAction(actionHandler, annotation.value()));
            } else if(resource instanceof MenuItem) {
                ((MenuItem) resource).setOnAction((e) -> handleAction(actionHandler, annotation.value()));
            } else if(resource instanceof Node){
                ((Node) resource).setOnMouseClicked((e) -> {
                    if (e.getClickCount() > 1) {
                        handleAction(actionHandler, annotation.value());
                    }
                });
            }
        }
    }

    private void handleAction(FlowActionHandler actionHandler, String id) {
        try {
            actionHandler.handle(id);
        } catch (VetoException | FlowException e) {
            actionHandler.getExceptionHandler().setException(e);
        }
    }

    @Override
    public Class<FXMLFlowAction> getSupportedAnnotation() {
        return FXMLFlowAction.class;
    }
}
