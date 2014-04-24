package org.datafx.samples.multitab;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.datafx.controller.FxmlLoadException;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;

public class MultiTabMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane pane = new BorderPane();
        TabPane tabPane = new TabPane();
        pane.setCenter(tabPane);

        FlowPane actionPane = new FlowPane();
        actionPane.setStyle("-fx-background-color: #a9a9a9");
        actionPane.setPadding(new Insets(12));

        Button addTabButton = new Button("add");
        addTabButton.setOnAction((e) -> addTab(tabPane, SampleTabController.class));
        actionPane.getChildren().add(addTabButton);
        pane.setTop(actionPane);

        primaryStage.setScene(new Scene(pane, 640, 480));
        primaryStage.show();
    }

    private <T> void addTab(TabPane tabPane, Class<T> controllerClass) {
        try {
            tabPane.getTabs().add(ViewFactory.getInstance().createTab(controllerClass));
        } catch (FxmlLoadException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
