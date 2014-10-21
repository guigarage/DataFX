package io.datafx.core.concurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This can be used to define methods that should be called on a background thread instead of the JavaFX
 * Application Thread. In the DataFX Flow API actions can be defined by annotations. In this case
 * the {@link io.datafx.core.concurrent.Async} annotation can be used to call the actions on a background
 * thread, for example. The DataFX message bus supports the annotation, too.
 * In addtion to methods some specific fields can be annotated by using {@link io.datafx.core.concurrent.Async}.
 * You can use a {@link java.util.function.Supplier} field for example to define a message supplier or a
 * {@link java.util.function.Consumer} field as a message consumer when working with the DataFX message bus. By
 * doing so this instances will be called on a background thread, too.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Async {
}
