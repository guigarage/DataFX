package org.datafx.samples.masterdetail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.DefaultFlowContainer;
import org.datafx.controller.flow.Flow;
import org.datafx.samples.masterdetail.action.DeleteAction;
import org.datafx.samples.masterdetail.controller.DetailViewController;
import org.datafx.samples.masterdetail.controller.MasterViewController;
import org.datafx.samples.masterdetail.data.DataModel;

public class MasterDetailApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		StackPane pane = new StackPane();
		
		DefaultFlowContainer flowContainer = new DefaultFlowContainer(pane);

		Flow flow = new Flow(MasterViewController.class)
				.withLink(MasterViewController.class, "showDetails",
						DetailViewController.class)
				.withLink(DetailViewController.class, "back",
						MasterViewController.class)
				.withTaskAction(MasterViewController.class, "delete",
						DeleteAction.class);

		flow.createHandler().start(flowContainer);
		
		Scene myScene = new Scene(pane);

		stage.setScene(myScene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
