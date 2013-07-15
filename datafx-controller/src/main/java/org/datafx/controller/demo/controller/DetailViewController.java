package org.datafx.controller.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javax.annotation.PostConstruct;

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
    private ImageView myImageView;
    
    @FXML
    private TextField myTextfield;
    
    @FXML
    @FlowAction("back")
    private Button backButton;
        
    @PostConstruct
    public void init() {
    	DataModel model = context.getViewFlowContext().getRegisteredObject(DataModel.class);
        myImageView.setImage(model.getSelected().getImage());
        myTextfield.setText(model.getSelected().getTitle());
    }
}
