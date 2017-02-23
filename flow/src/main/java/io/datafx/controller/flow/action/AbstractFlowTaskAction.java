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
package io.datafx.controller.flow.action;

import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.core.Assert;

/**
 * Basic class for {@link FlowAction} implementations that execute a custom task. In this case a task is always defined
 * as a {@link Runnable}.
 */
public abstract class AbstractFlowTaskAction implements FlowAction {

    private final TaskFactory runnableFactory;

    private Class<? extends Runnable> runnableClass;

    /**
     * Defines a new {@link AbstractFlowTaskAction} instance that task is defined by a class that extends
     * {@link Runnable}. Whenever a action is triggered a new instance of the given class will be created. Therefore the
     * class needs a default constructor.  All injection that is working in a controller class will work in the given
     * class that defines the task, too.
     * @param runnableClass the class that defines the task
     */
    public AbstractFlowTaskAction(final Class<? extends Runnable> runnableClass) {
        this((f) -> f.getCurrentViewContext().getResolver().createInstanceWithInjections(runnableClass));
    }

    /**
     * Defines a new {@link AbstractFlowTaskAction} instance that task is defined by a {@link Runnable} instance.
     * @param runnable defines the task and will be called whenever the action is triggered.
     */
    public AbstractFlowTaskAction(final Runnable runnable) {
        this((f) -> runnable);
    }

    public AbstractFlowTaskAction(final TaskFactory runnableFactory) {
        this.runnableFactory = Assert.requireNonNull(runnableFactory, "runnableFactory");
    }

    @Override
    public void handle(final FlowHandler flowHandler, final String actionId)
            throws FlowException {
        try {
            Runnable runnable = runnableFactory.create(flowHandler);
            execute(runnable);
        } catch (Exception e) {
            throw new FlowException(e);
        }
    }

    /**
     * Executes the defined {@link Runnable}
     * @param r the runnable
     * @throws Exception if the runnable can't be executed
     */
    protected abstract void execute(Runnable r) throws Exception;

    @FunctionalInterface
    public interface TaskFactory {

        Runnable create(FlowHandler flowHandler) throws Exception;
    }

}
