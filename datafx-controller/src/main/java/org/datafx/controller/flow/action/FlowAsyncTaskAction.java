package org.datafx.controller.flow.action;

import java.util.concurrent.Executor;

import org.datafx.concurrent.ObservableExecutor;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;

public class FlowAsyncTaskAction implements FlowAction {

    private Class<? extends Runnable> runnableClass;
    
    private Executor executorService;
    
    public FlowAsyncTaskAction(Class<? extends Runnable> runnableClass) {
       this(runnableClass, new ObservableExecutor());
    }
	
    public FlowAsyncTaskAction(Class<? extends Runnable> runnableClass, Executor executorService) {
        this.runnableClass = runnableClass;
        this.executorService = executorService;
    }
    
	@Override
	public void handle(FlowHandler flowHandler, String actionId)
			throws FlowException {
		try {
			Runnable runnable = flowHandler.getCurrentViewContext().getResolver().createInstanceWithInjections(runnableClass);
			executorService.execute(runnable);
		} catch (Exception e) {
			throw new FlowException(e);
		}
	}

}
