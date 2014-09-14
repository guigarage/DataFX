package io.datafx.samples.validation.simplevalidation;

import io.datafx.controller.validation.ValidatorFX;
import io.datafx.controller.validation.event.ValidationFinishedEvent;
import io.datafx.controller.validation.event.ValidationFinishedHandler;
import io.datafx.samples.validation.ValidateableDataModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;


public class ValidationController {

	@FXML
	private Button validateButton;
	
	@FXML
	private TextField nameField;
	
	@FXML
	private TextArea descriptionField;
	
	@Valid
	private ValidateableDataModel model = new ValidateableDataModel();
	
	private ValidatorFX<ValidationController> validator = new ValidatorFX<>(this);
	
	public void initialize() {
		nameField.textProperty().bindBidirectional(model.nameProperty());
		descriptionField.textProperty().bindBidirectional(model.descriptionProperty());
		
		validator.setOnValidationFinished(new ValidationFinishedHandler<ValidationController>() {
			
			@Override
			public void handle(ValidationFinishedEvent<ValidationController> event) {
				for(ConstraintViolation<ValidationController> violiation : event.getViolations()) {
					System.out.println(violiation.getMessage());
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