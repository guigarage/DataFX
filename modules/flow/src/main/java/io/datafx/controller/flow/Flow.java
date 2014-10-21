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
package io.datafx.controller.flow;

import io.datafx.controller.flow.action.FlowMethodAction;
import io.datafx.controller.flow.action.FlowLink;
import io.datafx.controller.flow.action.FlowBackAction;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.FlowTaskAction;
import io.datafx.controller.flow.action.FlowAction;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.context.ViewMetadata;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;

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
     * See {@link io.datafx.controller.FXMLController} for more information
     *
     * @param startViewControllerClass Controller class of the start view
     * @param viewConfiguration Configuration for all views of the flow
     * @see io.datafx.controller.FXMLController
     */
    public Flow(Class<?> startViewControllerClass, ViewConfiguration viewConfiguration) {
        this.startViewControllerClass = startViewControllerClass;
        globalFlowMap = new HashMap<>();
        viewFlowMap = new HashMap<>();
        this.viewConfiguration = viewConfiguration;
    }

    /**
     * Creates a new Flow with the given controller for the start view.
     * The startViewControllerClass must be a view controller as specified 
     * in the DataFX-Controller API, which means it must be a class
     * annotated with {@link io.datafx.controller.FXMLController}.
     * See {@link io.datafx.controller.FXMLController} for more information.
     * Using this constructor will create a new {@link ViewConfiguration}. 
     *
     * @param startViewControllerClass Controller class of the start view
     * @see io.datafx.controller.FXMLController
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
     * Creates a handler that can be used to run the flow. The Flow class provides only the definition of a flow.
     * To run a flow in the JavaFX scene graph a{@link FlowHandler} is needed
     *
     * @param flowContext The context for the flow
     * @return a flow handler to run the flow
     */
    public FlowHandler createHandler(ViewFlowContext flowContext) {
        return new FlowHandler(this, flowContext);
    }

    /**
     * Creates a handler that can be used to run the flow. The Flow class provides only the definition of a flow.
     * To run a flow in the JavaFX scene graph a {@link FlowHandler} is needed
     *
     * @return a flow handler to run the flow
     */
    public FlowHandler createHandler()  {
        return createHandler(new ViewFlowContext());
    }

    /**
     * Adds a global action to the flow. The action is registered by the given unique ID and can be called at runtime
     * by using the id. A global action can be called from each view and from outside of the flow.
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
     * Adds a task action as a global action to the flow. Internally a {@link FlowTaskAction} will be created and
     * added to the flow. As you can read in the documentation of {@link FlowTaskAction} a instance of the given
     * {@code actionClass} will be created and executed whenever the action will be called.
     *
     * @param actionId    unique action id
     * @param actionClass class that defines the runnable that will be called whenever the action is called
     * @return returns this flow (for the fluent API)
     * @see FlowTaskAction
     */
    public Flow withGlobalTaskAction(String actionId,
                                     Class<? extends Runnable> actionClass) {
        addGlobalAction(actionId, new FlowTaskAction(actionClass));
        return this;
    }

    /**
     * Adds a task action as a global action to the flow. Internally a {@link FlowTaskAction} will be created and
     * added to the flow.
     * @param actionId unique action id
     * @param action a runnable that will be called whenever the action is called
     * @return returns this flow (for the fluent API)
     * @see FlowTaskAction
     */
    public Flow withGlobalTaskAction(String actionId,
                                     Runnable action) {
        addGlobalAction(actionId, new FlowTaskAction(action));
        return this;
    }

    /**
     * Adds a link action as a global action to the flow. Internally a {@link FlowLink} will be created and added
     * to the flow.
     * @param actionId        unique action id
     * @param controllerClass the controller of the view that should be shown whenever the action will be called
     * @return returns this flow (for the fluent API)
     * @see FlowLink
     */
    public Flow withGlobalLink(String actionId, Class<?> controllerClass) {
        addGlobalAction(actionId, new FlowLink<>(controllerClass));
        return this;
    }

    /**
     * Adds a global back action to the flow that navigates back to the last view of a flow. Internally a
     * {@link FlowBackAction} will be created and added to the flow.
     * @param actionId unique action id
     * @return returns this flow (for the fluent API)
     * @see FlowBackAction
     */
    public Flow withGlobalBackAction(String actionId) {
        addGlobalAction(actionId, new FlowBackAction());
        return this;
    }

    /**
     * Adds a action to the view of the given view controller. The action is registered by the given unique ID and can
     * be called at runtime by using the id. the action can only be called when the view of the given controller is the
     * active view of the flow
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

    /**
     * Adds a navigation action to the flow that will navigate from one view to another. The two controller classes that
     * must be passed as parameters defines the origin view and the destination. Internally a {@link FlowLink} will be
     * created and added to the flow.
     * @param fromControllerClass the controller class of the view that is the origin of the navigation
     * @param actionId unique action id
     * @param toControllerClass the controller class of the view that is the destination of the navigation
     * @return returns this flow (for the fluent API)
     * @see FlowLink
     */
    public Flow withLink(Class<?> fromControllerClass, String actionId,
                         Class<?> toControllerClass) {
        addActionToView(fromControllerClass, actionId, new FlowLink<>(
                toControllerClass));
        return this;
    }

    /**
     * Adds a task action to the defined view of the flow. Internally a {@link FlowTaskAction} will be created and
     * added to the flow. As you can read in the documentation of {@link FlowTaskAction} a instance of the given
     * {@code actionClass} will be created and executed whenever the action will be called.
     * @param controllerClass the controller class of the view to that the action will be registered
     * @param actionId unique action id
     * @param actionClass class that defines the runnable that will be called whenever the action is called
     * @return returns this flow (for the fluent API)
     * @see FlowTaskAction
     */
    public Flow withTaskAction(Class<?> controllerClass, String actionId,
                               Class<? extends Runnable> actionClass) {
        addActionToView(controllerClass, actionId, new FlowTaskAction(
                actionClass));
        return this;
    }

    /**
     * Adds a task action to the defined view of the flow. Internally a {@link FlowTaskAction} will be created and
     * added to the flow.
     * @param controllerClass the controller class of the view to that the action will be registered
     * @param actionId unique action id
     * @param action a runnable that will be called whenever the action is called
     * @return returns this flow (for the fluent API)
     * @see FlowTaskAction
     */
    public Flow withTaskAction(Class<?> controllerClass, String actionId,
                               Runnable action) {
        addActionToView(controllerClass, actionId, new FlowTaskAction(
                action));
        return this;
    }

    /**
     * Adds a method call action as a global action to the flow. Internally a {@link FlowMethodAction} will be created and
     * added to the flow. Whenever the action will be executed a method in the view controller will be called
     * @param actionId unique action id
     * @param actionMethodName name of the controller method that will be called
     * @return returns this flow (for the fluent API)
     * @see FlowMethodAction
     */
    public Flow withGlobalCallMethodAction(String actionId,
                                           String actionMethodName) {
        addGlobalAction(actionId, new FlowMethodAction(actionMethodName));
        return this;
    }

    /**
     *  Adds a method call action to the defined view of the flow. Internally a {@link FlowMethodAction} will be created and
     * added to the flow. Whenever the action will be executed a method in the view controller will be called
     * @param controllerClass the controller class of the view to that the action will be registered
     * @param actionId unique action id
     * @param actionMethodName name of the controller method that will be called
     * @return returns this flow (for the fluent API)
     * @see FlowMethodAction
     */
    public Flow withCallMethodAction(Class<?> controllerClass, String actionId,
                                     String actionMethodName) {
        addActionToView(controllerClass, actionId, new FlowMethodAction(actionMethodName));
        return this;
    }

    /**
     * Adds a back action to the defined view of the flow that navigates back to the last view of a flow. Internally a
     * {@link FlowBackAction} will be created and added to the flow.
     * @param controllerClass the controller class of the view to that the action will be registered
     * @param actionId unique action id
     * @return returns this flow (for the fluent API)
     * @see FlowBackAction
     */
    public Flow withBackAction(Class<?> controllerClass, String actionId) {
        addActionToView(controllerClass, actionId, new FlowBackAction());
        return this;
    }

    /**
     * Adds an action to the defined view of the flow.
     * @param controllerClass the controller class of the view to that the action will be registered
     * @param actionId unique action id
     * @param action the action that will be added
     * @return returns this flow (for the fluent API)
     * @see FlowAction
     */
    public Flow addActionToView(Class<?> controllerClass, String actionId,
                                    FlowAction action) {
        if (viewFlowMap.get(controllerClass) == null) {
            viewFlowMap.put(controllerClass, new HashMap<String, FlowAction>());
        }
        viewFlowMap.get(controllerClass).put(actionId, action);
        return this;
    }

    /**
     * Adds a global action to the flow.
     * @param actionId unique action id
     * @param action the action that will be added
     * @return returns this flow (for the fluent API)
     * @see FlowAction
     */
    public Flow addGlobalAction(String actionId, FlowAction action) {
        globalFlowMap.put(actionId, action);
        return this;
    }

    /**
     * Returns the action that is registered by the given unique id
     * @param actionId the id
     * @return the action that is registered by the given unique id
     */
    public FlowAction getGlobalActionById(String actionId) {
        return globalFlowMap.get(actionId);
    }

    /**
     * Returns the class of the view controller that is defined as the start view.
     * @return the class of the view controller that is defined as the start view
     */
    public Class<?> getStartViewControllerClass() {
        return startViewControllerClass;
    }

    /**
     * This methods adds all registered actions to the given flow view. This method is needed by DataFX and should
     * normally not called by a developer.
     * @param newView the view that shoudl be prepaired
     * @param <U> Class of the view controller
     */
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
     * @throws FlowException in case something goes wrong
     */
    public StackPane start() throws FlowException {
        return start(new DefaultFlowContainer());
    }

    /**
     * Starts the flow directly in a Stage. This method is useful if an application contains of one main flow. Because
     * this flow can contain several sub-flows this is the preferred way to create a DataFX based application. The title
     * of the Stage will be bound to the title of the flow metadata and will change whenever the flow title fill change.
     * This can happen if a view of the flow defines its own title by using the title attribute of the @FXMLController
     * annotation or the ViewMetadata of an view is changed in code.
     *
     * By using the method a flow based application can be created by only a few lines of code as shown in this example:
     * <code>
     * public class Example extends Application {
     *
     *   public static void main(String[] args) {
     *       launch(args);
     *   }
     *
     *   public void start(Stage primaryStage) throws Exception {
     *       new Flow(SimpleController.class).startInStage(primaryStage);
     *   }
     *}
     *</code>
     * @param stage The stage in that the flow should be displayed.
     * @throws FlowException If the flow can't be created or started
     */
    public void startInStage(Stage stage) throws FlowException {
        FlowHandler handler = createHandler();
        stage.setScene(new Scene(handler.start(new DefaultFlowContainer())));
        handler.getCurrentViewMetadata().addListener((e) -> {
            stage.titleProperty().unbind();
            ViewMetadata metadata = handler.getCurrentViewMetadata().get();
            if (metadata != null) {
                stage.titleProperty().bind(metadata.titleProperty());
            }
        });

        stage.titleProperty().unbind();
        ViewMetadata metadata = handler.getCurrentViewMetadata().get();
        if (metadata != null) {
            stage.titleProperty().bind(metadata.titleProperty());
        }

        stage.show();
    }

    public void startInPane(StackPane pane) throws FlowException {
        FlowHandler handler = createHandler();
        handler.startInPane(pane);
    }



    /**
     * Creates a {@link Tab} that contains a running instance of the flow. A {@link DefaultFlowContainer} is used as the flow
     * container.
     * @return the tab that contains the running flow.
     * @throws FlowException if the flow instance can't be created
     */
    public Tab startInTab() throws FlowException {
        return startInTab(new DefaultFlowContainer());
    }

    /**
     * Creates a {@link Tab} that contains a running instance of the flow.
     * @param container the flow container in that the flow instance should run
     * @param <T> node type of the flow container
     * @return the tab that contains the running flow.
     * @throws FlowException if the flow instance can't be created
     */
    public <T extends Node> Tab startInTab(FlowContainer<T> container) throws FlowException {
        return createHandler().startInTab(container);
    }

    /**
     * Start the flow with a default handler and a provided FlowContainer
     *
     * @param <T> node type of the flow container
     * @param flowContainer the FlowContainer used to visualize this flow
     * @return the <tt>Parent</tt> that will be used for rendering
     * @throws FlowException
     */
    public <T extends Node> T start(FlowContainer<T> flowContainer) throws FlowException {
        createHandler().start(flowContainer);
        return flowContainer.getView();
    }
}
