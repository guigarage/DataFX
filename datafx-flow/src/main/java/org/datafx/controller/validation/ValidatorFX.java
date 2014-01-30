package org.datafx.controller.validation;

import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.validation.event.ValidationFinishedEvent;
import org.datafx.controller.validation.event.ValidationFinishedHandler;

public class ValidatorFX<U> {

    private Validator validator;
    
    private U controller;

    private ObjectProperty<ValidationFinishedHandler> onValidationFinished;

    public ValidatorFX(ViewContext<U> context) {
        this(context.getController());
    }

    public ValidatorFX(U controller) {
        this.controller = controller;
        Configuration<?> validationConf = Validation.byDefaultProvider().configure();
        validator = validationConf.buildValidatorFactory().getValidator();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<ConstraintViolation<U>> validateAllProperties(Class<?>... groups) {
        Set<ConstraintViolation<U>> violations = validator.validate(controller, groups);
        if(onValidationFinished != null && onValidationFinished.get() != null) {
            onValidationFinished.get().handle(new ValidationFinishedEvent<>(violations));
        }
        return violations;
    }


    public ObjectProperty<ValidationFinishedHandler> validationFinishedHandlerProperty() {
    	if(onValidationFinished == null) {
    		onValidationFinished = new SimpleObjectProperty<>();
    	}
    	return onValidationFinished;
    }

    public void setOnValidationFinished(ValidationFinishedHandler handler) {
    	validationFinishedHandlerProperty().set(handler);
	}
}
