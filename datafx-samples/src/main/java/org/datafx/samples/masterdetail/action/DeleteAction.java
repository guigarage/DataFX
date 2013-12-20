package org.datafx.samples.masterdetail.action;

import org.datafx.controller.context.FXMLViewContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.samples.masterdetail.data.DataModel;

public class DeleteAction implements Runnable {

	@FXMLViewFlowContext
    private ViewFlowContext context;
	
    @Override public void run() {
    	DataModel model = context.getRegisteredObject(DataModel.class);
        model.deleteSelected();
    }

}
