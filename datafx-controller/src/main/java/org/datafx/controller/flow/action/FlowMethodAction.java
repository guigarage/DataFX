package org.datafx.controller.flow.action;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;

import java.lang.reflect.InvocationTargetException;

public class FlowMethodAction implements FlowAction {

    private String actionMethodName;

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
