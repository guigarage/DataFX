package org.datafx.concurrent;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    public <V> ProcessChain<V> inPlatformThread(Function<T, V> function) {
        processes.add(new ProcessDescription<T, V>(function, ThreadType.PLATFORM));
        return new ProcessChain<V>(executorService, processes);
    }

    public <V> ProcessChain<V> inExecutor(Function<T, V> function) {
        processes.add(new ProcessDescription<T, V>(function, ThreadType.EXECUTOR));
        return new ProcessChain<V>(executorService, processes);
    }

    public ProcessChain<Void> inPlatformThread(Runnable runnable) {
        return inPlatformThread((Function<T, Void>) (e) -> {
            runnable.run();
            return null;
        });
    }

    public ProcessChain<Void> inExecutor(Runnable runnable) {
        return inExecutor((Function<T, Void>) (e) -> {
            runnable.run();
            return null;
        });
    }

    public ProcessChain<Void> inPlatformThread(Consumer<T> consumer) {
        return inPlatformThread((Function<T, Void>) (e) -> {
            consumer.accept(e);
            return null;
        });
    }

    public ProcessChain<Void> inExecutor(Consumer<T> consumer) {
        return inExecutor((Function<T, Void>) (e) -> {
            consumer.accept(e);
            return null;
        });
    }

    public <V> ProcessChain<V> inPlatformThread(Supplier<V> supplier) {
        return inPlatformThread((Function<T, V>) (e) -> {
            return supplier.get();
        });
    }

    public <V> ProcessChain<V> inExecutor(Supplier<V> supplier) {
        return inExecutor((Function<T, V>) (e) -> {
            return supplier.get();
        });
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
            return ConcurrentUtils.runAndWait(() -> {
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
