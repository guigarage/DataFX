package org.datafx.controller.validation.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.datafx.util.PrivilegedReflection;
import org.datafx.controller.validation.event.ValidationFinishedEvent;
import org.datafx.controller.validation.event.ValidationFinishedHandler;

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
                Object value = PrivilegedReflection.getPrivileged(field, controller);
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
