package org.datafx.samples.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.FXMLFlowAction;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@FXMLController("listView.fxml")
public class MasterViewController {

    @FXML
    @FXMLFlowAction("edit")
    private Button editButton;

    @FXML
    @FXMLFlowAction("remove")
    private Button removeButton;

    @FXML
    @FXMLFlowAction("add")
    private Button addButton;

    @FXML
    @FXMLFlowAction("load")
    private Button loadButton;

    @FXML
    private ListView dataList;

    @Inject
    private DataModel model;

    @PostConstruct
    public void init() {
        dataList.itemsProperty().bind(model.getPersons());
        model.selectedPersonIndexProperty().bind(dataList.getSelectionModel().selectedIndexProperty());
    }
}
