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

import org.datafx.controller.context.ViewFlowContext;
import org.datafx.controller.flow.action.FlowAction;
import org.datafx.controller.flow.action.FlowLink;
import org.datafx.controller.flow.action.FlowTaskAction;

public class Flow {

	private Class<?> startViewControllerClass;

	private Map<Class<?>, Map<String, FlowAction>> viewFlowMap;

	private Map<String, FlowAction> globalFlowMap;

	public Flow(Class<?> startViewControllerClass) {
		this.startViewControllerClass = startViewControllerClass;
		globalFlowMap = new HashMap<>();
		viewFlowMap = new HashMap<>();
	}

	public FlowHandler createHandler(ViewFlowContext flowContext) {
		return new FlowHandler(this, flowContext);
	}

	public FlowHandler createHandler() throws FlowException {
		return createHandler(new ViewFlowContext());
	}

	public <U> Flow withGlobalAction(String actionId, FlowAction action) {
		addGlobalAction(actionId, action);
		return this;
	}

	@SuppressWarnings("unchecked")
	public Flow withGlobalRunAction(String actionId,
			Class<? extends Runnable> actionClass) {
		addGlobalAction(actionId, new FlowTaskAction(actionClass));
		return this;
	}

	public Flow withGlobalLink(String actionId, Class<?> controllerClass) {
		addGlobalAction(actionId, new FlowLink<>(controllerClass));
		return this;
	}

	public <U> Flow withAction(Class<?> controllerClass, String actionId,
			FlowAction action) {
		addActionToView(controllerClass, actionId, action);
		return this;
	}

	public Flow withLink(Class<?> fromControllerClass, String actionId,
			Class<?> toControllerClass) {
		addActionToView(fromControllerClass, actionId, new FlowLink<>(
				toControllerClass));
		return this;
	}

	@SuppressWarnings("unchecked")
	public Flow withTaskAction(Class<?> controllerClass, String actionId,
			Class<? extends Runnable> actionClass) {
		addActionToView(controllerClass, actionId, new FlowTaskAction(
				actionClass));
		return this;
	}

	public <U> void addActionToView(Class<?> controllerClass, String actionId,
			FlowAction action) {
		if (viewFlowMap.get(controllerClass) == null) {
			viewFlowMap.put(controllerClass, new HashMap<String, FlowAction>());
		}
		viewFlowMap.get(controllerClass).put(actionId, action);
	}

	public <U> void addGlobalAction(String actionId, FlowAction action) {
		globalFlowMap.put(actionId, action);
	}

	public FlowAction getGlobalActionById(String actionId) {
		return globalFlowMap.get(actionId);
	}

	public Class<?> getStartViewControllerClass() {
		return startViewControllerClass;
	}

	public <U> void addActionsToView(FlowView<U> newView) {
		Map<String, FlowAction> viewActionMap = viewFlowMap.get(newView
				.getViewContext().getController().getClass());
		if (viewActionMap != null) {
			for (String actionId : viewActionMap.keySet()) {
				newView.addAction(actionId, viewActionMap.get(actionId));
			}
		}
	}
}
