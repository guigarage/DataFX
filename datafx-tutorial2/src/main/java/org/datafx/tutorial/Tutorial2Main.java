package org.datafx.tutorial;

import javafx.application.Application;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;

/**
 * This tutorial shows how navigation between views can be done with the help of the DataFX Flow API. You should have understood the
 * first DataFX tutorial (see module DataFX-tutorial1) before you have a look at this example because the basics of the first tutorial
 * won't be explained here any more.
 * <p/>
 * This tutorial shows how navigation can be implemented with the FLow API. To do so two views are part of this example.
 * Both views are defined by controller classes: View1Controller and View2Controller. IN the example you can navigate
 * from view1 to view2 and vice versa. To do so a Flow is created in this Application classes and added to the Stage.
 * The Flow instance has view1 as it's start view that is defined by passing its controller class (View1Controller) as the parameter
 * to the Flow constructor.
 * How the navigation between the two views is implemented can be found in the controller classes View1Controller and View2Controller.
 * <p/>
 * Note: To differ between the two view the second one has a red background that is defined as inline CSS in its FXML file (view2.fxml).
 */
public class Tutorial2Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Flow(View1Controller.class).startInStage(primaryStage);
    }
}

