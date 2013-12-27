package org.datafx.samples.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.FXMLFlowAction;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@FXMLController("detailView.fxml")
public class EditViewController {

    @FXML
    @FXMLFlowAction("save")
    private Button saveButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea notesTextArea;

    @Inject
    private DataModel model;

    @PostConstruct
    public void init() {
        Person p = model.getPersons().get(model.getSelectedPersonIndex());
        nameField.textProperty().bindBidirectional(p.nameProperty());
        notesTextArea.textProperty().bindBidirectional(p.notesProperty());
    }

}
