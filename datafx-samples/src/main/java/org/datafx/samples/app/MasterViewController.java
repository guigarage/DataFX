package org.datafx.samples.app;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.FXMLFlowAction;
import org.datafx.controller.flow.context.ActionHandler;
import org.datafx.controller.flow.context.FlowActionHandler;
import org.datafx.ejb.RemoteCalculator;
import org.datafx.ejb.RemoteEjb;
import org.datafx.samples.ejb.wildfly.DemoConfigurationProvider;

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

    @ActionHandler
    private FlowActionHandler actionHandler;

    @Inject
    private DataModel model;

    @RemoteEjb(DemoConfigurationProvider.NAME)
    private RemoteCalculator calculator;


    @PostConstruct
    public void init() {
        dataList.itemsProperty().bind(model.getPersons());
        model.selectedPersonIndexProperty().bind(dataList.getSelectionModel().selectedIndexProperty());

        dataList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.print(calculator.add(3,3));
                if(event.getClickCount() >= 2) {
                        actionHandler.handle("edit");
                }
            }
        });
    }
}
