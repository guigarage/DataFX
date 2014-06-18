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
package org.datafx.controller.validation.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.datafx.controller.validation.event.ValidationFinishedEvent;
import org.datafx.controller.validation.event.ValidationFinishedHandler;
import org.datafx.util.DataFXUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ViolationBindingManager<U> implements ValidationFinishedHandler<U> {

    ObservableList<ViolationMapper> bindings;

    public ViolationBindingManager(U controller)   {
        bindings = FXCollections.observableArrayList();
        Field[] fields = controller.getClass().getDeclaredFields();
        for (final Field field : fields) {
            if (field.isAnnotationPresent(ViolationBinding.class)) {
                ViolationBinding violationBinding = field.getAnnotation(ViolationBinding.class);
                Object value = DataFXUtils.getPrivileged(field, controller);
                ViolationMapper mapper = createMapper(value, violationBinding.path(), violationBinding.useSubPaths());
                if(mapper != null) {
                    bindings.add(mapper);
                }
            }
        }
    }

    private ViolationMapper createMapper(Object value, String path, boolean useSubPaths) {
        if(value != null) {
            //TODO: PlugIn API
            if(value instanceof Node) {
               return new ViolationMapper(path, useSubPaths);
            }
            //TODO: Custom Exception
            throw new RuntimeException("Unknown Type");
        }
        throw new NullPointerException("value == null");
    }

    public void update(Set<ConstraintViolation<U>> violations) {
        for(ViolationMapper binding : bindings) {
            List<ConstraintViolation<U>> matchingViolations = new ArrayList<>();
            for(ConstraintViolation<U> violation : violations) {
                if(isMatching(violation.getPropertyPath(), binding.path(), binding.getUseSubPaths())) {
                    matchingViolations.add(violation);
                }
            }
            binding.setViolations(matchingViolations);
        }
    }

    private boolean isMatching(Path path, String pathAsString, boolean useSubpath) {
        //TODO: Implement
        return true;
    }

    @Override
    public void handle(ValidationFinishedEvent<U> event) {
        update(event.getViolations());
    }
}
