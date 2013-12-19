package org.datafx.controller.validation.flow;

import org.datafx.controller.util.Veto;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ViolationVeto extends Veto {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	Set<ConstraintViolation> violations;

    @SuppressWarnings("rawtypes")
	public ViolationVeto(Set<ConstraintViolation> violations) {
        this.violations = violations;
    }

    @SuppressWarnings("rawtypes")
	public Set<ConstraintViolation> getViolations() {
        return violations;
    }
}
