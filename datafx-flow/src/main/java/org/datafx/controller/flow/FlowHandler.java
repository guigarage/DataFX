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

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import org.datafx.controller.FxmlLoadException;
import org.datafx.controller.ViewConfiguration;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.ViewMetadata;
import org.datafx.controller.flow.action.FlowAction;
import org.datafx.controller.flow.action.FlowLink;
import org.datafx.controller.flow.container.DefaultFlowContainer;
import org.datafx.controller.flow.context.FlowActionHandler;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.event.*;
import org.datafx.controller.util.Veto;
import org.datafx.controller.util.VetoException;
import org.datafx.controller.util.VetoHandler;
import org.datafx.util.ExceptionHandler;

import java.util.ResourceBundle;
import java.util.UUID;

public class FlowHandler {


    private final ObservableList<ViewHistoryDefinition<?>> controllerHistory;
    private ReadOnlyObjectWrapper<FlowView<?>> currentViewWrapper;
    private ReadOnlyObjectWrapper<FlowContainer> containerWrapper;
    private ReadOnlyObjectWrapper<ViewFlowContext> flowContextWrapper;
    private ReadOnlyObjectWrapper<Flow> flowWrapper;
    private SimpleObjectProperty<BeforeFlowActionHandler> beforeFlowActionHandler;
    private SimpleObjectProperty<AfterFlowActionHandler> afterFlowActionHandler;
    private SimpleObjectProperty<VetoableBeforeFlowActionHandler> vetoableBeforeFlowActionHandler;
    private SimpleObjectProperty<VetoHandler> vetoHandler;
    private ViewConfiguration viewConfiguration;
    private ExceptionHandler exceptionHandler;

    private ReadOnlyObjectWrapper<ViewMetadata> currentViewMetadataWrapper;

    public FlowHandler(Flow flow, ViewFlowContext flowContext) {
        this(flow, flowContext, new ViewConfiguration());
    }

    public FlowHandler(Flow flow, ViewFlowContext flowContext, ViewConfiguration viewConfiguration) {
        this(flow, flowContext, viewConfiguration, ExceptionHandler.getDefaultInstance());
    }

    public FlowHandler(Flow flow, ViewFlowContext flowContext, ViewConfiguration viewConfiguration, ExceptionHandler exceptionHandler) {
        this.flowWrapper = new ReadOnlyObjectWrapper<>(flow);
        this.viewConfiguration = viewConfiguration;
        this.exceptionHandler = exceptionHandler;
        controllerHistory = FXCollections.observableArrayList();
        currentViewWrapper = new ReadOnlyObjectWrapper<>();
        containerWrapper = new ReadOnlyObjectWrapper<>();
        flowContextWrapper = new ReadOnlyObjectWrapper<>(flowContext);
        currentViewMetadataWrapper = new ReadOnlyObjectWrapper<>();
        flowContextWrapper.get().currentViewContextProperty().addListener((e) -> currentViewMetadataWrapper.set(flowContextWrapper.get().getCurrentViewContext().getMetadata()));
        flowContextWrapper.get().register(new FlowActionHandler(this));
    }

    public StackPane start() throws FlowException {
        return start(new DefaultFlowContainer());
    }

    public Tab startInTab() throws FlowException {
        return startInTab(new DefaultFlowContainer());
    }

    public <T extends Node> Tab startInTab(FlowContainer<T> container) throws FlowException {
        Tab tab = new Tab();

        getCurrentViewMetadata().addListener((e) -> {
            tab.textProperty().unbind();
            tab.graphicProperty().unbind();
            tab.textProperty().bind(getCurrentViewMetadata().get().titleProperty());
            tab.graphicProperty().bind(getCurrentViewMetadata().get().graphicsProperty());
        });

        tab.setOnClosed(e -> {
            try {
                destroy();
            } catch (Exception exception) {
                exceptionHandler.setException(exception);
            }
        });
        tab.setContent(start(container));
        return tab;
    }

    public void destroy() {
        //TODO
    }

    public <T extends Node> T start(FlowContainer<T> container) throws FlowException {
        containerWrapper.set(container);
        flowContextWrapper.get().register(this);
        if (viewConfiguration != null) {
            flowContextWrapper.get().register(ResourceBundle.class.toString(), viewConfiguration.getResources());
        }
        try {
            FlowView<?> startView = new FlowView(ViewFactory.getInstance().createByController(flowWrapper.get().getStartViewControllerClass(), null, getViewConfiguration(), flowContextWrapper.get()));
            setNewView(startView, false);
        } catch (FxmlLoadException e) {
            throw new FlowException(e);
        }
        return container.getView();
    }

    public ViewConfiguration getViewConfiguration() {
        return viewConfiguration;
    }

