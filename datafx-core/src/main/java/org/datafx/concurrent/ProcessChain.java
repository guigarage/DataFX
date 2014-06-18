package org.datafx.concurrent;

import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *  The class defines a chain of processes. All processes will be running in a queue and the result of a process will be
 *  used as the input parameter for the next process. A process can run in the JavaFX Application Thread or as a
 *  background task. A {@code Executor} will handle all background tasks. Each task can have a input and a output
 *  value to pass parameters to the following tasks.
 * @param <T>  Return value of the chain.
 *
 * @author Hendrik Ebbers
 */
public class ProcessChain<T> {

    private List<ProcessDescription<?, ?>> processes;
    private Executor executorService;

    /**
     * Creates a new ProcessChain that uses the default {@code ObservableExecutor} as executor for all background tasks
     */
    public ProcessChain() {
        this(ObservableExecutor.getDefaultInstance(), null);
    }

    /**
     * Creates a new ProcessChain that uses the given executor for all background tasks
     * @param executorService the internal executor
     */
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

    /**
     * Creates a ProcessChain that uses the default {@code ObservableExecutor} as executor for all background tasks
     * @return a ProcessChain
     */
    public static ProcessChain<Void> create() {
        return new ProcessChain<>();
    }

    /**
     * Creates a new ProcessChain that uses the given executor for all background tasks
     * @param executorService the internal executor
     * @return a new ProcessChain
     */
    public static ProcessChain<Void> create(Executor executorService) {
        return new ProcessChain<>(executorService);
    }

    /**
     * Adds a new task to the process chain. A task is defined as a function because it can have a input and output
     * paramater. The input parameter will be the output parameter of the previous task and the output value of this
     * task will be the input value of a following task. The given task can be run in the JavaFX Application Thread or
     * a background thread.
     * @param function The function that defines the task
     * @param type Defines if the task will be run on the JavaFX Application Thread or in background
     * @param <V> Type of the output value of the task
     * @return The new processchain that is a composition of the current chain and the new task.
     */
    public <V> ProcessChain<V> addFunction(Function<T, V> function, ThreadType type) {
        processes.add(new ProcessDescription<T, V>(function, type));
        return new ProcessChain<V>(executorService, processes);
    }

    /**
     *  Adds a new task to the process chain. A task is defined as a function because it can have a input and output
     * paramater. The input parameter will be the output parameter of the previous task and the output value of this
     * task will be the input value of a following task. The given task will be run in the JavaFX Application Thread
     * @param function The function that defines the task
     * @param <V> Type of the output value of the task
     * @return The new processchain that is a composition of the current chain and the new task.
     */
    public <V> ProcessChain<V> addFunctionInPlatformThread(Function<T, V> function) {
        return addFunction(function, ThreadType.PLATFORM);
    }

    /**
     * Adds a new task to the process chain. A task is defined as a function because it can have a input and output
     * paramater. The input parameter will be the output parameter of the previous task and the output value of this
     * task will be the input value of a following task. The given task will be run in background
     * @param function The function that defines the task
     * @param <V> Type of the output value of the task
     * @return The new processchain that is a composition of the current chain and the new task.
     */
    public <V> ProcessChain<V> addFunctionInExecutor(Function<T, V> function) {
        return addFunction(function, ThreadType.EXECUTOR);
    }

    /**
     * Adds a new task to the process chain. The task is defined as a {@link Runnable} and therefore it can't handle any
     * input parameter and it's output value will be of type {@link Void}.  The given task can be run in the JavaFX
     * Application Thread or a background thread.
     * @param runnable defines the task
     * @param type Defines if the task will be run on the JavaFX Application Thread or in background
     * @return The new processchain that is a composition of the current chain and the new task.
     */
    public ProcessChain<Void> addRunnable(Runnable runnable, ThreadType type) {
           return addFunction((Function<T, Void>) (e) -> {
               runnable.run();
               return null;
           }, type);
    }

    /**
     * Adds a new task to the process chain. The task is defined as a {@link Runnable} and therefore it can't handle any
     * input parameter and it's output value will be of type {@link Void}.  The given task can be run in the JavaFX
     * Application Thread.
     * @param runnable defines the task
     * @return The new processchain that is a composition of the current chain and the new task.
     */
    public ProcessChain<Void> addRunnableInPlatformThread(Runnable runnable) {
        return addRunnable(runnable, ThreadType.PLATFORM);
    }

    /**
     *  Adds a new task to the process chain. The task is defined as a {@link Runnable} and therefore it can't handle any
     * input parameter and it's output value will be of type {@link Void}.  The given task can be run in background.
     * @param runnable defines the task
     * @return The new processchain that is a composition of the current chain and the new task.
     */
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

    public Task<T> repeatInfinite() {
        return repeat(Integer.MAX_VALUE);
    }

    public Task<T> repeatInfinite(Duration pauseTime) {
        return repeat(Integer.MAX_VALUE, pauseTime);
    }

    public Task<T> repeat(int count) {
        return repeat(count, Duration.ZERO);
    }

    public Task<T> repeat(int count, Duration pauseTime) {
        Task<T> task = new Task<T>() {

            @Override
            protected T call() throws Exception {
                Object lastResult = null;
                if(count == Integer.MAX_VALUE) {
                    while(true) {
                        lastResult = null;
                        for (ProcessDescription<?, ?> processDescription : processes) {
                            lastResult = execute(lastResult, (ProcessDescription<Object, ?>) processDescription, executorService);
                        }
                        Thread.sleep((long) pauseTime.toMillis());
                    }
                } else {
                    for(int i = 0; i < count; i++) {
                        lastResult = null;
                        for (ProcessDescription<?, ?> processDescription : processes) {
                            lastResult = execute(lastResult, (ProcessDescription<Object, ?>) processDescription, executorService);
                        }
                        Thread.sleep((long) pauseTime.toMillis());
                    }
                }
                return (T) lastResult;
            }

        };
        executorService.execute(task);
        return task;
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
