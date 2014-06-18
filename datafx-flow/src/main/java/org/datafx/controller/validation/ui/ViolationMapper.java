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

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.validation.ConstraintViolation;
import java.util.List;

public class ViolationMapper<U> {

    private ReadOnlyStringWrapper path;

    private ObservableList<ConstraintViolation<U>> violations;

    private  ReadOnlyBooleanWrapper useSubPaths;

    public ViolationMapper(String path, boolean useSubPaths) {
        this.path = new ReadOnlyStringWrapper(path);
        this.useSubPaths = new ReadOnlyBooleanWrapper(useSubPaths);
        violations = FXCollections.observableArrayList();
    }

    public ObservableList<ConstraintViolation<U>> getViolations() {
        return violations;
    }

    public String path() {
        return pathProperty().get();
    }

    public ReadOnlyStringProperty pathProperty() {
        return path.getReadOnlyProperty();
    }

    public boolean getUseSubPaths() {
        return useSubPathsProperty().get();
    }

    public ReadOnlyBooleanProperty useSubPathsProperty() {
        return useSubPaths.getReadOnlyProperty();
    }

    public void reset() {
        violations.clear();
    }

    public void setViolations(List<ConstraintViolation<U>> matchingViolations) {
        violations.setAll(matchingViolations);
    }
}
