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
