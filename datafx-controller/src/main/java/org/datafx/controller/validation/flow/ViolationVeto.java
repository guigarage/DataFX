package org.datafx.controller.validation.flow;

import org.datafx.controller.util.Veto;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ViolationVeto extends Veto {

    Set<ConstraintViolation<Object>> violations;

    public ViolationVeto(Set<ConstraintViolation<Object>> violations) {
        this.violations = violations;
    }

    public Set<ConstraintViolation<Object>> getViolations() {
        return violations;
    }
}
