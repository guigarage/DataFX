package org.datafx.samples.masterdetail.action;

import org.datafx.controller.context.FXMLViewContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.samples.masterdetail.data.DataModel;

public class DeleteAction implements Runnable {

	@FXMLViewContext
    private ViewContext context;
	
    @Override public void run() {
    	DataModel model = context.getViewFlowContext().getRegisteredObject(DataModel.class);
        model.deleteSelected();
    }

}
