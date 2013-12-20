package org.datafx.controller.flow;

import java.util.HashMap;
import java.util.Map;

import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.action.FlowAction;
import org.datafx.controller.flow.action.FlowLink;
import org.datafx.controller.flow.action.FlowTaskAction;

public class Flow {

	private Class<?> startViewControllerClass;

	private Map<Class<?>, Map<String, FlowAction>> viewFlowMap;

	private Map<String, FlowAction> globalFlowMap;

	public Flow(Class<?> startViewControllerClass) {
		this.startViewControllerClass = startViewControllerClass;
		globalFlowMap = new HashMap<>();
		viewFlowMap = new HashMap<>();
	}

	public FlowHandler createHandler(ViewFlowContext flowContext) {
		return new FlowHandler(this, flowContext);
	}

	public FlowHandler createHandler() throws FlowException {
		return createHandler(new ViewFlowContext());
	}

	public <U> Flow withGlobalAction(String actionId, FlowAction action) {
		addGlobalAction(actionId, action);
		return this;
	}

	public Flow withGlobalRunAction(String actionId,
			Class<? extends Runnable> actionClass) {
		addGlobalAction(actionId, new FlowTaskAction(actionClass));
		return this;
	}

	public Flow withGlobalLink(String actionId, Class<?> controllerClass) {
		addGlobalAction(actionId, new FlowLink<>(controllerClass));
		return this;
	}

	public <U> Flow withAction(Class<?> controllerClass, String actionId,
			FlowAction action) {
		addActionToView(controllerClass, actionId, action);
		return this;
	}

	public Flow withLink(Class<?> fromControllerClass, String actionId,
			Class<?> toControllerClass) {
		addActionToView(fromControllerClass, actionId, new FlowLink<>(
				toControllerClass));
		return this;
	}

	public Flow withTaskAction(Class<?> controllerClass, String actionId,
			Class<? extends Runnable> actionClass) {
		addActionToView(controllerClass, actionId, new FlowTaskAction(
				actionClass));
		return this;
	}

	public <U> void addActionToView(Class<?> controllerClass, String actionId,
			FlowAction action) {
		if (viewFlowMap.get(controllerClass) == null) {
			viewFlowMap.put(controllerClass, new HashMap<String, FlowAction>());
		}
		viewFlowMap.get(controllerClass).put(actionId, action);
	}

	public <U> void addGlobalAction(String actionId, FlowAction action) {
		globalFlowMap.put(actionId, action);
	}

	public FlowAction getGlobalActionById(String actionId) {
		return globalFlowMap.get(actionId);
	}

	public Class<?> getStartViewControllerClass() {
		return startViewControllerClass;
	}

	public <U> void addActionsToView(FlowView<U> newView) {
		Map<String, FlowAction> viewActionMap = viewFlowMap.get(newView
				.getViewContext().getController().getClass());
		if (viewActionMap != null) {
			for (String actionId : viewActionMap.keySet()) {
				newView.addAction(actionId, viewActionMap.get(actionId));
			}
		}
	}
}
