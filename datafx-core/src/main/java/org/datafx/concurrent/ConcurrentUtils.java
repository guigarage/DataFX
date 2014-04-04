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
package org.datafx.concurrent;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/**
 *  Utility class for concurrency issues in JavaFX
 *
 *  @author Hendrik Ebbers
 */
public class ConcurrentUtils {

    private ConcurrentUtils() {}

    /**
     * Runs the given <tt>Runnable</tt> on the JavaFX Application Thread. The method blocks until the <tt>Runnable</tt> is executed completely.
     * @param runnable the runnable that will be executed on the JavaFX Application Thread
     * @throws InterruptedException  if the JavaFX Application Thread was interrupted while waiting
     * @throws ExecutionException if the call of the run method of the <tt>Runnable</tt> threw an exception
     */
	public static void runAndWait(Runnable runnable)
			throws InterruptedException, ExecutionException {
		FutureTask<Void> future = new FutureTask<>(runnable, null);
		Platform.runLater(future);
		future.get();
	}

    /**
     * Runs the given <tt>Callable</tt> on the JavaFX Application Thread. The method blocks until the <tt>Callable</tt> is executed completely. The return value of the call() method of the callable will be returned
     * @param callable  the callable that will be executed on the JavaFX Application Thread
     * @param <T>  return type of the callable
     * @return  return value of the executed call() method of the <tt>Callable</tt>
     * @throws InterruptedException   if the JavaFX Application Thread was interrupted while waiting
     * @throws ExecutionException  if the call of the run method of the <tt>Callable</tt> threw an exception
     */
	public static <T> T runCallableAndWait(Callable<T> callable)
			throws InterruptedException, ExecutionException {
		FutureTask<T> future = new FutureTask<T>(callable);
		Platform.runLater(future);
		return future.get();
	}

    public static DataFxService<Void> createService(Runnable runnable) {
        return createService(new RunnableBasedDataFxTask(runnable));
    }

    public static <T> DataFxService<T> createService(Callable<T> callable) {
        return createService(new CallableBasedDataFxTask<T>(callable));
    }

    public static <T> DataFxService<T> createService(Task<T> task) {
        return new DataFxService<T>() {
            @Override protected Task<T> createTask() {
                return task;
            }
        };
    }

    public static <T> Worker<T> executeService(Executor executor, Service<T> service) {
        if (executor != null && executor instanceof ObservableExecutor) {
            return ((ObservableExecutor) executor).submit(service);
        } else {
            if (executor != null) {
                service.setExecutor(executor);
            }
            service.start();
            return service;
        }
    }

}
