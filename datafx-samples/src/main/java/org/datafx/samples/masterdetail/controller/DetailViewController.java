package org.datafx.samples.masterdetail.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.datafx.controller.FXMLController;
import org.datafx.controller.context.FXMLViewContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.action.FXMLFlowAction;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.samples.masterdetail.data.DataModel;


@FXMLController("Details.fxml")
public class DetailViewController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    
    @FXML
    private TextField myTextfield;
    
    @FXML
    @FXMLFlowAction("back")
    private Button backButton;

    @Inject
    private DataModel model;

    @PostConstruct
    public void init() {
    	//DataModel model = context.getRegisteredObject(DataModel.class);
    	myTextfield.textProperty().bindBidirectional(model.getSelected());
    }
    
    @PreDestroy
    public void destroy() {
    	DataModel model = context.getRegisteredObject(DataModel.class);
    	model.getSelected().unbind();
    }
}
