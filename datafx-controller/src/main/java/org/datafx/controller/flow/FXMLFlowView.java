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

import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.ViewFlowContext;
import org.datafx.controller.util.FxmlLoadException;

public class FXMLFlowView implements FXMLFlowNode {

    private Class<?> controllerClass;

    private Map<String, FXMLFlowNode> flowMap;

    private ViewContext viewContext;
    
    public FXMLFlowView(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
        flowMap = new HashMap<>();
    }

    @Override public FXMLFlowView handle(FXMLFlowView currentFlowView, ViewFlowContext flowContext, FXMLFlowHandler flowHandler) throws FXMLFlowException {
        try {
            viewContext = ViewFactory.getInstance().createByControllerInViewFlow(controllerClass, flowContext, null, flowHandler);
            return this;
        } catch (FxmlLoadException e) {
            throw new FXMLFlowException(e);
        }
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
    
    public FXMLFlowView withRunAction(String actionId, Class<? extends Runnable> runnableClass) {
        addAction(actionId, new FXMLFlowAction(runnableClass));
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
