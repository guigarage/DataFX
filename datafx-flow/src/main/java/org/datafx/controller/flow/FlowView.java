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
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.action.FlowAction;

/**
 * This class defines one view in the flow. Normally this class will only be used by the Flow API and should not be used by developers
 * @param <T> Controller class of the Flow
 *
 * @author Hendrik Ebbers
 */
public class FlowView<T> {

    private Map<String, FlowAction> flowMap;

    private ViewContext<T> viewContext;

    /**
     * Creates a View based on a context
     * @param viewContext The context of the view
     */
    public FlowView(ViewContext<T> viewContext) {
    	this.viewContext = viewContext;
        flowMap = new HashMap<>();
    }

    /**
     * Adds a flow action to the view
     * @param actionId unique ID of the action
     * @param action  the action
     */
    public void addAction(String actionId, FlowAction action) {
        flowMap.put(actionId, action);
    }

    /**
     * Returns the registered flow action for the given unique id or null if no action is registered for the id
     * @param actionId the unique action id
     * @return the flow action for the given id
     */
    public FlowAction getActionById(String actionId) {
        return flowMap.get(actionId);
    }

    /**
     * Returns the context of the view
     * @return  context of the view
     */
    public ViewContext<T> getViewContext() {
        return viewContext;
    }

    /**
     * Returns the context of the flow
     * @return
     */
    public ViewFlowContext getViewFlowContext() {
        return getViewContext().getRegisteredObject(ViewFlowContext.class);
    }
}
