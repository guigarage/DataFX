package org.datafx.controller.injection.provider;

import org.datafx.controller.context.ApplicationContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.injection.ApplicationScoped;
import org.datafx.controller.injection.ViewScoped;

public class ViewContextProvider implements ContextProvider<ViewScoped, ViewContext>{

    @Override
    public Class<ViewScoped> supportedAnnotation() {
        return ViewScoped.class;
    }

    @Override
    public ViewContext getContext(ViewContext viewContext) {
        return viewContext;
    }
}
