package org.datafx.controller.flow;

import java.util.HashMap;
import java.util.Map;

import org.datafx.controller.FxmlLoadException;
import org.datafx.controller.ViewContext;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.ViewFlowContext;

public class FXMLFlowView implements FXMLFlowNode {

    private Class<?> controllerClass;

    private FXMLFlowView previousViewContext;

    private Map<String, FXMLFlowNode> flowMap;

    private ViewContext viewContext;
    
    public FXMLFlowView(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
        flowMap = new HashMap<>();
    }

    @Override public FXMLFlowView handle(FXMLFlowView currentViewContext, ViewFlowContext flowContext, FXMLFlowHandler flowHandler) throws FXMLFlowException {
        try {
            viewContext = ViewFactory.getInstance().createByControllerInViewFlow(controllerClass, flowContext, null, flowHandler);
            previousViewContext = currentViewContext;
            return this;
        } catch (FxmlLoadException e) {
            throw new FXMLFlowException(e);
        }
    }

    public FXMLFlowView getPreviousViewContext() {
        return previousViewContext;
    }

    public static FXMLFlowView create(Class<?> controllerClass) {
        return new FXMLFlowView(controllerClass);
    }

    public FXMLFlowView withChangeViewAction(String actionId, Class<?> controllerClass) {
        return withChangeViewAction(actionId, new FXMLFlowView(controllerClass));
    }

    public FXMLFlowView withChangeViewAction(String actionId, FXMLFlowView flowView) {
        addAction(actionId, flowView);
        return this;
    }
    
    public FXMLFlowView withFinishFlowAction(String actionId) {
        //TODO!!!
        return this;
    }

    public FXMLFlowView withRunAction(String actionId, Runnable runnable) {
        addAction(actionId, new FXMLFlowAction(runnable));
        return this;
    }
    
    private void addAction(String actionId, FXMLFlowNode node) {
        flowMap.put(actionId, node);
    }
    
    public FXMLFlowNode getActionById(String actionId) {
        return flowMap.get(actionId);
    }
    
    public ViewContext getViewContext() {
        return viewContext;
    }
    
    public ViewFlowContext getViewFlowContext() {
        return getViewContext().getViewFlowContext();
    }
}
