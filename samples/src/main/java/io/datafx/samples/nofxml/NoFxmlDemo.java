package io.datafx.samples.nofxml;

import io.datafx.controller.flow.Flow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by hendrikebbers on 21.10.14.
 */
public class NoFxmlDemo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Flow flow = new Flow(SimpleController.class);
        flow.startInStage(primaryStage);
    }

    public static void main(String... args) {
        launch(args);
    }
}
