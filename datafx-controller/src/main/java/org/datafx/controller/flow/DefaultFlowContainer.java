package org.datafx.controller.flow;

import javafx.scene.layout.StackPane;

import org.datafx.controller.context.ViewContext;

public class DefaultFlowContainer implements FXMLFlowContainer {

	private StackPane pane;
	
	public DefaultFlowContainer(StackPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void setView(ViewContext context) {
		pane.getChildren().clear();
		pane.getChildren().add(context.getRootNode());
	}

	@Override
	public void flowFinished() {
		// TODO Auto-generated method stub

	}

}
