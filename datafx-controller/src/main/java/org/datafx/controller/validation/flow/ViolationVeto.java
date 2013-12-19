package org.datafx.controller.validation.flow;

import org.datafx.controller.util.Veto;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ViolationVeto extends Veto {

	private static final long serialVersionUID = 1L;

	Set<ConstraintViolation<Object>> violations;

    public ViolationVeto(Set<ConstraintViolation<Object>> violations) {
        this.violations = violations;
    }

    public Set<ConstraintViolation<Object>> getViolations() {
        return violations;
    }
}
