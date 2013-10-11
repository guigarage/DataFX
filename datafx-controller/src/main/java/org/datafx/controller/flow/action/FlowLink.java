package org.datafx.controller.flow.action;

import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.FlowView;
import org.datafx.controller.util.FxmlLoadException;

public class FlowLink<T> implements FlowAction {

    private Class<T> controllerClass;

    public FlowLink(Class<T> controllerClass) {
        this.controllerClass = controllerClass;
    }
	
	@Override
	public void handle(FlowHandler flowHandler, String actionId)
			throws FlowException {
		try {
			ViewContext<T> viewContext = ViewFactory.getInstance().createByControllerInViewFlow(controllerClass, flowHandler.getFlowContext(), null, flowHandler);
            flowHandler.setNewView(new FlowView<T>(viewContext));
        } catch (FxmlLoadException e) {
            throw new FlowException(e);
        }
	}

}
