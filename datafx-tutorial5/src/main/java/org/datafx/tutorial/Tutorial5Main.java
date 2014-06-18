package org.datafx.tutorial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.container.DefaultFlowContainer;

/**
 * Main class of tutorial 5.
 * A flow with only 1 view controller is created here. The view controller that is defined by the
 * {@link WizardController} class defines a internal flow that manages all the separate views of the wizard.
 * @see WizardController
 */
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

