package org.datafx.samples.jpacrud;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;

public class JpaCrudMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new Flow(TestEntityMasterController.class).start()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
