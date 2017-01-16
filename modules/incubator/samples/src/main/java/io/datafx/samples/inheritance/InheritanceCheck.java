package io.datafx.samples.inheritance;

import io.datafx.controller.flow.Flow;
import javafx.application.Application;
import javafx.stage.Stage;

public class InheritanceCheck extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Flow(ControllerImpl.class).startInStage(primaryStage);
    }
}
