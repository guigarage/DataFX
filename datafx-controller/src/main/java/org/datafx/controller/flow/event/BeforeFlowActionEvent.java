package org.datafx.controller.flow.event;

import javafx.event.Event;
import javafx.event.EventType;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.action.FlowAction;

public class BeforeFlowActionEvent extends Event {

	private static final long serialVersionUID = 1L;


	public static final EventType<AfterFlowActionEvent> DEFAULT_EVENT_TYPE =
            new EventType<>("BeforeFlowActionEvent");


    private String actionId;
    private FlowAction action;
    private ViewFlowContext flowContext;

    public BeforeFlowActionEvent(String actionId, FlowAction action, ViewFlowContext flowContext) {
        super(DEFAULT_EVENT_TYPE);
        this.actionId = actionId;
        this.action = action;
        this.flowContext = flowContext;
    }

    public String getActionId() {
        return actionId;
    }

    public FlowAction getAction() {
        return action;
    }

    public ViewFlowContext getFlowContext() {
        return flowContext;
    }
}
