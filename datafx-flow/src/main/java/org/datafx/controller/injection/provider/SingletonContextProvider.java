package org.datafx.controller.injection.provider;

import org.datafx.controller.context.ApplicationContext;
import org.datafx.controller.context.ViewContext;

import javax.inject.Singleton;

public class SingletonContextProvider implements ContextProvider<Singleton, ApplicationContext>{

    @Override
    public Class<Singleton> supportedAnnotation() {
        return Singleton.class;
    }

    @Override
    public ApplicationContext getContext(ViewContext viewContext) {
        return viewContext.getApplicationContext();
    }
}
