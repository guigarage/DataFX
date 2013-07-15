package org.datafx.controller.demo.action;

import org.datafx.controller.FXMLViewContext;
import org.datafx.controller.ViewContext;
import org.datafx.controller.demo.data.DataModel;

public class DeleteAction implements Runnable {

	@FXMLViewContext
    private ViewContext context;
	
    @Override public void run() {
    	DataModel model = context.getViewFlowContext().getRegisteredObject(DataModel.class);
        model.deleteSelected();
    }

}