    public void handle(String actionId) throws VetoException, FlowException {
        FlowAction action = null;
        if (getCurrentView() != null) {
            action = getCurrentView().getActionById(actionId);
        }
        if (action == null) {
            action = flowWrapper.get().getGlobalActionById(actionId);
        }
        handle(action, actionId);
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public ViewFlowContext getFlowContext() {
        return flowContextWrapper.get();
    }

    public ReadOnlyObjectProperty<ViewMetadata> getCurrentViewMetadata() {
        return currentViewMetadataWrapper.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<ViewFlowContext> getFlowContextProperty() {
        return flowContextWrapper.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<FlowView<?>> getCurrentViewProperty() {
        return currentViewWrapper.getReadOnlyProperty();
    }


    public ReadOnlyObjectProperty<FlowContainer> getContainerProperty() {
        return containerWrapper.getReadOnlyProperty();
    }

    public FlowView<?> getCurrentView() {
        return currentViewWrapper.get();
    }

    public ViewContext<?> getCurrentViewContext() {
        return getCurrentView().getViewContext();
    }

    /**
     * Returns the controller class of the current visible
     * @return the view controller class
     */
    public Class<?> getCurrentViewControllerClass() {
        return getCurrentViewContext().getController().getClass();
    }

    public void handle(FlowAction action, String actionId) throws FlowException, VetoException {

        if (beforeFlowActionHandler != null && beforeFlowActionHandler.getValue() != null) {
            beforeFlowActionHandler.getValue().handle(new BeforeFlowActionEvent(actionId, action, flowContextWrapper.get()));
        }

        if (vetoableBeforeFlowActionHandler != null && vetoableBeforeFlowActionHandler.getValue() != null) {
            try {
                vetoableBeforeFlowActionHandler.getValue().handle(new BeforeFlowActionEvent(actionId, action, flowContextWrapper.get()));
            } catch (Veto veto) {
                if (vetoHandler != null && vetoHandler.getValue() != null) {
                    vetoHandler.get().onVeto(veto);
                }
                throw new VetoException(veto);
            }
        }

        action.handle(this, actionId);
        if (afterFlowActionHandler != null && afterFlowActionHandler.getValue() != null) {
            afterFlowActionHandler.getValue().handle(new AfterFlowActionEvent(actionId, action, flowContextWrapper.get()));
        }
    }

    public <U> ViewContext<U> setNewView(FlowView<U> newView, boolean addOldToHistory)
            throws FlowException {
        if (getCurrentView() != null && addOldToHistory) {
            ViewHistoryDefinition<?> historyDefinition = new ViewHistoryDefinition(getCurrentView().getViewContext().getController().getClass(), "", null);
            controllerHistory.add(0, historyDefinition);
        }
        flowWrapper.get().addActionsToView(newView);

        FlowView<?> oldView = getCurrentView();

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
        currentViewWrapper.set(newView);
        flowContextWrapper.get().setCurrentViewContext(getCurrentView().getViewContext());
        containerWrapper.get().setViewContext(getCurrentView().getViewContext());
        return newView.getViewContext();
    }

    public void navigateBack() throws VetoException, FlowException {
        navigateToHistoryIndex(0);
    }

    public ObservableList<ViewHistoryDefinition<?>> getControllerHistory() {
        return FXCollections.unmodifiableObservableList(controllerHistory);
    }

    public void navigateToHistoryIndex(int index) throws VetoException, FlowException {
        handle(new FlowLink(controllerHistory.remove(index).getControllerClass(), false), "backAction-" + UUID.randomUUID().toString());
    }

    /**
     * Navigate to the view that is defined by the given controller class
     * @param controllerClass the controller class of the view
     * @throws VetoException
     * @throws FlowException
     */
    public void navigateTo(Class<?> controllerClass) throws VetoException, FlowException {
        handle(new FlowLink(controllerClass), "navigateAction-" + UUID.randomUUID().toString());
    }

    public void attachAction(Node node, Runnable action) {
        if (node instanceof ButtonBase) {
            ((ButtonBase) node).setOnAction((e) -> action.run());
        } else {
            node.setOnMouseClicked((ev) -> {
                if (ev.getClickCount() > 1) {
                    action.run();
                }
            });
        }
    }


    public void attachAction(MenuItem menuItem, Runnable action) {
        menuItem.setOnAction((e) -> action.run());
    }

    public void attachEventHandler(Node node, String actionId) {
        if (node instanceof ButtonBase) {
            ((ButtonBase) node).setOnAction((e) -> handleActionWithExceptionHandler(actionId));
        } else {
            node.setOnMouseClicked((e) -> {
                if (e.getClickCount() > 1) {
                    handleActionWithExceptionHandler(actionId);
                }
            });
        }
    }

    public void attachBackEventHandler(MenuItem menuItem) {
        menuItem.setOnAction((e) -> handleBackActionWithExceptionHandler());
    }

    public void attachBackEventHandler(Node node) {
        if (node instanceof ButtonBase) {
            ((ButtonBase) node).setOnAction((e) -> handleBackActionWithExceptionHandler());
        } else {
            node.setOnMouseClicked((e) -> {
                if (e.getClickCount() > 1) {
                    handleBackActionWithExceptionHandler();
                }
            });
        }
    }

    public void attachEventHandler(MenuItem menuItem, String actionId) {
        menuItem.setOnAction((e) -> handleActionWithExceptionHandler(actionId));
    }

    private void handleActionWithExceptionHandler(String id) {
        try {
            handle(id);
        } catch (VetoException | FlowException e) {
            getExceptionHandler().setException(e);
        }
    }

    private void handleBackActionWithExceptionHandler() {
        try {
            navigateBack();
        } catch (VetoException | FlowException e) {
            getExceptionHandler().setException(e);
        }
    }
}
