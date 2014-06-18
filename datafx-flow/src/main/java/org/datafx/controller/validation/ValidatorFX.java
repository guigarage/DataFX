/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.controller.validation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.validation.event.ValidationFinishedEvent;
import org.datafx.controller.validation.event.ValidationFinishedHandler;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


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
