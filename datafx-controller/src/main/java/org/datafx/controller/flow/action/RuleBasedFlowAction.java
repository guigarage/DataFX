package org.datafx.controller.flow.action;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;

public abstract class RuleBasedFlowAction implements FlowAction {

	@Override
	public void handle(FlowHandler flowHandler,
			String actionId) throws FlowException {
		getCurrentAction(flowHandler, actionId).handle(flowHandler, actionId);
	}
	
	protected abstract FlowAction getCurrentAction(FlowHandler flowHandler,
			String actionId);

}
