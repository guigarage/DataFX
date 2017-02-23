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
 * DISCLAIMED. IN NO EVENT SHALL DataFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.core.concurrent;
/**
* Helper class that can be used to easily create a JavaFX service from a runnable. 
* The class extends the {@link javafx.concurrent.Task} class. 
* By doing so it can be used as a basic JavaFX task. 
* Instances of the class internally hold a Runnable that will be executed. 
* By doing so developers doesn't need to create an implementation of the Task
* class if they want to execute a simple runnable in JavaFX or DataFX context. 
* Another benefit is the use / support of Lambdas. 
* The Task class isn't a functional interface and therefore you can't use Lambdas. 
* By using a Runnable and
* wrap it in a RunnableBasedDataFxTask instance a developer can use Lambda expressions.
* <p>
* If the internal Runnable is a DataFxRunnable the TaskStateHandler class will 
* be supported and a handler will be injected in the DataFxRunnable instance. 
* Therefore developers can define title, message, ... for the instance.
*/


public class RunnableBasedDataFxTask extends DataFxTask<Void> {

    private Runnable runnable;

    public RunnableBasedDataFxTask(Runnable runnable) {
        this.runnable = runnable;
        if (this.runnable instanceof DataFxRunnable) {
            ((DataFxRunnable) this.runnable).injectStateHandler(this);
        }
    }

    @Override public Void call() throws Exception {
        runnable.run();
        return null;
    }
}
