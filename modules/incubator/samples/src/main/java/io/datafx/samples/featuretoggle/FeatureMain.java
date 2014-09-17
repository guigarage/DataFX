package io.datafx.samples.featuretoggle;

import io.datafx.controller.ViewFactory;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
