package org.datafx.concurrent;

import java.util.concurrent.Callable;

public class CallableBasedDataFxTask<V> extends DataFxTask<V> {
    private Callable<V> callable;

    public CallableBasedDataFxTask(Callable<V> callable) {
        this.callable = callable;
        if (this.callable instanceof DataFxCallable) {
            ((DataFxCallable<V>) this.callable).injectStateHandler(this);
        }
    }

    @Override public V call() throws Exception {
        return callable.call();
    }
}