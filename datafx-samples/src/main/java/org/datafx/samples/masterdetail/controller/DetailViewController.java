package org.datafx.samples.masterdetail.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.datafx.controller.FXMLController;
import org.datafx.controller.context.FXMLViewContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.action.FXMLFlowAction;
import org.datafx.samples.masterdetail.data.DataModel;


@FXMLController("Details.fxml")
public class DetailViewController {

    @FXMLViewContext
    private ViewContext<DetailViewController> context;
    
    @FXML
    private TextField myTextfield;
    
    @FXML
    @FXMLFlowAction("back")
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
