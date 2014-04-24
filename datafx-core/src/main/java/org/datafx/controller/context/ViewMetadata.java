package org.datafx.controller.context;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public class ViewMetadata {

    private StringProperty titleProperty;

    private ObjectProperty<Node> graphicProperty;

    public String getTitle() {
        return titleProperty().get();
    }

    public StringProperty titleProperty() {
        if(titleProperty == null) {
            titleProperty = new SimpleStringProperty();
        }
        return titleProperty;
    }

    public void setTitle(String title) {
        titleProperty().set(title);
    }

    public ObjectProperty<Node> graphicsProperty() {
        if(graphicProperty == null) {
            graphicProperty = new SimpleObjectProperty<>();
        }
        return graphicProperty;
    }


    public Node getGraphic() {
        return graphicsProperty().get();
    }

    public void setGraphic(Node graphic) {
        this.graphicsProperty().set(graphic);
    }
}
