package org.datafx.controller.flow;

import javafx.scene.Node;

public class ViewHistoryDefinition<T> {

    private Class<T> controllerClass;

    private String viewTitle;

    private Node viewGraphics;

    public ViewHistoryDefinition(Class<T> controllerClass, String viewTitle, Node viewGraphics) {
        this.controllerClass = controllerClass;
        this.viewTitle = viewTitle;
        this.viewGraphics = viewGraphics;
    }

    public Class<T> getControllerClass() {
        return controllerClass;
    }

    public String getViewTitle() {
        return viewTitle;
    }

    public Node getViewGraphics() {
        return viewGraphics;
    }
}
