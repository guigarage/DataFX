package io.datafx.samples.jpacrud;

import io.datafx.controller.flow.Flow;
import javafx.application.Application;
import javafx.stage.Stage;

public class JpaCrudMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Flow(TestEntityMasterController.class).startInStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
