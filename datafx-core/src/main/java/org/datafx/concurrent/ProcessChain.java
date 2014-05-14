package org.datafx.concurrent;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *  The class defines a chain of processes. All processes will be running in a queue and the result of a process will be used as the input parameter for the next process. A process can run
 * @param <T>  Return value of the chain.
 *
 * @author Hendrik Ebbers
 */
public class ProcessChain<T> {

    private List<ProcessDescription<?, ?>> processes;
    private Executor executorService;

    public ProcessChain() {
        this(Executors.newCachedThreadPool(), null);
    }

    public ProcessChain(Executor executorService) {
        this(executorService, null);
    }

    private ProcessChain(Executor executorService, List<ProcessDescription<?, ?>> processes) {
        this.executorService = executorService;
        this.processes = new ArrayList<>();
        if (processes != null) {
            this.processes.addAll(processes);
        }
    }

    public static ProcessChain<Void> create() {
        return new ProcessChain<>();
    }

    public static ProcessChain<Void> create(Executor executorService) {
        return new ProcessChain<>(executorService);
    }

    public <V> ProcessChain<V> addFunction(Function<T, V> function, ThreadType type) {
        processes.add(new ProcessDescription<T, V>(function, type));
        return new ProcessChain<V>(executorService, processes);
    }

    public <V> ProcessChain<V> addFunctionInPlatformThread(Function<T, V> function) {
        return addFunction(function, ThreadType.PLATFORM);
    }

    public <V> ProcessChain<V> addFunctionInExecutor(Function<T, V> function) {
        return addFunction(function, ThreadType.EXECUTOR);
    }

    public ProcessChain<Void> addRunnable(Runnable runnable, ThreadType type) {
           return addFunction((Function<T, Void>) (e) -> {
               runnable.run();
               return null;
           }, type);
    }

    public ProcessChain<Void> addRunnableInPlatformThread(Runnable runnable) {
        return addRunnable(runnable, ThreadType.PLATFORM);
    }

    public ProcessChain<Void> addRunnableInExecutor(Runnable runnable) {
        return addRunnable(runnable, ThreadType.EXECUTOR);
    }

    public ProcessChain<Void> addConsumer(Consumer<T> consumer, ThreadType type) {
        return addFunction((Function<T, Void>) (e) -> {
            consumer.accept(e);
            return null;
        }, type);
    }

    public ProcessChain<Void> addConsumerInPlatformThread(Consumer<T> consumer) {
        return addConsumer(consumer, ThreadType.PLATFORM);
    }

    public ProcessChain<Void> addConsumerInExecutor(Consumer<T> consumer) {
        return addConsumer(consumer, ThreadType.EXECUTOR);
    }

    public <V> ProcessChain<V> addSupplierInPlatformThread(Supplier<V> supplier) {
        return addSupplier(supplier, ThreadType.PLATFORM);
    }

    public <V> ProcessChain<V> addSupplierInExecutor(Supplier<V> supplier) {
        return addSupplier(supplier, ThreadType.EXECUTOR);
    }

    public <V> ProcessChain<V> addSupplier(Supplier<V> supplier, ThreadType type) {
        return addFunction((Function<T, V>) (e) -> {
            return supplier.get();
        }, type);
    }

    @SuppressWarnings("unchecked")
    private <U, V> V execute(U inputParameter, ProcessDescription<U, V> processDescription, Executor executorService) throws InterruptedException, ExecutionException {
        if (processDescription.getThreadType().equals(ThreadType.EXECUTOR)) {
            FutureTask<V> task = new FutureTask<V>(() -> {
                return processDescription.getFunction().apply(inputParameter);
            });
            executorService.execute(task);
            return task.get();
        } else {
            return ConcurrentUtils.runCallableAndWait(() -> {
                return processDescription.getFunction().apply(inputParameter);
            });
        }
    }

    public Task<T> run() {
        Task<T> task = new Task<T>() {

            @Override
            protected T call() throws Exception {
                Object lastResult = null;
                for (ProcessDescription<?, ?> processDescription : processes) {
                    lastResult = execute(lastResult, (ProcessDescription<Object, ?>) processDescription, executorService);
                }
                return (T) lastResult;
            }

        };
        executorService.execute(task);
        return task;
    }
}
