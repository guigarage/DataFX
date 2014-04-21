package org.datafx.samples.multitab;

import javafx.application.Application;
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
        Button addTabButton = new Button("add");
        addTabButton.setOnAction((e) -> addTab(tabPane, SampleTabController.class));
        actionPane.getChildren().add(addTabButton);
        pane.setTop(actionPane);

        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }

    private <T> void addTab(TabPane tabPane, Class<T> controllerClass) {
        try {
            addTab(tabPane, ViewFactory.getInstance().createByController(controllerClass));
        } catch (FxmlLoadException e) {
            e.printStackTrace();
        }
    }

    private <T> void addTab(TabPane tabPane, ViewContext<T> context) {
        Tab tab = new Tab();
        tab.textProperty().bind(context.getMetadata().titleProperty());
        tab.graphicProperty().bind(context.getMetadata().graphicsProperty());
        tab.setContent(context.getRootNode());
        tabPane.getTabs().add(tab);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
