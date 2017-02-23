/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
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

import io.datafx.core.Assert;
import io.datafx.core.ExceptionHandler;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * A Executor that task can be observed. All current running and scheduled task
 * can be accessed by by a the
 * <code>currentServices</code> ListProperty. Each background task (
 * <code>Callable</code>,
 * <code>Runnable</code>, etc.) is wrapped in a
 * <code>Service</code>
 *
 * The
 * <code>ObservableExecutor</code> can be used as a wrapper around a default
 * executor.
 *
 * @author Hendrik Ebbers
 *
 */
public class ObservableExecutor implements Executor {

    private static ObservableExecutor defaultInstance;

    private final Executor executor;

    private final ListProperty<Service<?>> currentServices;

    private final ExceptionHandler exceptionHandler;

    /**
     * Creates a new ObservableExecutor that uses a cached thread pool to handle
     * all commited tasks.
     */
    public ObservableExecutor() {
        this(ThreadPoolExecutorFactory.getThreadPoolExecutor());
    }

    public ObservableExecutor(final Executor executor) {
        this(executor, ExceptionHandler.getDefaultInstance());
    }

    public ObservableExecutor(final ExceptionHandler exceptionHandler) {
        this(ThreadPoolExecutorFactory.getThreadPoolExecutor(), exceptionHandler);
    }

    /**
     * Creates a new ObservableExecutor that uses the given executor to handle
     * all commited tasks.
     *
     * @param executor wrapped executor. It will be used to handle all task that
     * are commited to this executor.
     * @param exceptionHandler the exceptionhandler
     */
    public ObservableExecutor(final Executor executor, final ExceptionHandler exceptionHandler) {
        this.executor = Assert.requireNonNull(executor, "executor");
        this.exceptionHandler = Assert.requireNonNull(exceptionHandler, "exceptionHandler");
        currentServices = new SimpleListProperty<>(
                FXCollections.<Service<?>>observableArrayList());
        currentServices.addListener((ListChangeListener<? super Service<?>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<? extends Service<?>> newServices = change
                            .getAddedSubList();
                    for (final Service<?> service : newServices) {
                        service.stateProperty().addListener(
                                new ChangeListener<State>() {
                                    @Override
                                    public void changed(
                                            ObservableValue<? extends State> observableValue,
                                            State oldSate, State newState) {
                                        if (newState != null
                                                && (newState
                                                .equals(State.CANCELLED)
                                                || newState
                                                .equals(State.SUCCEEDED) || newState
                                                .equals(State.FAILED))) {
                                            currentServices.remove(service);
                                        }
                                    }
                                });
                        State currentState = service.getState();
                        if (currentState != null
                                && (currentState.equals(State.CANCELLED)
                                || currentState
                                .equals(State.SUCCEEDED) || currentState
                                .equals(State.FAILED))) {
                            currentServices.remove(service);
                        }
                    }
                }
            }
        });
    }

    /**
     * Returns the a ListProperty that contains all currently scheduled and
     * running tasks.
     *
     * @return a ListProperty that contains all currently scheduled and running
     * tasks.
     */
    public ReadOnlyListProperty<Service<?>> currentServicesProperty() {
        return currentServices;
    }

    /**
     * Execute the given service at some time in the future.
     *
     * @param <T> The return type for the service
     * @param service the service.
     * @return a worker that can be used to check the state of the service and
     * receive the result of it.
     */
    public <T> Worker<T> submit(final Service<T> service) {
        Assert.requireNonNull(service, "service");
        service.setExecutor(executor);
        currentServices.add(service);
        if (exceptionHandler != null) {
            exceptionHandler.observeWorker(service);
        }
        service.start();
        return service;
    }

    /**
     * Execute the given task at some time in the future.
     *
     * @param <T> the return type for the task
     * @param task the task.
     * @return a worker that can be used to check the state of the task and
     * receive the result of it.
     */
    public <T> Worker<T> submit(final Task<T> task) {
        return submit(ConcurrentUtils.createService(task));
    }

    /**
     * Execute the given callable at some time in the future.
     *
     * @param <T> the return type for the worker
     * @param callable the callable. If a <code>DataFxCallable</code> is used
     * here a <code>TaskStateHandler</code> will be injected.
     * @return a worker that can be used to check the state of the callable and
     * receive the result of it.
     */
    public <T> Worker<T> submit(final Callable<T> callable) {
        return submit(ConcurrentUtils.createService(callable));
    }

    /**
     * Execute the given runnable at some time in the future.
     *
     * @param runnable the runnable. If a <code>DataFxRunnable</code> is used
     * here a <code>TaskStateHandler</code> will be injected.
     * @return a worker that can be used to check the state of the runnable
     */
    public Worker<Void> submit(final Runnable runnable) {
        return submit(ConcurrentUtils.createService(runnable));
    }

    /**
     * Execute the given runnable at some time in the future.
     *
     * @param runnable the runnable. If a <code>DataFxRunnable</code> is used
     * here a <code>TaskStateHandler</code> will be injected.
     */
    public void execute(final Runnable runnable) {
        submit(runnable);
    }

    /**
     * Creates a new <tt>ProcessChain</tt> that uses this executor as the executor for all background tasks.
     * @return a new <tt>ProcessChain</tt>
     *
     * @see ProcessChain
     */
    public ProcessChain<Void> createProcessChain() {
        return new ProcessChain<>(this);
    }

    /**
     * Returns the default executor. This one uses an internal cached thread pool.
     * @return the default executor
     */
    public static synchronized ObservableExecutor getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new ObservableExecutor();
        }
        return defaultInstance;
    }
}
