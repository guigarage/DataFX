package org.datafx.controller.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.datafx.controller.ViewFactory;
import org.datafx.controller.ViewFlowContext;
import org.datafx.controller.demo.action.DeleteAction;
import org.datafx.controller.demo.controller.DetailViewController;
import org.datafx.controller.demo.controller.MasterViewController;
import org.datafx.controller.demo.data.DataModel;
import org.datafx.controller.flow.FXMLFlowView;

public class EnterpriseDemoApplication extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		ViewFlowContext flowContext = new ViewFlowContext();
		flowContext.register(new DataModel());

		StackPane pane = new StackPane();
		ViewFactory.startFlowInPane(createFlow(),
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
				detailView).withRunAction("delete", DeleteAction.class);
		
		detailView.withChangeViewAction("back", masterView);
		
		return masterView;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
