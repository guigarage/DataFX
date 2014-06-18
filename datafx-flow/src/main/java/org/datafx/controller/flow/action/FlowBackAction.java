package org.datafx.controller.flow.action;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.util.VetoException;

/**
 * Defines a {@link FlowAction} that navigates back to the last view of a flow. You should know this behavior from a back
 * button in a web browser for example.
 */
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
