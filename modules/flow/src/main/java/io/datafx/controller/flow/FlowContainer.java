/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of DataFX, the website
 * javafxdata.org, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.controller.flow;

import javafx.scene.Node;
import io.datafx.controller.context.ViewContext;

/**
 * A Flow needs a JavaFX <tt>Parent</tt> node in that the current view of the
 * flow will be added as a child. This interface defines a general class that
 * will be used internally to set the new ViewMetadata for a Flow. A implementation
 * should add the <tt>Node</tt> of the view to the scene graph.
 *
 * @author Hendrik Ebbers
 * @param <T> Controller type of the current view
 * @see io.datafx.controller.flow.container.DefaultFlowContainer
 */
public interface FlowContainer<T extends Node> {

    /**
     * The given view will be the new view of the flow. Implementations should
     * add the he <tt>Node</tt> of the view to the scene graph and remove the
     * old view.
     *
     * @param context Context of the current view
     */
    public <U> void setViewContext(ViewContext<U> context);

    /**
     * Return the <tt>Parent</tt> node that is rendering the flow.
     *
     * @return the <tt>Parent</tt> node that is rendering the flow
     */
    public T getView();

}
