package org.datafx.controller.flow.context;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.ControllerResourceConsumer;
import org.datafx.controller.flow.action.FXMLFlowAction;

public class FXMLActionResourceConsumer implements ControllerResourceConsumer<FXMLFlowAction, Node>  {

    @Override
    public void consumeResource(FXMLFlowAction annotation, Node resource, ViewContext<?> context) {
        FlowActionHandler actionHandler = context.getRegisteredObject(ViewFlowContext.class).getRegisteredObject(FlowActionHandler.class);
        if (resource != null) {
            if (resource instanceof Button) {
                ((Button) resource)
                        .setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
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
