package org.datafx.samples;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Book {

	private StringProperty titleProperty = new SimpleStringProperty();

	public StringProperty titleProperty() {
		return titleProperty;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return titleProperty.get();
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		titleProperty.set(title);
	}
	
	public String toString() {
		return "book with title "+titleProperty.get();
	}
}
