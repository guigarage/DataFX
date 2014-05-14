package org.datafx.samples.inheritance;

import javafx.application.Application;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;

public class InheritanceCheck extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Flow(ControllerImpl.class).startInStage(primaryStage);
    }
}
