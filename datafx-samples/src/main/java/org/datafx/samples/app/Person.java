package org.datafx.samples.app;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.datafx.controller.injection.ViewScoped;

public class Person {

    private StringProperty name;

    private StringProperty notes;

    public String getName() {
        return nameProperty().get();
    }

    public StringProperty nameProperty() {
        if(name == null) {
            name = new SimpleStringProperty();
        }
        return name;
    }

    public void setName(String name) {
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

    public void setNotes(String notes) {
        this.notesProperty().set(notes);
    }

    @Override
    public String toString() {
        return getName();
    }
}
