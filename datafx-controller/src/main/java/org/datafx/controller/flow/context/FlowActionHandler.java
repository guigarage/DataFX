package org.datafx.controller.flow.context;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.util.VetoException;

public class FlowActionHandler {

    private FlowHandler handler;

    public FlowActionHandler(FlowHandler handler) {
                 this.handler = handler;
    }

    public void handle(String actionId) throws FlowException, VetoException {
        this.handler.handle(actionId);
    }
}
