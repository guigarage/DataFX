package org.datafx.concurrent;

/**
 * Helper class that can be used to easily create a JavaFX service from a runnable. The class extends the
 * {@link javafx.concurrent.Task} class. By doing so it can be used as a basic JavaFX task. Instances of the class internally
 * hold a Runnable that will be executed. By doing so developers doesn't need to create an implementation of the Task
 * class if the want to execute a simple runnable in JavaFX or DataFX context. Another benefit is the use / support of
 * Lambdas. The Task class isn't a functional interface and therefore you can't use Lambdas. By using a Runnable and
 * wrap it in a RunnableBasedDataFxTask instance a developer can use Lambda expressions.
 * <p/>
 * If the internal Runnable is a {@link DataFxRunnable} the {@link TaskStateHandler} class will be supported and a handler will be
 * injected in the {@link DataFxRunnable} instance. Therefore developers can define title, message, ... for the instance.
 */
public class RunnableBasedDataFxTask extends DataFxTask<Void> {

    private Runnable runnable;

    public RunnableBasedDataFxTask(Runnable runnable) {
        this.runnable = runnable;
        if (this.runnable instanceof DataFxRunnable) {
            ((DataFxRunnable) this.runnable).injectStateHandler(this);
        }
    }

    @Override
    public Void call() throws Exception {
        runnable.run();
        return null;
    }
}
