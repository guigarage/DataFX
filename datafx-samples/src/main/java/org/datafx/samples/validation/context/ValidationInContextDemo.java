package org.datafx.samples.validation.context;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.datafx.controller.ViewFactory;

public class ValidationInContextDemo extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene myScene = new Scene((Parent) ViewFactory.getInstance().createByController(ValidationController.class).getRootNode());
		stage.setScene(myScene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
