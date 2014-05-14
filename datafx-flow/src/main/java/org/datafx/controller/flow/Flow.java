/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
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

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.datafx.controller.ViewConfiguration;
import org.datafx.controller.flow.action.*;
import org.datafx.controller.flow.context.ViewFlowContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * This class defines a flow. A flow is a map of different views that are linked.
 * A flow can define actions for each view or global actions for the complete flow.
 * The class provides a fluent API to create a flow with views and actions.
 *
 * @author Hendrik Ebbers
 */
public class Flow {

    private Class<?> startViewControllerClass;
    private Map<Class<?>, Map<String, FlowAction>> viewFlowMap;
    private Map<String, FlowAction> globalFlowMap;
    private ViewConfiguration viewConfiguration;

    /**
     * Creates a new Flow with the given controller for the start view and a
     * view configuration for all views.
     * The start view must be a view controller as specified in the DataFX-Controller API.
     * See <tt>FXMLController</tt> for more information
     *
     * @param startViewControllerClass Controller class of the start view
     * @param viewConfiguration        Configuration for all views of the flow
     * @see org.datafx.controller.FXMLController
     */
    public Flow(Class<?> startViewControllerClass, ViewConfiguration viewConfiguration) {
        this.startViewControllerClass = startViewControllerClass;
        globalFlowMap = new HashMap<>();
        viewFlowMap = new HashMap<>();
        this.viewConfiguration = viewConfiguration;
    }

    /**
     * Creates a new Flow with the given controller for the start view.
     * The start view must be a view controller as specified in the DataFX-Controller API.
     * See <tt>FXMLController</tt> for more information
     *
     * @param startViewControllerClass Controller class of the start view
     */
    public Flow(Class<?> startViewControllerClass) {
        this(startViewControllerClass, new ViewConfiguration());
    }

    /**
     * Returns the view configuration for all views of the flow
     *
     * @return the view configuration
     */
    public ViewConfiguration getViewConfiguration() {
        return viewConfiguration;
    }

    /**
     * Creates a handler that can be used to run the flow. The Flow class provides only the definition of a flow. To run a flow in the JavaFX scene graph a <tt>FlowHandler</tt> is needed
     *
     * @param flowContext The context for the flow
     * @return a flow handler to run the flow
     */
    public FlowHandler createHandler(ViewFlowContext flowContext) {
        return new FlowHandler(this, flowContext);
    }

    /**
     * Creates a handler that can be used to run the flow. The Flow class provides only the definition of a flow. To run a flow in the JavaFX scene graph a <tt>FlowHandler</tt> is needed
     *
     * @return a flow handler to run the flow
     */
    public FlowHandler createHandler() throws FlowException {
        return createHandler(new ViewFlowContext());
    }

    /**
     * Adds a global action to the flow. The action is registered by the given unique ID and can be called at runtime by using the id. A global action can be called from each view and from outside of the flow.
     *
     * @param actionId unique action id
     * @param action   the action
     * @return returns this flow (for the fluent API)
     */
    public Flow withGlobalAction(String actionId, FlowAction action) {
        addGlobalAction(actionId, action);
        return this;
    }

    /**
     * Adds a run action as a global action to the flow. Internally a <tt>FlowTaskAction</tt> will be created and added to the flow
     *
     * @param actionId    unique action id
     * @param actionClass a runnable that will be called whenever the action is called
     * @return returns this flow (for the fluent API)
     * @see FlowTaskAction
     */
    public Flow withGlobalRunAction(String actionId,
                                    Class<? extends Runnable> actionClass) {
        addGlobalAction(actionId, new FlowTaskAction(actionClass));
        return this;
    }

    /**
     * Adds a link action as a global action to the flow. Internally a <tt>FlowLink</tt> will be created and added to the flow
     *
     * @param actionId        unique action id
     * @param controllerClass the controller of the view that should be shown whenever the action will be called
     * @return returns this flow (for the fluent API)
     * @see FlowLink
     */
    public Flow withGlobalLink(String actionId, Class<?> controllerClass) {
        addGlobalAction(actionId, new FlowLink<>(controllerClass));
        return this;
    }

    public Flow withGlobalBackAction(String actionId) {
        addGlobalAction(actionId, new FlowBackAction());
        return this;
    }

    /**
     * Adds a action to the view of the given view controller. The action is registered by the given unique ID and can be called at runtime by using the id. the action can only be called when the view of the given controller is the active view of the flow
     *
     * @param controllerClass controller class for the view of the action
     * @param actionId        unique action id
     * @param action          the action
     * @return returns this flow (for the fluent API)
     */
    public Flow withAction(Class<?> controllerClass, String actionId,
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

    public Flow withTaskAction(Class<?> controllerClass, String actionId,
                               Class<? extends Runnable> actionClass) {
        addActionToView(controllerClass, actionId, new FlowTaskAction(
                actionClass));
        return this;
    }

    public Flow withGlobalCallMethodAction(String actionId,
                                           String actionMethodName) {
        addGlobalAction(actionId, new FlowMethodAction(actionMethodName));
        return this;
    }

    public Flow withCallMethodAction(Class<?> controllerClass, String actionId,
                                     String actionMethodName) {
        addActionToView(controllerClass, actionId, new FlowMethodAction(actionMethodName));
        return this;
    }

    public Flow withBackAction(Class<?> controllerClass, String actionId) {
        addActionToView(controllerClass, actionId, new FlowBackAction());
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

        for (Method method : newView
                .getViewContext().getController().getClass().getMethods()) {
            ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
            if (actionMethod != null) {
                newView.addAction(actionMethod.value(), new FlowMethodAction(method.getName()));
            }

        }
    }

    /**
     * Start the flow with a default handler and a default FlowContainer
     * This will start the flow using a DefaultFlowContainer, which returns a
     * StackPane.
     *
     * @return the <tt>Parent</tt> that will be used for rendering
     * @throws FlowException
     */
    public StackPane start() throws FlowException {
        return start(new DefaultFlowContainer());
    }

    public void startInStage(Stage stage) throws FlowException {
        stage.setScene(new Scene(start()));
        stage.show();
    }

    public Tab startInTab() throws FlowException {
        return startInTab(new DefaultFlowContainer());
    }

    public <T extends Node> Tab startInTab(FlowContainer<T> container) throws FlowException {
        return createHandler().startInTab(container);
    }

    /**
     * Start the flow with a default handler and a provided FlowContainer
     *
     * @param <T>
     * @param flowContainer the FlowContainer used to visualize this flow
     * @return the <tt>Parent</tt> that will be used for rendering
     * @throws FlowException
     */
    public <T extends Node> T start(FlowContainer<T> flowContainer) throws FlowException {
        createHandler().start(flowContainer);
        return flowContainer.getView();
    }
}
