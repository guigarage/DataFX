package io.datafx.samples.executor;

import io.datafx.core.concurrent.ObservableExecutor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Date;

public class InfinitiveProgressChain extends Application {

    private Label label;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        label = new Label();
        Scene myScene = new Scene(new StackPane(label), 300, 100);
        primaryStage.setScene(myScene);
        primaryStage.show();
        ObservableExecutor.getDefaultInstance().createProcessChain().addRunnableInPlatformThread(() -> label.setText(new Date() + "")).repeatInfinite(Duration.seconds(1));
    }
}
