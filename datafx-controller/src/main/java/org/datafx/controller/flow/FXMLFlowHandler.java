package org.datafx.controller.flow;

import org.datafx.controller.ViewContext;
import org.datafx.controller.ViewFlowContext;

public class FXMLFlowHandler {

    private FXMLFlowView currentView;
    
    private FXMLFlowView startView;


    private FXMLFlowContainer container;

    private ViewFlowContext flowContext;
    
    public FXMLFlowHandler(FXMLFlowView startView, FXMLFlowContainer container, ViewFlowContext flowContext) {
        this.container = container;
        this.startView = startView;
        this.flowContext = flowContext;
        flowContext.register(this);
    }
    
    public FXMLFlowHandler(FXMLFlowView startView, FXMLFlowContainer container) {
    	this(startView, container, new ViewFlowContext());
    }
    
    public ViewContext start() throws FXMLFlowException {
        return handle(startView);
    }

    public ViewContext handle(String actionId) throws FXMLFlowException {
        return handle(currentView.getActionById(actionId));
    }

    private ViewContext handle(FXMLFlowNode node) throws FXMLFlowException {
    	FXMLFlowView oldViewContext = currentView;
        currentView = node.handle(currentView, flowContext, this);
        
        if(oldViewContext != null) {
        	ViewContext lastViewContext = oldViewContext.getViewContext();
        	if(lastViewContext != null) {
        		lastViewContext.destroy();
        	}
        }
        flowContext.setCurrentViewContext(currentView.getViewContext());
        container.setView(currentView.getViewContext());
        return currentView.getViewContext();
    }
}
