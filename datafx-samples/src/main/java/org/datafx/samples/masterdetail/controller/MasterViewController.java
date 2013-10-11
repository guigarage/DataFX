package org.datafx.samples.masterdetail.controller;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.datafx.controller.context.FXMLViewFlowContext;
import org.datafx.controller.context.ViewFlowContext;
import org.datafx.controller.flow.action.FXMLFlowAction;
import org.datafx.samples.masterdetail.data.DataModel;


public class MasterViewController {

	@FXMLViewFlowContext
    private ViewFlowContext context;
    
    @FXML
    private ListView<StringProperty> myList;
    
    @FXML
    @FXMLFlowAction("delete")
    private Button deleteButton;
    
    @FXML
    @FXMLFlowAction("showDetails")
    private Button showDetailsButton;
    
    @PostConstruct
    public void init() {
    	DataModel model = context.getRegisteredObject(DataModel.class);
        myList.setItems(model.getNames());
        myList.getSelectionModel().select(model.selectedIndex().get());
        model.selectedIndex().bind(myList.getSelectionModel().selectedIndexProperty());
    }
    
    @PreDestroy
    public void destroy() {
    	DataModel model = context.getRegisteredObject(DataModel.class);
    	model.selectedIndex().unbind();
    }
}
