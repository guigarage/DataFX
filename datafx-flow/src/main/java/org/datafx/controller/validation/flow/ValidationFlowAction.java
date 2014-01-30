package org.datafx.controller.validation.flow;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.action.FlowAction;
import org.datafx.controller.validation.ValidatorFX;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ValidationFlowAction<U> implements FlowAction {

    private Class<?>[] groups;

    public ValidationFlowAction(Class<?>... groups) {
        this.groups = groups;
    }

    @Override
    public void handle(FlowHandler flowHandler, String actionId) throws FlowException {
        ValidatorFX<U> validator = flowHandler.getCurrentViewContext().getRegisteredObject(ValidatorFX.class);
        if(validator == null) {
            validator =  new ValidatorFX<U>((ViewContext<U>) flowHandler.getCurrentViewContext());
            flowHandler.getCurrentViewContext().register(validator);
        }
        Set<ConstraintViolation<U>> violations = validator.validateAllProperties(groups);
        if(violations != null && !violations.isEmpty()) {
            throw new FlowException("Validation violation!");
        }
    }
}
