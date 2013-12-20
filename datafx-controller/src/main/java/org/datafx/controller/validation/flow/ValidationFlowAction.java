package org.datafx.controller.validation.flow;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.action.FlowAction;
import org.datafx.controller.validation.ValidatorFX;

public class ValidationFlowAction implements FlowAction {

    private Class<?>[] groups;

    public ValidationFlowAction(Class<?>... groups) {
        this.groups = groups;
    }

    @Override
    public void handle(FlowHandler flowHandler, String actionId) throws FlowException {
        new ValidatorFX(flowHandler.getCurrentViewContext()).validateAllProperties(groups);
    }
}
