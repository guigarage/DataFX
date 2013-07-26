package org.datafx.controller.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.datafx.controller.FXMLController;
import org.datafx.controller.FXMLViewContext;
import org.datafx.controller.ViewContext;
import org.datafx.controller.demo.data.DataModel;
import org.datafx.controller.flow.FlowAction;

@FXMLController("Details.fxml")
public class DetailViewController {

    @FXMLViewContext
    private ViewContext context;
    
    @FXML
    private TextField myTextfield;
    
    @FXML
    @FlowAction("back")
    private Button backButton;
        
    @PostConstruct
    public void init() {
    	DataModel model = context.getViewFlowContext().getRegisteredObject(DataModel.class);
    	myTextfield.textProperty().bindBidirectional(model.getSelected());
    }
    
    @PreDestroy
    public void destroy() {
    	DataModel model = context.getRegisteredObject(DataModel.class);
    	model.getSelected().unbind();
    }
}
