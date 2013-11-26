package org.datafx.concurrent;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;

public class TaskState {

	private DoubleProperty progress;
	
	private DoubleProperty maxProgress;
	
	private ObservableStringValue title;
	
	private ObservableStringValue message;
	
	public DoubleProperty maxProgress() {
		if(maxProgress == null) {
			maxProgress = new SimpleDoubleProperty();
		}
		return maxProgress;
	}
	
	public void setMaxProgress(double maxProgress) {
		maxProgress().set(maxProgress);
	}
	
	public double getMaxProgress() {
		return maxProgress == null ? 0d : maxProgress.get();
	}
	
	public DoubleProperty progress() {
		if(progress == null) {
			progress = new SimpleDoubleProperty();
		}
		return progress;
	}
	
	public double getProgress() {
		return progress == null ? 0d : progress.get();
	}
	
	public void setProgress(double progress) {
		progress().set(progress);
	}
	public ObservableStringValue title() {
		if(title == null) {
			title = new SimpleStringProperty();
		}
		return title;
	}
}
