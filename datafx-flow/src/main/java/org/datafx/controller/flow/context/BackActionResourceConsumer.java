package org.datafx.controller.flow.context;

import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.ControllerResourceConsumer;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.action.BackAction;
import org.datafx.controller.util.VetoException;

public class BackActionResourceConsumer implements ControllerResourceConsumer<BackAction, Object> {

    @Override
    public void consumeResource(BackAction annotation, Object resource, ViewContext<?> context) {
        FlowActionHandler actionHandler = context.getRegisteredObject(ViewFlowContext.class).getRegisteredObject(FlowActionHandler.class);
        if (resource != null) {
            if (resource instanceof ButtonBase) {
                ((ButtonBase) resource).setOnAction((e) -> handleBackAction(actionHandler));
            } else if(resource instanceof MenuItem) {
                ((MenuItem) resource).setOnAction((e) -> handleBackAction(actionHandler));
            } else if(resource instanceof Node){
                ((Node) resource).setOnMouseClicked((e) -> {
                    if (e.getClickCount() > 1) {
                        handleBackAction(actionHandler);
                    }
                });
            }
        }
    }

    private void handleBackAction(FlowActionHandler actionHandler) {
        try {
            actionHandler.navigateBack();
        } catch (VetoException | FlowException e) {
            actionHandler.getExceptionHandler().setException(e);
        }
    }

    @Override
    public Class<BackAction> getSupportedAnnotation() {
        return BackAction.class;
    }
}
