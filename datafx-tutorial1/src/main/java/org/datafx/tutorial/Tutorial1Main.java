package org.datafx.tutorial;

import javafx.application.Application;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;

/**
 * Main class for the first tutorial of the DataFX API. This application will show only one view in the given Stage.
 * The view is defined by the DataFX controller API and shown in a DataFX flow.
 * As we will see later a flow definition in DataFX can contain several views that are linked internally. But in this
 * first example we will only use one view.
 */
public class Tutorial1Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //This is the most simple way to start a flow:
        // The controller class of the start view will always passed as parameter to the constructor of the FLow class
        // The FLow class provides a util method that shows the flow in a Stage. By doing so the Scene will be created
        // automatically and the Stage will contain a Scene that only contains the flow.
        // In this first tutorial the flow will only contain one view.
        new Flow(SimpleController.class).startInStage(primaryStage);
    }
}
