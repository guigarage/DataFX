package io.datafx.samples;

import io.datafx.core.concurrent.ProcessChain;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ProcessChainDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Label label = new Label("No data");

        Button button = new Button("Press me");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ProcessChain.create().addRunnableInPlatformThread(() -> button.setDisable(true))
                        .addRunnableInExecutor(() -> communicateWithServer())
                        .addSupplierInExecutor(() -> {
                            return "Time in Millis: " + System.currentTimeMillis();
                        })
                        .addConsumerInPlatformThread((Consumer<String>) (t) -> label.setText(t.toString()))
                        .addRunnableInPlatformThread(() -> button.setDisable(false))
                        .run();
            }
        });

        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(12);
        pane.setPadding(new Insets(12));
        pane.getChildren().addAll(label, button);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void communicateWithServer() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
