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

import io.datafx.core.Assert;
import io.datafx.core.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The class defines a chain of processes.
 * All processes will be running in a queue and the result of a process will be
 * used as the input parameter for the next process.
 *
 * Here is a common usecase for the chain:
 * <tt>
 * ProcessChain.create().
 * addRunnableInPlatformThread(() -&gt; blockUI()).
 * addSupplierInExecutor(() -&gt; loadFromServer()).
 * addConsumerInPlatformThread(d -&gt; updateUI(d)).
 * onException(e -&gt; handleException(e)).
 * withFinal(() -&gt; unblockUI()).
 * run();
 *</tt>
 * In this example the {@link io.datafx.core.concurrent.ProcessChain} is used to wrap a background task
 * in some UI related tasks. By doing so, actions on the UI can be prohibited while the background action is running or
 * a loading animation can be shown on screen.
 * @param <T> Return value of the chain.
 */
public class ProcessChain<T> {

    private final List<ProcessDescription<?, ?>> processes;

    private final Executor executorService;

    private ExceptionHandler exceptionHandler;

    private Runnable finalRunnable;

    public ProcessChain() {
        this(ObservableExecutor.getDefaultInstance());
    }

    public ProcessChain(final Executor executorService) {
        this(executorService, null, null, null);
    }

    private ProcessChain(final Executor executorService, final List<ProcessDescription<?, ?>> processes, final ExceptionHandler exceptionHandler, final Runnable finalRunnable) {
        this.executorService = Assert.requireNonNull(executorService, "executorService");
        this.processes = new ArrayList<>();
        if (processes != null) {
            this.processes.addAll(processes);
        }
    }

    public static ProcessChain<Void> create() {
        return new ProcessChain<>();
    }

    public static ProcessChain<Void> create(final Executor executorService) {
        return new ProcessChain<>(executorService);
    }

    public <V> ProcessChain<V> addFunction(final Function<T, V> function, final ThreadType type) {
        return addProcessDescription(new ProcessDescription<T, V>(function, type));
    }

    public <V> ProcessChain<V> addProcessDescription(final ProcessDescription<T, V> processDescription) {
        processes.add(processDescription);
        return new ProcessChain<V>(executorService, processes, exceptionHandler, finalRunnable);
    }

    public <V> ProcessChain<V> addFunctionInPlatformThread(final Function<T, V> function) {
        return addFunction(function, ThreadType.PLATFORM);
    }

    public <V> ProcessChain<V> addFunctionInExecutor(final Function<T, V> function) {
        return addFunction(function, ThreadType.EXECUTOR);
    }

    public ProcessChain<Void> addRunnable(final Runnable runnable, final ThreadType type) {
        Assert.requireNonNull(runnable, "runnable");
        return addFunction((Function<T, Void>) (e) -> {
            runnable.run();
            return null;
        }, type);
    }

    public ProcessChain<Void> addRunnableInPlatformThread(final Runnable runnable) {
        return addRunnable(runnable, ThreadType.PLATFORM);
    }

    public ProcessChain<Void> addRunnableInExecutor(final Runnable runnable) {
        return addRunnable(runnable, ThreadType.EXECUTOR);
    }

    public ProcessChain<Void> addConsumer(final Consumer<T> consumer, final ThreadType type) {
        Assert.requireNonNull(consumer, "consumer");
        return addFunction((Function<T, Void>) (e) -> {
            consumer.accept(e);
            return null;
        }, type);
    }

    public ProcessChain<Void> addConsumerInPlatformThread(final Consumer<T> consumer) {
        return addConsumer(consumer, ThreadType.PLATFORM);
    }

    public ProcessChain<Void> addConsumerInExecutor(final Consumer<T> consumer) {
        return addConsumer(consumer, ThreadType.EXECUTOR);
    }

    public <V> ProcessChain<V> addSupplierInPlatformThread(final Supplier<V> supplier) {
        return addSupplier(supplier, ThreadType.PLATFORM);
    }

    public <V> ProcessChain<V> addSupplierInExecutor(final Supplier<V> supplier) {
        return addSupplier(supplier, ThreadType.EXECUTOR);
    }

