package org.datafx.controller.validation;

import java.lang.reflect.Field;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.validation.event.ValidationFinishedEvent;
import org.datafx.controller.validation.event.ValidationFinishedHandler;
import org.datafx.controller.validation.event.ValidationViolationEvent;
import org.datafx.controller.validation.event.ValidationViolationHandler;

public class ValidatorFX<U> {

    private Validator validator;
    
    private U controller;
    
    private ObjectProperty<ValidationViolationHandler<?>> onValidationViolation;
    
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
	public Set<ConstraintViolation> validateAllProperties(Class<?>... groups) {
        Set<ConstraintViolation> allViolations = FXCollections.observableSet();
        Field[] fields = controller.getClass().getDeclaredFields();
        for (final Field field : fields) {
            if (field.isAnnotationPresent(Validatable.class)) {
                Object content = ViewFactory.getPrivileged(field, controller);
                Set<ConstraintViolation<Object>> violations = validate(content, groups);
                allViolations.addAll(violations);
                for (ConstraintViolation<Object> violation : violations) {
                    if (onValidationViolation != null && onValidationViolation.getValue() != null) {
                        onValidationViolation.getValue().handle(new ValidationViolationEvent(violation));
                    }
                }
            }
        }
        if (onValidationFinished != null && onValidationFinished.getValue() != null) {
        	onValidationFinished.getValue().handle(new ValidationFinishedEvent(allViolations));
        }
        return allViolations;
    }

    private <T> Set<ConstraintViolation<T>> validate(T property, Class<?>... groups) {
        return validator.validate(property, groups);
    }
    
    public ObjectProperty<ValidationViolationHandler<?>> validationViolationHandlerProperty() {
    	if(onValidationViolation == null) {
    		onValidationViolation = new SimpleObjectProperty<>();
    	}
    	return onValidationViolation;
    }
    
    public ObjectProperty<ValidationFinishedHandler> validationFinishedHandlerProperty() {
    	if(onValidationFinished == null) {
    		onValidationFinished = new SimpleObjectProperty<>();
    	}
    	return onValidationFinished;
    }
    
    public void setOnValidationViolation(ValidationViolationHandler<?> handler) {
		validationViolationHandlerProperty().set(handler);
	}
    
    public void setOnValidationFinished(ValidationFinishedHandler handler) {
    	validationFinishedHandlerProperty().set(handler);
	}
}
