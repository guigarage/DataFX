package org.datafx.controller.flow;

import org.datafx.controller.ViewFlowContext;


public interface FXMLFlowNode {

    public FXMLFlowView handle(FXMLFlowView flowView, ViewFlowContext flowContext, FXMLFlowHandler flowHandler) throws FXMLFlowException;
}
