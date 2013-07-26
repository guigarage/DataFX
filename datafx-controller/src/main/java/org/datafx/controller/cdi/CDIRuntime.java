package org.datafx.controller.cdi;

import org.datafx.controller.context.ViewContext;

public interface CDIRuntime {

    public void resolve(Object data, ViewContext viewContext);
}
