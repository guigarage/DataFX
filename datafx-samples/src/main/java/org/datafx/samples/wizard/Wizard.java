package org.datafx.samples.wizard;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.BreadCrumbBar;
import org.datafx.controller.flow.Flow;
import org.datafx.controller.flow.FlowHandler;

public class Wizard extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane pane = new BorderPane();
        BreadCrumbBar<String> breadCrumbBar = new BreadCrumbBar<>();
        pane.setTop(breadCrumbBar);

        Flow flow = new Flow(ViewController1.class);
        FlowHandler handler = flow.createHandler();
        pane.setCenter(handler.start());

        HBox actionBar = new HBox();
        actionBar.setSpacing(6);
        actionBar.setAlignment(Pos.CENTER_RIGHT);
        Button backButton = new Button("back");
        handler.attachBackEventHandler(backButton);
        Button nextButton = new Button("next");
        handler.attachEventHandler(nextButton, "next");
        actionBar.getChildren().addAll(backButton, nextButton);
        pane.setBottom(handler.start());

    }

    public static void main(String[] args) {
        launch(args);
    }
}
