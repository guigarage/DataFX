package io.datafx.samples.messagebus;

import io.datafx.controller.flow.Flow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by hendrikebbers on 21.10.14.
 */
public class MessageBusDemo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        HBox box = new HBox();
        box.setSpacing(12);
        box.setPadding(new Insets(12));

        Flow senderFlow = new Flow(ProducerController.class);
        box.getChildren().add(senderFlow.start());

        Flow receiverFlow = new Flow(ReceiverController.class);
        box.getChildren().add(receiverFlow.start());

        primaryStage.setScene(new Scene(box));
        primaryStage.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}
