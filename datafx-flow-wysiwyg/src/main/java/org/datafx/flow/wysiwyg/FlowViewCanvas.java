package org.datafx.flow.wysiwyg;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.datafx.flow.wysiwyg.data.FlowDefinition;
import org.datafx.flow.wysiwyg.data.FlowViewDefinition;
import org.datafx.flow.wysiwyg.netview.NetView;

public class FlowViewCanvas extends NetView<FlowViewDefinition> {

    private ObjectProperty<FlowDefinition> flowDefinition;

    public FlowViewCanvas() {
        flowDefinition = new SimpleObjectProperty<>();
        flowDefinition.addListener((observable, oldValue, newValue) -> setItems(newValue.getViews()));
        setCellFactory((e) -> new FlowViewCell());
    }

    public FlowDefinition getFlowDefinition() {
        return flowDefinition.get();
    }

    public ObjectProperty<FlowDefinition> flowDefinitionProperty() {
        return flowDefinition;
    }

    public void setFlowDefinition(FlowDefinition flowDefinition) {
        this.flowDefinition.set(flowDefinition);
    }

}
