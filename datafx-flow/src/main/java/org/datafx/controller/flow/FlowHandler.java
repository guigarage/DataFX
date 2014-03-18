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

import javafx.beans.property.SimpleObjectProperty;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.context.FlowActionHandler;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.action.FlowAction;
import org.datafx.controller.flow.event.*;
import org.datafx.controller.util.*;

import java.util.ResourceBundle;

public class FlowHandler {

    private FlowView<?> currentView;
    private FlowContainer container;
    private ViewFlowContext flowContext;
    private Flow flow;
    private SimpleObjectProperty<BeforeFlowActionHandler> beforeFlowActionHandler;
    private SimpleObjectProperty<AfterFlowActionHandler> afterFlowActionHandler;
    private SimpleObjectProperty<VetoableBeforeFlowActionHandler> vetoableBeforeFlowActionHandler;

    private SimpleObjectProperty<VetoHandler> vetoHandler;

    private ViewConfiguration viewConfiguration;

    public FlowHandler(Flow flow, ViewFlowContext flowContext, ViewConfiguration viewConfiguration) {
        this.flowContext = flowContext;
        this.flow = flow;
        this.viewConfiguration = viewConfiguration;
        flowContext.register(new FlowActionHandler(this));
    }

    public FlowHandler(Flow flow, ViewFlowContext flowContext) {
        this(flow, flowContext, new ViewConfiguration());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(FlowContainer container) throws FlowException {
        this.container = container;
        flowContext.register(this);
        if(viewConfiguration != null) {
            flowContext.register(ResourceBundle.class.toString(), viewConfiguration.getResources());
        }
        try {
            FlowView<?> startView = new FlowView(ViewFactory.getInstance()
                    .createByControllerInViewFlow(
                            this.flow.getStartViewControllerClass(),
                            flowContext, null, this, getViewConfiguration()));
            setNewView(startView);
        } catch (FxmlLoadException e) {
            throw new FlowException(e);
        }
    }

    public ViewConfiguration getViewConfiguration() {
        return viewConfiguration;
    }

    public void handle(String actionId) {
        try {
        FlowAction action = null;
        if (currentView != null) {
            action = currentView.getActionById(actionId);
        }
        if (action == null) {
            action = flow.getGlobalActionById(actionId);
        }
        handle(action, actionId);
        } catch (Exception e) {
            //TODO: ExceptionHandler
            e.printStackTrace();
        }
    }

    public ViewFlowContext getFlowContext() {
        return flowContext;
    }

    public FlowView<?> getCurrentView() {
        return currentView;
    }

    public ViewContext<?> getCurrentViewContext() {
        return currentView.getViewContext();
    }

    public void handle(FlowAction action, String actionId) throws FlowException, VetoException {

        if (beforeFlowActionHandler != null && beforeFlowActionHandler.getValue() != null) {
            beforeFlowActionHandler.getValue().handle(new BeforeFlowActionEvent(actionId, action, flowContext));
        }

        if (vetoableBeforeFlowActionHandler != null && vetoableBeforeFlowActionHandler.getValue() != null) {
            try {
                vetoableBeforeFlowActionHandler.getValue().handle(new BeforeFlowActionEvent(actionId, action, flowContext));
            } catch (Veto veto) {
                if (vetoHandler != null && vetoHandler.getValue() != null) {
                    vetoHandler.get().onVeto(veto);
                }
                throw new VetoException(veto);
            }
        }

        action.handle(this, actionId);
        if (afterFlowActionHandler != null && afterFlowActionHandler.getValue() != null) {
            afterFlowActionHandler.getValue().handle(new AfterFlowActionEvent(actionId, action, flowContext));
        }
    }

    public <U> ViewContext<U> setNewView(FlowView<U> newView)
            throws FlowException {

        flow.addActionsToView(newView);

        FlowView<?> oldView = currentView;

        if (oldView != null) {
            ViewContext<?> lastViewContext = oldView.getViewContext();
            if (lastViewContext != null) {
                try {
                    lastViewContext.destroy();
                } catch (Exception e) {
                    throw new FlowException(
                            "Last ViewContext can't be destroyed!", e);
                }
            }
        }

        currentView = newView;
        flowContext.setCurrentViewContext(currentView.getViewContext());
        container.setView(currentView.getViewContext());
        return newView.getViewContext();
    }
}
