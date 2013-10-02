package org.datafx.samples.masterdetail.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {

	private ObservableList<StringProperty> names;
	
	private IntegerProperty selectedIndex;
	
	public DataModel() {
		selectedIndex = new SimpleIntegerProperty(-1);
		names = FXCollections.observableArrayList();
		
		names.add(new SimpleStringProperty("Jonathan"));
		names.add(new SimpleStringProperty("Johan"));
		names.add(new SimpleStringProperty("Hendrik"));
	}
	
    public void deleteSelected() {
    	names.remove(selectedIndex.get());
    }

    public StringProperty getSelected() {
        return names.get(selectedIndex.get());
    }
    
    public ObservableList<StringProperty> getNames() {
        return names;
    }
    
    public IntegerProperty selectedIndex() {
        return selectedIndex;
    }
}
