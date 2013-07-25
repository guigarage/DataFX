package org.datafx.controller.flow;

import org.datafx.controller.ViewFactory;
import org.datafx.controller.ViewFlowContext;


public class FXMLFlowAction implements FXMLFlowNode {

    private Class<? extends Runnable> runnableClass;
    
    public FXMLFlowAction(Class<? extends Runnable> runnableClass) {
        this.runnableClass = runnableClass;
    }
    
    @Override public FXMLFlowView handle(FXMLFlowView currentFlowView, ViewFlowContext flowContext, FXMLFlowHandler flowHandler) {
		try {
			Runnable runnable = ViewFactory.getInstance().createInstanceWithInjections(runnableClass, currentFlowView.getViewContext());
			runnable.run();
	        return currentFlowView;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}
