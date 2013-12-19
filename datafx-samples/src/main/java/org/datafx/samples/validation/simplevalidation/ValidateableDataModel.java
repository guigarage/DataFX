package org.datafx.samples.validation.simplevalidation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class ValidateableDataModel {
	
	@NotNull
	private StringProperty name;
	
	@Null
	private StringProperty description;

	public StringProperty nameProperty() {
		if(name == null) {
			name = new SimpleStringProperty();
		}
		return name;
	}

	public void setName(String name) {
		nameProperty().set(name);
	}

	public String getName() {
		return nameProperty().get();
	}
	
	public StringProperty descriptionProperty() {
		if(description == null) {
			description = new SimpleStringProperty();
		}
		return description;
	}

	public void setDescription(String description) {
		descriptionProperty().set(description);
	}
	
	public String getDescription() {
		return descriptionProperty().get();
	}
	
}
