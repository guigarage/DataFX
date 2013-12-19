package org.datafx.controller.validation.flow;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.event.BeforeFlowActionEvent;
import org.datafx.controller.flow.event.VetoableBeforeFlowActionHandler;
import org.datafx.controller.validation.ValidatorFX;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class DefaultValidationListener<U> implements VetoableBeforeFlowActionHandler {

    private ValidatorFX<U> validator;
    private Class<?>[] groups;

    public DefaultValidationListener(ViewContext<U> context, Class<?>... groups) {
        this(new ValidatorFX<>(context), groups);
    }

    public DefaultValidationListener(ValidatorFX<U> validator, Class<?>... groups) {
        this.validator = validator;
        this.groups = groups;
    }

    public ValidatorFX<U> getValidator() {
        return validator;
    }

    @Override
    public void handle(BeforeFlowActionEvent event) throws ViolationVeto {
        if (validateOnAction(event)) {
            Set<ConstraintViolation<Object>> violations = validator.validateAllProperties(groups);
            if (violations != null && !violations.isEmpty()) {
                if (vetoOnViolations(event, violations)) {
                    throw new ViolationVeto(violations);
                }
            }
        }
    }

    protected boolean validateOnAction(BeforeFlowActionEvent event) {
        return true;
    }

    protected boolean vetoOnViolations(BeforeFlowActionEvent event, Set<ConstraintViolation<Object>> violations) {
        return true;
    }
}
