package org.datafx.samples.app;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.datafx.controller.flow.injection.FlowScoped;

@FlowScoped
public class DataModel {

    private ListProperty<Person> persons;
    private IntegerProperty selectedPersonIndex;

    public ListProperty<Person> getPersons() {
        if (persons == null) {
            ObservableList<Person> innerList = FXCollections.observableArrayList();
            persons = new SimpleListProperty<>(innerList);
        }
        return persons;
    }

    public int getSelectedPersonIndex() {
        return selectedPersonIndexProperty().get();
    }

    public void setSelectedPersonIndex(int selectedPersonIndex) {
        this.selectedPersonIndex.set(selectedPersonIndex);
    }

    public IntegerProperty selectedPersonIndexProperty() {
        if (selectedPersonIndex == null) {
            selectedPersonIndex = new SimpleIntegerProperty();
        }
        return selectedPersonIndex;
    }

}
