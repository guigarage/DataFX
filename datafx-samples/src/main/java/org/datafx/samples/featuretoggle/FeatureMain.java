package org.datafx.samples.featuretoggle;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.datafx.controller.ViewFactory;
import org.datafx.samples.validation.context.ValidationController;

public class FeatureMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene myScene = new Scene((Parent) ViewFactory.getInstance().createByController(FeatureController.class).getRootNode());
        stage.setScene(myScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
