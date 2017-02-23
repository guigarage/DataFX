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

import java.util.concurrent.Executor;

/**
 * Implementation of a {@link FlowAction} that calls the given {@link Runnable} whenever the action is triggered.
 * The {@link Runnable} will be called in the JavaFX Application Thread.
 */
public class FlowTaskAction extends AbstractFlowTaskAction {

    private Executor executorService;

    /**
     * Defines a new {@link FlowTaskAction} instance that task is defined by a class that extends
     * {@link Runnable}. Whenever a action is triggered a new instance of the given class will be created. Therefore the
     * class needs a default constructor.  All injection that is working in a controller class will work in the given
     * class that defines the task, too.
     * @param runnableClass the class that defines the task
     */
    public FlowTaskAction(Class<? extends Runnable> runnableClass) {
        super(runnableClass);
    }

    /**
     * Defines a new {@link FlowTaskAction} instance that task is defined by a {@link Runnable} instance.
     * @param runnable defines the task and will be called whenever the action is triggered.
     */
    public FlowTaskAction(Runnable runnable) {
        super(runnable);
    }


    @Override
    protected void execute(Runnable r) throws Exception {
        r.run();
    }

}