package org.datafx.samples.masterdetail.controller;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javafx.scene.input.MouseEvent;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.context.ActionHandler;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.FlowActionHandler;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.action.FXMLFlowAction;
import org.datafx.controller.util.VetoException;
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


    @ActionHandler
    private FlowActionHandler actionHandler;

    @PostConstruct
    public void init() {
    	DataModel model = context.getRegisteredObject(DataModel.class);
        myList.setItems(model.getNames());
        myList.getSelectionModel().select(model.selectedIndex().get());
        myList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
               if(event.getClickCount() >= 2) {
                   try {
                       actionHandler.handle("showDetails");
                   } catch (FlowException e) {
                       e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                   } catch (VetoException e) {
                       e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                   }
               }
            }
        });

        model.selectedIndex().bind(myList.getSelectionModel().selectedIndexProperty());
    }
    
    @PreDestroy
    public void destroy() {
    	DataModel model = context.getRegisteredObject(DataModel.class);
    	model.selectedIndex().unbind();
    }
}
