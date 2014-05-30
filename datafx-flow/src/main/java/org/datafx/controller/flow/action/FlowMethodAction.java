package org.datafx.controller.flow.action;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;

/**
 * Implementation of a {@link FlowAction} that calls a method in the current view controller instance.
 */
public class FlowMethodAction implements FlowAction {

    private String actionMethodName;

    /**
     * Default constructor
     * @param actionMethodName defines the name of the method that should be called whenever the action is triggered.
     */
    public FlowMethodAction(String actionMethodName) {
        this.actionMethodName = actionMethodName;
    }

    @Override
    public void handle(FlowHandler flowHandler, String actionId) throws FlowException {
        Object controller = flowHandler.getCurrentViewContext().getController();
        try {
            controller.getClass().getMethod(actionMethodName).invoke(controller);
        } catch (Exception e) {
            throw new FlowException(e);
        }
    }
}
