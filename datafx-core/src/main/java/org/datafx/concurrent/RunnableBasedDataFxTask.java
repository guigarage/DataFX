package org.datafx.concurrent;

public class RunnableBasedDataFxTask extends DataFxTask<Void> {

    private Runnable runnable;

    public RunnableBasedDataFxTask(Runnable runnable) {
        this.runnable = runnable;
        if (this.runnable instanceof DataFxRunnable) {
            ((DataFxRunnable) this.runnable).injectStateHandler(this);
        }
    }

    @Override public Void call() throws Exception {
        runnable.run();
        return null;
    }
}
