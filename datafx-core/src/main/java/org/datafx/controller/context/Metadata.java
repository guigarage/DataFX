package org.datafx.controller.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: hendrikebbers
 * Date: 21.04.14
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Target(value = { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Metadata {
}
