package org.datafx.controller.validation;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.validation.event.ValidationViolationEvent;
import org.datafx.controller.validation.event.ValidationViolationHandler;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Set;

public class ValidatorFX<U> {

    private Validator validator;
    private U controller;
    private ObjectProperty<ValidationViolationHandler> validationViolationHandlerProperty;

    public ValidatorFX(ViewContext<U> context) {
        this(context.getController());
    }

    public ValidatorFX(U controller) {
        this.controller = controller;
    }

    public Set<ConstraintViolation<Object>> validateAllProperties(Class<?>... groups) {
        Set<ConstraintViolation<Object>> allViolations = FXCollections.observableSet();
        Field[] fields = controller.getClass().getDeclaredFields();
        for (final Field field : fields) {
            if (field.isAnnotationPresent(Validatable.class)) {
                Object content = ViewFactory.getPrivileged(field, controller);
                Set<ConstraintViolation<Object>> violations = validate(content, groups);
                allViolations.addAll(violations);
                for (ConstraintViolation<Object> violation : violations) {
                    if (validationViolationHandlerProperty != null && validationViolationHandlerProperty.getValue() != null) {
                        validationViolationHandlerProperty.getValue().handle(new ValidationViolationEvent(violation));
                    }
                }
            }
        }
        return allViolations;
    }

    private <T> Set<ConstraintViolation<T>> validate(T property, Class<?>... groups) {
        return validator.validate(property, groups);
    }
}
