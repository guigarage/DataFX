package org.datafx.controller.context;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.util.Callback;

public class ViewMetadata {

    private StringProperty titleProperty;

    private ObjectProperty<Callback<Dimension2D, Node>> graphicsFactoryProperty;

    public String getTitleProperty() {
        return titlePropertyProperty().get();
    }

    public StringProperty titlePropertyProperty() {
        if(titleProperty == null) {
            titleProperty = new SimpleStringProperty();
        }
        return titleProperty;
    }

    public void setTitleProperty(String titleProperty) {
        titlePropertyProperty().set(titleProperty);
    }

    public Callback<Dimension2D, Node> getGraphicsFactoryProperty() {
        return graphicsFactoryPropertyProperty().get();
    }

    public ObjectProperty<Callback<Dimension2D, Node>> graphicsFactoryPropertyProperty() {
        if(graphicsFactoryProperty == null) {
            graphicsFactoryProperty = new SimpleObjectProperty<>((d) -> null);
        }
        return graphicsFactoryProperty;
    }

    public void setGraphicsFactoryProperty(Callback<Dimension2D, Node> graphicsFactoryProperty) {
        this.graphicsFactoryPropertyProperty().set(graphicsFactoryProperty);
    }
}
