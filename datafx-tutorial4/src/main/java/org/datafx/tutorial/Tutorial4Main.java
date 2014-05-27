package org.datafx.tutorial;

import javafx.application.Application;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;

public class Tutorial4Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Flow(WizardStartController.class).
                withLink(WizardStartController.class, "next", Wizard1Controller.class).
                withLink(WizardStartController.class, "next", Wizard1Controller.class).
                withLink(Wizard1Controller.class, "next", Wizard2Controller.class).
                withLink(Wizard2Controller.class, "next", Wizard3Controller.class).
                withLink(Wizard3Controller.class, "next", WizardDoneController.class).
                withGlobalBackAction("back").
                withGlobalLink("finish", WizardDoneController.class).
                withGlobalTaskAction("help", () -> System.out.println("There is no help for the application :(")).
                startInStage(primaryStage);
    }
}

