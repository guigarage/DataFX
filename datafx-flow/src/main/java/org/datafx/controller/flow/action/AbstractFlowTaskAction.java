package org.datafx.controller.flow.action;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;

/**
 * Basic class for {@link FlowAction} implementations that execute a custom task. In this case a task is always defined
 * as a {@link Runnable}.
 */
public abstract class AbstractFlowTaskAction implements FlowAction {

    private Class<? extends Runnable> runnableClass;

    private TaskFactory runnableFactory;

    /**
     * Defines a new {@link AbstractFlowTaskAction} instance that task is defined by a class that extends
     * {@link Runnable}. Whenever a action is triggered a new instance of the given class will be created. Therefore the
     * class needs a default constructor.  All injection that is working in a controller class will work in the given
     * class that defines the task, too.
     * @param runnableClass the class that defines the task
     */
    public AbstractFlowTaskAction(Class<? extends Runnable> runnableClass) {
        this.runnableFactory = (f) -> f.getCurrentViewContext().getResolver().createInstanceWithInjections(runnableClass);
    }

    /**
     * Defines a new {@link AbstractFlowTaskAction} instance that task is defined by a {@link Runnable} instance.
     * @param runnable defines the task and will be called whenever the action is triggered.
     */
    public AbstractFlowTaskAction(Runnable runnable) {
        this.runnableFactory = (f) -> runnable;
    }

    @Override
    public void handle(FlowHandler flowHandler, String actionId)
            throws FlowException {
        try {
            Runnable runnable = runnableFactory.create(flowHandler);
            execute(runnable);
        } catch (Exception e) {
            throw new FlowException(e);
        }
    }

    /**
     * Executes the defined {@link Runnable}
     * @param r the runnable
     * @throws Exception if the runnable can't be executed
     */
    protected abstract void execute(Runnable r) throws Exception;

    @FunctionalInterface
    public interface TaskFactory {

        Runnable create(FlowHandler flowHandler) throws Exception;
    }

}
