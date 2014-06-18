package org.datafx.concurrent;

import java.util.function.Function;

/**
 * This class defines the description of a process. The process can be used as part of a process chain
 * @param <V> Input parameter type of the progress
 * @param <T> Return parameter type of the progress
 *
 * @see org.datafx.concurrent.ProcessChain
 */
public class ProcessDescription<V, T> {

    private Function<V, T> function;

    private ThreadType threadType;

    /**
     * Creates a new {@link ProcessDescription} with the given function and thread type. The functions defines what
     * this process will do at runtime. The function will be called when the process will be executed. The thread type
     * defines if the process should be executed om the JavaFX Platform Thread or on a background thread.
     * @param function defines what this process will do
     * @param threadType defines if the process should be executed om the JavaFX Platform Thread or on a background thread
     */
    public ProcessDescription(Function<V, T> function, ThreadType threadType) {
        this.function = function;
        this.threadType = threadType;
    }

    /**
     * Returns the function of the process.
     * @return the function
     */
    public Function<V, T> getFunction() {
        return function;
    }

    /**
     * Returns the thread type of the process
     * @return  the thread type
     */
    public ThreadType getThreadType() {
        return threadType;
    }
}