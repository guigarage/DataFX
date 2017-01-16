package io.datafx.samples.app;

import io.datafx.controller.injection.scopes.FlowScoped;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The data model for the sample. The model is defined as flow scoped.
 * By doing so the model can be injected in any controller or action. For one flow the injected instance will always be the same.
 */
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
