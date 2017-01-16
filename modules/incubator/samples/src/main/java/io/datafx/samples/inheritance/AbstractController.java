package io.datafx.samples.inheritance;

import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;

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
