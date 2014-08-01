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

import java.util.function.Function;

/**
 * This class defines the description of a process. 
 * The process can be used as part of a process chain
 * @param <V> Input parameter type of the progress
 * @param <T> Return parameter type of the progress
 *
 * @see org.datafx.util.concurrent.ProcessChain
 */
public class ProcessDescription<V, T> {

    private Function<V, T> function;

    private ThreadType threadType;

    /**
     * Creates a new <tt>ProcessDescription</tt> with the given function and thread type. 
     * The functions defines what this process will do at runtime. 
     * The function will be called when the process will be executed. 
     * The thread type defines if the process should be executed om the 
     * JavaFX Platform Thread or on a background thread.
     * 
     * @param function  defines what this process will do
     * @param threadType  defines if the process should be executed om the JavaFX Platform Thread or on a background thread
     */
    public ProcessDescription(Function<V, T> function, ThreadType threadType) {
        this.function = function;
        this.threadType = threadType;
    }

    /**
     * Returns the function of the process.
     * 
     * @return the function
     */
    public Function<V, T> getFunction() {
        return function;
    }

    /**
     * Returns the thread type of the process
     * 
     * @return  the thread type
     */
    public ThreadType getThreadType() {
        return threadType;
    }
}