package org.datafx.controller.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.datafx.controller.FXMLViewFlowContext;
import org.datafx.controller.ViewFlowContext;
import org.datafx.controller.demo.data.DataModel;
import org.datafx.controller.demo.data.ImageObject;
import org.datafx.controller.flow.FlowAction;

public class MasterViewController {

	@FXMLViewFlowContext
    private ViewFlowContext context;
    
    @FXML
    private ListView<ImageObject> myList;
    
    @FXML
    @FlowAction("delete")
    private Button deleteButton;
    
    @FXML
    @FlowAction("showDetails")
    private Button showDetailsButton;
    
    @PostConstruct
    public void init() {
    	DataModel model = context.getRegisteredObject(DataModel.class);
        myList.setItems(model.getImages());
        model.selectedIndex().bind(myList.getSelectionModel().selectedIndexProperty());
    }
    
    @PreDestroy
    public void destroy() {
    	DataModel model = context.getRegisteredObject(DataModel.class);
    	model.selectedIndex().unbind();
    }
}
