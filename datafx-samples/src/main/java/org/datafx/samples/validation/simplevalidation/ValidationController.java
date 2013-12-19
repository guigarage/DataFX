package org.datafx.samples.validation.simplevalidation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.validation.ConstraintViolation;

import org.datafx.controller.validation.Validatable;
import org.datafx.controller.validation.ValidatorFX;
import org.datafx.controller.validation.event.ValidationFinishedEvent;
import org.datafx.controller.validation.event.ValidationFinishedHandler;

public class ValidationController {

	@FXML
	private Button validateButton;
	
	@FXML
	private TextField nameField;
	
	@FXML
	private TextArea descriptionField;
	
	@Validatable
	private ValidateableDataModel model = new ValidateableDataModel();
	
	private ValidatorFX<ValidationController> validator = new ValidatorFX<>(this);
	
	public void initialize() {
		nameField.textProperty().bindBidirectional(model.nameProperty());
		descriptionField.textProperty().bindBidirectional(model.descriptionProperty());
		
		validator.setOnValidationFinished(new ValidationFinishedHandler() {
			
			@Override
			public void handle(ValidationFinishedEvent event) {
				for(ConstraintViolation<Object> violiation : event.getViolations()) {
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
