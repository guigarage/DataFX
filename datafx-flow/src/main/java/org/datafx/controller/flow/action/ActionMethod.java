package org.datafx.controller.flow.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that adds a method action to the annotated node. The action will call a method that is defined in the same
 * controller class as the node field. The {@link #value()} field defines the method name that will be called.
 * The node value must be injected from fxml. Therefore the ActionMethod should be always used in combination with the
 * {@link javafx.fxml.FXML} annotation.
 * If a node that extends {@link javafx.scene.control.ButtonBase} or {@link javafx.scene.control.MenuItem} then the back
 * action will be triggered whenever the node fires an action event. Otherwise the action will be triggered when the
 * node is clicked.
 */
@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionMethod {

    public abstract String value();
}
