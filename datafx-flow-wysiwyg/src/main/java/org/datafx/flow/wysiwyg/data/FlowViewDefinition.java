package org.datafx.flow.wysiwyg.data;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

public class FlowViewDefinition {

    private StringProperty controllerClassName;

    private StringProperty fxmlFile;

    private ObservableList<FlowActionDefinition> actions;

    private ObjectProperty<Point2D> position;

    public FlowViewDefinition() {
        this(null);
    }

    public FlowViewDefinition(String controllerClassName) {
        actions = FXCollections.observableArrayList();
        position = new SimpleObjectProperty<>(new Point2D(0,0));
        this.controllerClassName = new SimpleStringProperty(controllerClassName);
        fxmlFile = new SimpleStringProperty();
    }

    public ObservableList<FlowActionDefinition> getActions() {
        return actions;
    }

    public void setActions(ObservableList<FlowActionDefinition> actions) {
        this.actions = actions;
    }

    public String getControllerClassName() {
        return controllerClassName.get();
    }

    public StringProperty controllerClassNameProperty() {
        return controllerClassName;
    }

    public void setControllerClassName(String controllerClassName) {
        this.controllerClassName.set(controllerClassName);
    }

    public String getFxmlFile() {
        return fxmlFile.get();
    }

    public StringProperty fxmlFileProperty() {
        return fxmlFile;
    }

    public void setFxmlFile(String fxmlFile) {
        this.fxmlFile.set(fxmlFile);
    }

    public Point2D getPosition() {
        return position.get();
    }

    public ObjectProperty<Point2D> positionProperty() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position.set(position);
    }
}
