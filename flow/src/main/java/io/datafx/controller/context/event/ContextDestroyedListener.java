package io.datafx.controller.context.event;

import io.datafx.controller.context.ViewContext;

@FunctionalInterface
public interface ContextDestroyedListener<T> {

    void contextDestroyed(ViewContext<T> context);
}
