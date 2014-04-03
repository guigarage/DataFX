package org.datafx.samples.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A simple entity
 */
public class Person {

    private StringProperty name;

    private StringProperty notes;

    public Person() {
    }
    
    public Person (String name, String notes) {
        setName(name);
        setNotes(notes);
    }
    
    public String getName() {
        return nameProperty().get();
    }

    public StringProperty nameProperty() {
        if(name == null) {
            name = new SimpleStringProperty();
        }
        return name;
    }

    public final void setName(String name) {
        this.nameProperty().set(name);
    }

    public String getNotes() {
        return notesProperty().get();
    }

    public StringProperty notesProperty() {
        if(notes == null) {
            notes = new SimpleStringProperty();
        }
        return notes;
    }

    public final void setNotes(String notes) {
        this.notesProperty().set(notes);
    }

    @Override
    public String toString() {
        return getName();
    }
}
