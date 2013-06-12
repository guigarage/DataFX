package org.datafx.writer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author johan
 */

@Target(value=ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteTransient {
    
}
