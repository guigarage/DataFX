package org.datafx.controller.flow;

import java.util.HashMap;
import java.util.Map;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.ViewFlowContext;

public class FXMLFlowHandler {

    private FXMLFlowView currentView;
    
    private FXMLFlowView startView;

    private FXMLFlowContainer container;

    private ViewFlowContext flowContext;
    
    private Map<String, FXMLFlowNode> globalFlowMap;
    
    public FXMLFlowHandler(FXMLFlowView startView, FXMLFlowContainer container, ViewFlowContext flowContext) {
        this.container = container;
        this.startView = startView;
        this.flowContext = flowContext;
        
        globalFlowMap = new HashMap<>();
        
        flowContext.register(this);
    }
    
    public FXMLFlowHandler(FXMLFlowView startView, FXMLFlowContainer container) {
    	this(startView, container, new ViewFlowContext());
    }
    
    public ViewContext start() throws FXMLFlowException {
        return handle(startView);
    }

    public ViewContext handle(String actionId) throws FXMLFlowException {
    	FXMLFlowNode flowNode = null;
    	if(currentView != null) {
    		flowNode = currentView.getActionById(actionId);
    	}
    	if(flowNode == null) {
    		flowNode = getGlobalActionById(actionId);
    	}
        return handle(flowNode);
    }

    public FXMLFlowHandler withGlobalAction(String actionId, FXMLFlowNode node) {
    	addGlobalAction(actionId, node);
    	return this;
    }
    
    public void addGlobalAction(String actionId, FXMLFlowNode node) {
    	globalFlowMap.put(actionId, node);
    }
    
    public FXMLFlowNode getGlobalActionById(String actionId) {
        return globalFlowMap.get(actionId);
    }
    
    public ViewFlowContext getFlowContext() {
		return flowContext;
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
