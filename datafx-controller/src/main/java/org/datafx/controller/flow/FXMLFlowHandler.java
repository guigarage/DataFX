/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
    
    public FXMLFlowHandler withGlobalRunAction(String actionId, Class<? extends Runnable> actionClass) {
    	addGlobalAction(actionId, new FXMLFlowAction(actionClass));
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
