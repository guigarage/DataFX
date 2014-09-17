package io.datafx.samples.validation.context;

import io.datafx.controller.FXMLController;
import io.datafx.controller.validation.ValidatorFX;
import io.datafx.controller.validation.context.Validator;
import io.datafx.controller.validation.event.ValidationFinishedEvent;
import io.datafx.controller.validation.event.ValidationFinishedHandler;
import io.datafx.samples.validation.ValidateableDataModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;


@FXMLController("view.fxml")
public class ValidationController {
	
	@FXML
	private Button validateButton;
	
	@FXML
	private TextField nameField;
	
	@FXML
	private TextArea descriptionField;
	
	@Valid
	private ValidateableDataModel model = new ValidateableDataModel();
	
	@Validator
	private ValidatorFX<ValidationController> validator;
	
	@PostConstruct
	public void init() {
		nameField.textProperty().bindBidirectional(model.nameProperty());
		descriptionField.textProperty().bindBidirectional(model.descriptionProperty());
		
		validator.setOnValidationFinished(new ValidationFinishedHandler<ValidationController>() {
			
			@Override
			public void handle(ValidationFinishedEvent<ValidationController> event) {
				for(ConstraintViolation<ValidationController> violation : event.getViolations()) {
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
