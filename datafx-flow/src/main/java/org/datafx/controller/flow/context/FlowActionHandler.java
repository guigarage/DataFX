package org.datafx.controller.flow.context;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.action.FlowAction;
import org.datafx.controller.flow.action.FlowLink;
import org.datafx.controller.flow.action.FlowTaskAction;
import org.datafx.controller.util.VetoException;
import org.datafx.util.ExceptionHandler;

import java.util.UUID;

public class FlowActionHandler {

    private FlowHandler handler;

    public FlowActionHandler(FlowHandler handler) {
                 this.handler = handler;
    }

    public void handle(String actionId) throws VetoException, FlowException {
        this.handler.handle(actionId);
    }

    public void handleTask(Class<Runnable> task) throws VetoException, FlowException {
        handleAction(new FlowTaskAction(task));
    }

    public <T> void navigate(Class<T> controllerClass) throws VetoException, FlowException {
        handleAction(new FlowLink<>(controllerClass));
    }

    private void handleAction(FlowAction action) throws VetoException, FlowException {
        this.handler.handle(action, UUID.randomUUID().toString());
    }

    public ExceptionHandler getExceptionHandler() {
        return handler.getExceptionHandler();
    }
}
