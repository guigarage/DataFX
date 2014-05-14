package org.datafx.samples.inheritance;

import org.datafx.controller.flow.context.ActionHandler;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.FlowActionHandler;
import org.datafx.controller.flow.context.ViewFlowContext;

import javax.inject.Inject;
import java.util.Date;

public abstract class AbstractController {

    @FXMLViewFlowContext
    protected ViewFlowContext context;

    @ActionHandler
    protected FlowActionHandler actionHandler;

    @Inject
    private Date date;

    public ViewFlowContext getContext() {
        return context;
    }

    public FlowActionHandler getActionHandler() {
        return actionHandler;
    }

    public Date getDate() {
        return date;
    }
}
