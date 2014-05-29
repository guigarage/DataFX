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
package org.datafx.controller.flow.action;

import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.FlowView;
import org.datafx.controller.FxmlLoadException;

/**
 * A {@Link FlowAction} implementation that navigates to a different view in a flow.
 * @param <T> class of the controller of the target view
 */
public class FlowLink<T> implements FlowAction {

    private Class<T> controllerClass;

    private boolean addOldToHistory;

    /**
     * Default constructor of the class
     * @param controllerClass controller class of the target view
     */
    public FlowLink(Class<T> controllerClass) {
        this(controllerClass, true);
    }

    /**
     * This Constructor is needed for DataFX when calling a back action. In this case the navigation should not be added
     * to the navigation history. Normally a developer should not use this constructor.
     * @param controllerClass controller class of the target view
     * @param addOldToHistory defines if the old view should be added to the view history of the flow
     */
    public FlowLink(Class<T> controllerClass, boolean addOldToHistory) {
        this.controllerClass = controllerClass;
        this.addOldToHistory = addOldToHistory;
    }
	
	@Override
	public void handle(FlowHandler flowHandler, String actionId)
			throws FlowException {
		try {
			ViewContext<T> viewContext = ViewFactory.getInstance().createByController(controllerClass, null, flowHandler.getViewConfiguration(), flowHandler.getFlowContext());
            flowHandler.setNewView(new FlowView<T>(viewContext), addOldToHistory);
        } catch (FxmlLoadException e) {
            throw new FlowException(e);
        }
	}

}
