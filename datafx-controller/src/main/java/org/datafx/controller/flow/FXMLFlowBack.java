package org.datafx.controller.flow;

import org.datafx.controller.ViewFlowContext;

public class FXMLFlowBack implements FXMLFlowNode {

    private FXMLFlowView currentView;
    
    public FXMLFlowBack(FXMLFlowView currentView) {
        this.currentView = currentView;
    }
    
    @Override public FXMLFlowView handle(FXMLFlowView currentViewContext, ViewFlowContext flowContext) throws FXMLFlowException {
        return currentView.getPreviousViewContext();
    }

}
