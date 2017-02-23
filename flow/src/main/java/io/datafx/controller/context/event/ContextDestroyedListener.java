package io.datafx.controller.context.event;

import io.datafx.controller.context.ViewContext;

/**
 * Created by hendrikebbers on 12.10.14.
 */
public interface ContextDestroyedListener<T> {

    void contextDestroyed(ViewContext<T> context);
}
