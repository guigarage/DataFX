package org.datafx.controller.flow.context;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
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

    public <T> void navigateBack() throws VetoException, FlowException {
        this.handler.navigateBack();
    }

    private void handleAction(FlowAction action) throws VetoException, FlowException {
        this.handler.handle(action, UUID.randomUUID().toString());
    }

    public ExceptionHandler getExceptionHandler() {
        return handler.getExceptionHandler();
    }

    public void attachEventHandler(Node node, String actionId) {
        handler.attachEventHandler(node, actionId);
    }

    public void attachLinkEventHandler(MenuItem menuItem, Class<?> controllerClass) {
        handler.attachAction(menuItem, () -> {
            try {
                navigate(controllerClass);
            } catch (Exception e) {
                getExceptionHandler().setException(e);
            }
        });
    }

    public void attachLinkEventHandler(Node node, Class<?> controllerClass) {
        handler.attachAction(node, () -> {
            try {
                navigate(controllerClass);
            } catch (Exception e) {
                getExceptionHandler().setException(e);
            }
        });
    }

    public void attachBackEventHandler(MenuItem menuItem) {
        handler.attachBackEventHandler(menuItem);
    }

    public void attachBackEventHandler(Node node) {
        handler.attachBackEventHandler(node);
    }

    public void attachEventHandler(MenuItem menuItem, String actionId) {
        handler.attachEventHandler(menuItem, actionId);
    }
}
