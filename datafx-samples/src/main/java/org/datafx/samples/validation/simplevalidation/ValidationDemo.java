package org.datafx.samples.validation.simplevalidation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ValidationDemo extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene myScene = new Scene((Parent) FXMLLoader.load(ValidationDemo.class.getResource("view.fxml")));
		stage.setScene(myScene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
