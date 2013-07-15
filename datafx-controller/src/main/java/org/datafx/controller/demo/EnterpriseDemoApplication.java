package org.datafx.controller.demo;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.datafx.controller.ViewContext;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.ViewFlowContext;
import org.datafx.controller.demo.controller.DetailViewController;
import org.datafx.controller.demo.controller.MasterViewController;
import org.datafx.controller.demo.data.DataModel;
import org.datafx.controller.flow.FXMLFlowView;

public class EnterpriseDemoApplication extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		ViewFlowContext flowContext = new ViewFlowContext();
		flowContext.register(new DataModel());

		// ViewContext viewContext =
		// ViewFactory.getInstance().createByControllerInViewFlow(MasterViewController.class,
		// flowContext);
		// Scene myScene = new Scene((Parent) viewContext.getRootNode());

		StackPane pane = new StackPane();
		ViewContext viewContext = ViewFactory.startFlowInPane(createFlow(),
				pane, flowContext);
		Scene myScene = new Scene(pane);

		stage.setScene(myScene);
		stage.show();
	}

	public FXMLFlowView createFlow() {
		FXMLFlowView detailView = FXMLFlowView
				.create(DetailViewController.class);
		
		FXMLFlowView masterView = FXMLFlowView.create(
				MasterViewController.class).withChangeViewAction("showDetails",
				detailView);
		
		detailView.withChangeViewAction("back", masterView);
		
		return masterView;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
