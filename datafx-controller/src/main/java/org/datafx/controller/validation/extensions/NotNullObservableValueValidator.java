package org.datafx.controller.validation.extensions;

import javafx.beans.value.ObservableValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

@SuppressWarnings("rawtypes")
public class NotNullObservableValueValidator implements ConstraintValidator<NotNull, ObservableValue> {

	@Override
	public void initialize(NotNull constraintAnnotation) {}

	@Override
	public boolean isValid(ObservableValue value,
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
