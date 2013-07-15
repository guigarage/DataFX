package org.datafx.controller.demo.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {

	private ObservableList<ImageObject> images;
	
	private IntegerProperty selectedIndex;
	
	public DataModel() {
		selectedIndex = new SimpleIntegerProperty(-1);
		images = FXCollections.observableArrayList();
		
		images.add(new ImageObject());
		images.add(new ImageObject());
		images.add(new ImageObject());
	}
	
    public void deleteSelected() {
    	images.remove(selectedIndex.get());
    }

    public ImageObject getSelected() {
        return images.get(selectedIndex.get());
    }
    
    public ObservableList<ImageObject> getImages() {
        return images;
    }
    
    public IntegerProperty selectedIndex() {
        return selectedIndex;
    }
}
