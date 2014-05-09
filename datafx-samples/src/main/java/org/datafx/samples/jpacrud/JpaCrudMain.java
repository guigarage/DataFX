package org.datafx.samples.jpacrud;

import javafx.application.Application;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;

public class JpaCrudMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Flow(TestEntityMasterController.class).startInStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
