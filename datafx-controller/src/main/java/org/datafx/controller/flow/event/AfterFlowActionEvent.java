package org.datafx.controller.flow.event;

import javafx.event.Event;
import javafx.event.EventType;
import org.datafx.controller.context.ViewFlowContext;
import org.datafx.controller.flow.action.FlowAction;

public class AfterFlowActionEvent extends Event {

    public static final EventType<AfterFlowActionEvent> DEFAULT_EVENT_TYPE =
            new EventType<>("AfterFlowActionEvent");


    private String actionId;
    private FlowAction action;
    private ViewFlowContext flowContext;

    public AfterFlowActionEvent(String actionId, FlowAction action, ViewFlowContext flowContext) {
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
