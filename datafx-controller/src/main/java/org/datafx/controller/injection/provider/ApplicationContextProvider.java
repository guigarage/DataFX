package org.datafx.controller.injection.provider;

import org.datafx.controller.context.ApplicationContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.injection.ApplicationScoped;

public class ApplicationContextProvider implements ContextProvider<ApplicationScoped, ApplicationContext>{

    @Override
    public Class<ApplicationScoped> supportedAnnotation() {
        return ApplicationScoped.class;
    }

    @Override
    public ApplicationContext getContext(ViewContext viewContext) {
        return viewContext.getApplicationContext();
    }
}
