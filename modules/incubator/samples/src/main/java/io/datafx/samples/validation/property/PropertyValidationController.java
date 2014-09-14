package io.datafx.samples.validation.property;

import io.datafx.controller.FXMLController;
import io.datafx.controller.validation.ValidatorFX;
import io.datafx.controller.validation.context.Validator;
import io.datafx.controller.validation.event.ValidationFinishedEvent;
import io.datafx.controller.validation.event.ValidationFinishedHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;

@FXMLController("view.fxml")
public class PropertyValidationController {

    @FXML
    private Button validateButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @NotNull
    private StringProperty name = new SimpleStringProperty();
    @NotNull
    private StringProperty description = new SimpleStringProperty();
    @Validator
    private ValidatorFX<PropertyValidationController> validator;

    @PostConstruct
    public void init() {
        nameField.textProperty().bindBidirectional(name);
        descriptionField.textProperty().bindBidirectional(description);

        validator.setOnValidationFinished(new ValidationFinishedHandler<PropertyValidationController>() {

            @Override
            public void handle(ValidationFinishedEvent<PropertyValidationController> event) {
                for (ConstraintViolation<PropertyValidationController> violation : event.getViolations()) {
                    System.out.println(violation.getMessage());
                }
            }
        });

        validateButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                validator.validateAllProperties();
            }
        });
    }
}
