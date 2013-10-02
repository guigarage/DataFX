package org.datafx.samples.masterdetail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewFlowContext;
import org.datafx.controller.flow.FXMLFlowView;
import org.datafx.samples.masterdetail.action.DeleteAction;
import org.datafx.samples.masterdetail.controller.DetailViewController;
import org.datafx.samples.masterdetail.controller.MasterViewController;
import org.datafx.samples.masterdetail.data.DataModel;

public class MasterDetailApp extends Application {

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
