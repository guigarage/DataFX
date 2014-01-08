package org.datafx.controller.context.resource;

import org.datafx.concurrent.ObservableExecutor;
import org.datafx.controller.context.ConcurrencyProvider;
import org.datafx.controller.context.ViewContext;

public class ConcurrencyProviderResourceType implements AnnotatedControllerResourceType<ConcurrencyProvider, ObservableExecutor>{

    @Override
    public ObservableExecutor getResource(ConcurrencyProvider annotation, Class<ObservableExecutor> cls, ViewContext<?> context) {
        return ObservableExecutor.getDefaultInstance();
    }

    @Override
    public Class<ConcurrencyProvider> getSupportedAnnotation() {
        return ConcurrencyProvider.class;
    }
}
