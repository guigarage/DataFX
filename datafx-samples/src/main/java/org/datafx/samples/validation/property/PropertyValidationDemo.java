package org.datafx.samples.validation.property;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.datafx.controller.ViewFactory;
import org.datafx.samples.validation.context.ValidationController;

public class PropertyValidationDemo extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene myScene = new Scene((Parent) ViewFactory.getInstance().createByController(PropertyValidationController.class).getRootNode());
        stage.setScene(myScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}