    public <V> ProcessChain<V> addSupplier(final Supplier<V> supplier, final ThreadType type) {
        Assert.requireNonNull(supplier, "supplier");
        return addFunction((Function<T, V>) (e) -> {
            return supplier.get();
        }, type);
    }

    public <V> ProcessChain<List<V>> addPublishingTask(final Supplier<List<V>> supplier, final Consumer<Publisher<V>> consumer) {
        Assert.requireNonNull(supplier, "supplier");
        Assert.requireNonNull(consumer, "consumer");
        return addFunction((Function<T, List<V>>) (e) -> {
            List<V> list = supplier.get();
            Publisher<V> publisher = p -> {
                try {
                    ConcurrentUtils.runAndWait(() -> list.addAll(Arrays.asList(p)));
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            };
            consumer.accept(publisher);
            return list;
        }, ThreadType.EXECUTOR);
    }

    public <V> ProcessChain<List<V>> addPublishingTask(final List<V> list, final Consumer<Publisher<V>> consumer) {
        return addPublishingTask(() -> list, consumer);
    }

    public <V> ProcessChain<List<V>> addPublishingTask(final Consumer<Publisher<V>> consumer) {
        return addPublishingTask(() -> FXCollections.<V>observableArrayList(), consumer);
    }

    public ProcessChain<T> onException(final Consumer<Throwable> consumer) {
        Assert.requireNonNull(consumer, "consumer");
        this.exceptionHandler = new ExceptionHandler();
        exceptionHandler.exceptionProperty().addListener(e -> consumer.accept(exceptionHandler.getException()));
        return this;
    }

    public ProcessChain<T> onException(final ExceptionHandler handler) {
        this.exceptionHandler = handler;
        return this;
    }

    public ProcessChain<T> withFinal(final Runnable finalRunnable) {
        this.finalRunnable = finalRunnable;
        return this;
    }

    public <V> ProcessChain<V> waitFor(final Worker<V> worker) {
        return addSupplierInExecutor(() -> {
            try {
                return ConcurrentUtils.waitFor(worker);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <U, V> V execute(final U inputParameter, final ProcessDescription<U, V> processDescription) throws InterruptedException, ExecutionException {
        Assert.requireNonNull(processDescription, "processDescription");
        if (processDescription.getThreadType().equals(ThreadType.EXECUTOR)) {
            return processDescription.getFunction().apply(inputParameter);
        } else {
            return ConcurrentUtils.runCallableAndWait(() -> processDescription.getFunction().apply(inputParameter));
        }
    }

    public Task<T> repeatInfinite() {
        return repeat(Integer.MAX_VALUE);
    }

    public Task<T> repeatInfinite(final Duration pauseTime) {
        return repeat(Integer.MAX_VALUE, pauseTime);
    }

    public Task<T> repeat(final int count) {
        return repeat(count, Duration.ZERO);
    }

    public Task<T> repeat(final int count, final Duration pauseTime) {
        Assert.requireNonNull(pauseTime, "pauseTime");
        Task<T> task = new Task<T>() {

            @Override
            protected T call() throws Exception {
                try {
                    Object lastResult = null;
                    if (count == Integer.MAX_VALUE) {
                        while (true) {
                            lastResult = null;
                            for (ProcessDescription<?, ?> processDescription : processes) {
                                lastResult = execute(lastResult, (ProcessDescription<Object, ?>) processDescription);
                            }
                            Thread.sleep((long) pauseTime.toMillis());
                        }
                    } else {
                        for (int i = 0; i < count; i++) {
                            lastResult = null;
                            for (ProcessDescription<?, ?> processDescription : processes) {
                                lastResult = execute(lastResult, (ProcessDescription<Object, ?>) processDescription);
                            }
                            Thread.sleep((long) pauseTime.toMillis());
                        }
                    }
                    return (T) lastResult;
                } catch (Exception e) {
                    if (exceptionHandler != null) {
                        ConcurrentUtils.runAndWait(() -> exceptionHandler.setException(e));
                    }
                    throw e;
                } finally {
                    if (finalRunnable != null) {
                        ConcurrentUtils.runAndWait(() -> finalRunnable.run());
                    }
                }
            }

        };
        executorService.execute(task);
        return task;
    }

    public Task<T> run() {
        return repeat(1);
    }
}
