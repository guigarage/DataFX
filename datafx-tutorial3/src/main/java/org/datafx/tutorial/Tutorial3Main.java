package org.datafx.tutorial;

import javafx.application.Application;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;

public class Tutorial3Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Flow(WizardStartController.class).startInStage(primaryStage);
    }
}

