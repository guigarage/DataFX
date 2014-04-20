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

    public String getTitle() {
        return titleProperty().get();
    }

    public StringProperty titleProperty() {
        if(titleProperty == null) {
            titleProperty = new SimpleStringProperty();
        }
        return titleProperty;
    }

    public void setTitle(String titleProperty) {
        titleProperty().set(titleProperty);
    }

    public Callback<Dimension2D, Node> getGraphicsFactory() {
        return graphicsFactoryProperty().get();
    }

    public ObjectProperty<Callback<Dimension2D, Node>> graphicsFactoryProperty() {
        if(graphicsFactoryProperty == null) {
            graphicsFactoryProperty = new SimpleObjectProperty<>((d) -> null);
        }
        return graphicsFactoryProperty;
    }

    public void setGraphicsFactory(Callback<Dimension2D, Node> graphicsFactoryProperty) {
        this.graphicsFactoryProperty().set(graphicsFactoryProperty);
    }
}
