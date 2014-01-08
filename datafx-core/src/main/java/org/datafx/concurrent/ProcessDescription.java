package org.datafx.concurrent;

import java.util.function.Function;

public class ProcessDescription<V, T> {

    private Function<V, T> function;

    private ThreadType threadType;

    public ProcessDescription(Function<V, T> function, ThreadType threadType) {
        this.function = function;
        this.threadType = threadType;
    }

    public Function<V, T> getFunction() {
        return function;
    }

    public ThreadType getThreadType() {
        return threadType;
    }
}