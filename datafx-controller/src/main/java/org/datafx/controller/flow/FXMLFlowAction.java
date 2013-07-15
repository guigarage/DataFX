package org.datafx.controller.flow;

import org.datafx.controller.ViewFlowContext;


public class FXMLFlowAction implements FXMLFlowNode {

    private Runnable runnable;
    
    public FXMLFlowAction(Runnable runnable) {
        this.runnable = runnable;
    }
    
    public Runnable getRunnable() {
        return runnable;
    }

    @Override public FXMLFlowView handle(FXMLFlowView currentViewContext, ViewFlowContext flowContext) {
        runnable.run();
        return currentViewContext;
    }
}
