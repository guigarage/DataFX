package org.datafx.flow.wysiwyg.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FlowDefinition {

    private ObservableList<FlowViewDefinition> views;

    private ObservableList<FlowActionDefinition> actions;

    public FlowDefinition() {
        views = FXCollections.observableArrayList();
        actions = FXCollections.observableArrayList();
    }

    public ObservableList<FlowActionDefinition> getActions() {
        return actions;
    }

    public void setActions(ObservableList<FlowActionDefinition> actions) {
        this.actions = actions;
    }

    public ObservableList<FlowViewDefinition> getViews() {
        return views;
    }

    public void setViews(ObservableList<FlowViewDefinition> views) {
        this.views = views;
    }
}
