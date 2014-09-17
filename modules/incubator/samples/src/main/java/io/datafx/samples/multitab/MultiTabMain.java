package io.datafx.samples.multitab;

import io.datafx.control.cell.ServiceListCellFactory;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.core.concurrent.ObservableExecutor;
import io.datafx.samples.app.EditViewController;
import io.datafx.samples.app.LoadPersonsTask;
import io.datafx.samples.app.MasterViewController;
import io.datafx.samples.app.RemoveActionTask;
import io.datafx.samples.jpacrud.TestEntityMasterController;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class MultiTabMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane pane = new BorderPane();
        TabPane tabPane = new TabPane();
        pane.setCenter(tabPane);

        Tab concurrencyOverview = new Tab("Concurrency");
        concurrencyOverview.setClosable(false);
        ListView<Service<?>> taskList = new ListView<>();
        taskList.setItems(ObservableExecutor.getDefaultInstance().currentServicesProperty().get());
        taskList.setCellFactory(new ServiceListCellFactory());
        concurrencyOverview.setContent(taskList);
        tabPane.getTabs().add(concurrencyOverview);

        FlowPane actionPane = new FlowPane();
        actionPane.setStyle("-fx-background-color: #a9a9a9");
        actionPane.setPadding(new Insets(12));

        Button addTabButton = new Button("addFunction");
        addTabButton.setOnAction((e) -> addTab(tabPane, SampleTabController.class));

        Button addCrudTabButton = new Button("addJpaCrud");
        addCrudTabButton.setOnAction((e) -> addTab(tabPane, TestEntityMasterController.class));


        Flow flow = new Flow(MasterViewController.class).withLink(MasterViewController.class, "edit", EditViewController.class).withLink(EditViewController.class, "save", MasterViewController.class).withTaskAction(MasterViewController.class, "remove", RemoveActionTask.class).withTaskAction(MasterViewController.class, "load", LoadPersonsTask.class);
        Button addFlowTabButton = new Button("addFlow");
        addFlowTabButton.setOnAction((e) -> addTab(tabPane, flow));

        actionPane.getChildren().addAll(addTabButton, addCrudTabButton, addFlowTabButton);
        pane.setTop(actionPane);

        primaryStage.setScene(new Scene(pane, 640, 480));
        primaryStage.show();
    }

    private <T> void addTab(TabPane tabPane, Class<T> controllerClass) {
        addTab(tabPane, new Flow(controllerClass));
    }

    private <T> void addTab(TabPane tabPane, Flow flow) {
        try {
            tabPane.getTabs().add(flow.startInTab());
        } catch (FlowException e) {
            e.printStackTrace();
        }
    }

}
