package org.datafx.samples.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.FXMLFlowAction;
import org.datafx.controller.validation.ValidatorFX;
import org.datafx.controller.validation.context.Validator;
import org.datafx.controller.validation.event.ValidationFinishedEvent;
import org.datafx.controller.validation.event.ValidationFinishedHandler;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import java.util.Set;

@FXMLController("detailView.fxml")
public class AddViewController {

    @FXML
    @FXMLFlowAction("save")
    private Button saveButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea notesTextArea;

    @NotNull
    private StringProperty nameProperty = new SimpleStringProperty();

    private StringProperty noteProperty = new SimpleStringProperty();

    @FXML
    private Label violationLabel;

    @Validator
    private ValidatorFX<AddViewController> validator;

    @Inject
    private DataModel model;

    @PostConstruct
    public void init() {
        nameField.textProperty().bindBidirectional(nameProperty);
        notesTextArea.textProperty().bindBidirectional(noteProperty);
        validator.setOnValidationFinished(new ValidationFinishedHandler<AddViewController>() {

            @Override
            public void handle(ValidationFinishedEvent<AddViewController> event) {
                      handleViolations(event.getViolations());
            }
        });
    }

    private void handleViolations(Set<ConstraintViolation<AddViewController>> violations) {
        if(violations.isEmpty()) {
            violationLabel.setVisible(false);
        } else {
            ConstraintViolation<AddViewController> violation = violations.iterator().next();
            violationLabel.setText(violation.getPropertyPath() + " " + violation.getMessage());
            violationLabel.setVisible(true);
        }
    }

    @ActionMethod("addPerson")
    public void addPerson() {
        Person p = new Person();
        p.setName(nameProperty.get());
        model.getPersons().add(p);
    }
}
