package org.datafx.controller.validation.extensions;

import javafx.beans.value.ObservableValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

public class NotNullObservableValueValidator implements ConstraintValidator<NotNull, ObservableValue<Object>> {

	@Override
	public void initialize(NotNull constraintAnnotation) {}

	@Override
	public boolean isValid(ObservableValue<Object> value,
			ConstraintValidatorContext context) {
		if(value == null) {
			return false;
		}
		if(value.getValue() == null) {
			return false;
		}
		return true;
	}

}
