package org.datafx.controller.flow.injection;

import javax.inject.Scope;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Scope
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlowScoped {
}
