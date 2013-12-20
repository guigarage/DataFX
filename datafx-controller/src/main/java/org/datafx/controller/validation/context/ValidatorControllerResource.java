package org.datafx.controller.validation.context;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.AnnotatedControllerResourceType;
import org.datafx.controller.validation.ValidatorFX;

@SuppressWarnings("rawtypes")
public class ValidatorControllerResource implements AnnotatedControllerResourceType<Validator, ValidatorFX> {

	@SuppressWarnings("unchecked")
	@Override
	public ValidatorFX getResource(Validator annotation, Class<ValidatorFX> cls, ViewContext<?> context) {
        ValidatorFX validator =  new ValidatorFX(context);
		return validator;
	}

	@Override
	public Class<Validator> getSupportedAnnotation() {
		return Validator.class;
	}

}
