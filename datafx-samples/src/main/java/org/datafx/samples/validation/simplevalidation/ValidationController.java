package org.datafx.samples.validation.simplevalidation;

import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.validation.ConstraintViolation;

import org.datafx.controller.validation.Validatable;
import org.datafx.controller.validation.ValidatorFX;

public class ValidationController {

	@FXML
	private Button validateButton;
	
	@FXML
	private TextField nameField;
	
	@FXML
	private TextArea descriptionField;
	
	@Validatable
	private ValidateableDataModel model;
	
	public void initialize() {
		model = new ValidateableDataModel();
		
		nameField.textProperty().bindBidirectional(model.nameProperty());
		descriptionField.textProperty().bindBidirectional(model.descriptionProperty());
		
		validateButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				ValidatorFX<ValidationController> validator = new ValidatorFX<>(ValidationController.this);
				Set<ConstraintViolation<Object>> violations = validator.validateAllProperties();
				if(violations != null && !violations.isEmpty()) {
					System.out.println("Violation Error!");
					for(ConstraintViolation<Object> violiation : violations) {
						System.out.println(violiation.getMessage());
					}
				}
			}
		});
	}
}
