package org.datafx.controller.validation.extensions;

import javafx.beans.value.ObservableValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Null;

@SuppressWarnings("rawtypes")
public class NullObservableValueValidator implements ConstraintValidator<Null, ObservableValue> {

	@Override
	public void initialize(Null constraintAnnotation) {}

	@Override
	public boolean isValid(ObservableValue value,
			ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		if(value.getValue() == null) {
			return true;
		}
		return false;
	}

	
}
