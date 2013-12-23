package org.datafx.samples.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.datafx.controller.flow.DefaultFlowContainer;
import org.datafx.controller.flow.Flow;
import org.datafx.controller.flow.action.FlowActionChain;
import org.datafx.controller.flow.action.FlowLink;
import org.datafx.controller.flow.action.FlowMethodAction;
import org.datafx.controller.validation.flow.ValidationFlowAction;

public class DataFXDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Flow flow = new Flow(MasterViewController.class).
                withLink(MasterViewController.class, "edit", EditViewController.class).
                withLink(MasterViewController.class, "add", AddViewController.class).
                withLink(EditViewController.class, "save", MasterViewController.class).
                withTaskAction(MasterViewController.class, "remove", RemoveActionTask.class).
                withTaskAction(MasterViewController.class, "load", LoadPersonsTask.class).
                withAction(AddViewController.class, "save", new FlowActionChain(new ValidationFlowAction(), new FlowMethodAction("addPerson"), new FlowLink<MasterViewController>(MasterViewController.class)));

        DefaultFlowContainer container = new DefaultFlowContainer();
        flow.createHandler().start(container);
        Scene scene = new Scene(container.getPane());
        stage.setScene(scene);
        stage.show();
    }

}
