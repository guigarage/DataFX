package org.datafx.tutorial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.datafx.controller.flow.DefaultFlowContainer;
import org.datafx.controller.flow.Flow;
import org.datafx.controller.flow.FlowHandler;

public class Tutorial5Main  extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Flow flow  = new Flow(WizardController.class);

        FlowHandler flowHandler = flow.createHandler();

        StackPane pane = flowHandler.start(new DefaultFlowContainer());
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }
}

