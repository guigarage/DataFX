package org.datafx.samples.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The controller for the edit view.
 */
@FXMLController(value="detailView.fxml", title = "Edit user")
public class EditViewController {

    /**
     * The save button is bound to a flow action.
     * This action is defined by the unique id "save"
     */
    @FXML
    @ActionTrigger("save")
    private Button saveButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea notesTextArea;

    /**
     * The data model is injected.
     */
    @Inject
    private DataModel model;

    /**
     * This binds the textfield to values of the selected person.
     */
    @PostConstruct
    public void init() {
        Person p = model.getPersons().get(model.getSelectedPersonIndex());
        nameField.textProperty().bindBidirectional(p.nameProperty());
        notesTextArea.textProperty().bindBidirectional(p.notesProperty());
    }

}
