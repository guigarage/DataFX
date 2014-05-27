package org.datafx.controller.flow.action;

import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.util.Factory;

public abstract class AbstractFlowTaskAction implements FlowAction {

    private Class<? extends Runnable> runnableClass;

    private Factory<FlowHandler, Runnable> runnableFactory;

    public AbstractFlowTaskAction(Class<? extends Runnable> runnableClass) {
        this.runnableFactory = (f) -> f.getCurrentViewContext().getResolver().createInstanceWithInjections(runnableClass);
    }

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

    protected abstract void execute(Runnable r) throws Exception;

}
