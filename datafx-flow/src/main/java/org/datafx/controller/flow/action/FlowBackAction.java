package org.datafx.controller.flow.action;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.util.VetoException;

public class FlowBackAction implements FlowAction {

    @Override
    public void handle(FlowHandler flowHandler, String actionId)
            throws FlowException {
        try {
            flowHandler.navigateBack();
        } catch (VetoException e) {
            throw new FlowException(e);
        }
    }

}
