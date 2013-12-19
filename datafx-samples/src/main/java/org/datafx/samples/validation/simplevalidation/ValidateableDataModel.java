package org.datafx.samples.validation.simplevalidation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ValidateableDataModel {

	private StringProperty name;
	
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

	@NotNull
	@Size(min=4, max=12)
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
	
	@Size(max=256)
	public String getDescription() {
		return descriptionProperty().get();
	}
	
}
