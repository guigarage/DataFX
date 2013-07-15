package org.datafx.controller.flow;

import org.datafx.controller.ViewContext;
import org.datafx.controller.ViewFlowContext;

public class FXMLFlowHandler {

    private FXMLFlowView currentView;

    private FXMLFlowContainer container;

    private ViewFlowContext flowContext;
    
    public FXMLFlowHandler(FXMLFlowView startView, FXMLFlowContainer container, ViewFlowContext flowContext) {
        this.container = container;
        this.currentView = startView;
        this.flowContext = flowContext;
        flowContext.register(this);
    }
    
    public FXMLFlowHandler(FXMLFlowView startView, FXMLFlowContainer container) {
    	this(startView, container, new ViewFlowContext());
    }
    
    public ViewContext start() throws FXMLFlowException {
        return handle(currentView);
    }

    public ViewContext handle(String actionId) throws FXMLFlowException {
        return handle(currentView.getActionById(actionId));
    }

    private ViewContext handle(FXMLFlowNode node) throws FXMLFlowException {
        currentView = node.handle(currentView, flowContext, this);
        updateViewInContainer();
        return currentView.getViewContext();
    }
    
    private void updateViewInContainer() {
        container.setView(currentView.getViewContext());
    }
}
