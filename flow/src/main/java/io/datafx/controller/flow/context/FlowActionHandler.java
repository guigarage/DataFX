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
package io.datafx.controller.flow.context;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.FlowAction;
import io.datafx.controller.flow.action.FlowLink;
import io.datafx.controller.flow.action.FlowTaskAction;
import io.datafx.controller.util.VetoException;
import io.datafx.core.ExceptionHandler;

import java.util.UUID;

public class FlowActionHandler {

    private FlowHandler handler;

    public FlowActionHandler(FlowHandler handler) {
        this.handler = handler;
    }

    public void handle(String actionId) throws VetoException, FlowException {
        this.handler.handle(actionId);
    }

    public void handleTask(Class<Runnable> task) throws VetoException, FlowException {
        handleAction(new FlowTaskAction(task));
    }

    public <T> void navigate(Class<T> controllerClass) throws VetoException, FlowException {
        handleAction(new FlowLink<>(controllerClass));
    }

    public <T> void navigateBack() throws VetoException, FlowException {
        this.handler.navigateBack();
    }

    private void handleAction(FlowAction action) throws VetoException, FlowException {
        this.handler.handle(action, UUID.randomUUID().toString());
    }

    public ExceptionHandler getExceptionHandler() {
        return handler.getExceptionHandler();
    }

    public void attachEventHandler(Node node, String actionId) {
        handler.attachEventHandler(node, actionId);
    }

    public void attachLinkEventHandler(MenuItem menuItem, Class<?> controllerClass) {
        handler.attachAction(menuItem, () -> {
            try {
                navigate(controllerClass);
            } catch (Exception e) {
                getExceptionHandler().setException(e);
            }
        });
    }

    public void attachLinkEventHandler(Node node, Class<?> controllerClass) {
        handler.attachAction(node, () -> {
            try {
                navigate(controllerClass);
            } catch (Exception e) {
                getExceptionHandler().setException(e);
            }
        });
    }

    public void attachBackEventHandler(MenuItem menuItem) {
        handler.attachBackEventHandler(menuItem);
    }

    public void attachBackEventHandler(Node node) {
        handler.attachBackEventHandler(node);
    }

    public void attachEventHandler(MenuItem menuItem, String actionId) {
        handler.attachEventHandler(menuItem, actionId);
    }
}
