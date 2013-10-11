package org.datafx.controller.flow.action;

import java.util.Arrays;
import java.util.List;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;

public class FlowActionChain implements FlowAction {

	private List<FlowAction> actions;
	
	public FlowActionChain(FlowAction...actions) {
		this.actions = Arrays.asList(actions);
	}
	
	@Override
	public void handle(FlowHandler flowHandler,
			String actionId) throws FlowException {
		for(FlowAction action : actions) {
			action.handle(flowHandler, actionId);
		}
	}

}
