package io.datafx.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define UI elements. The annotation can be used in a controller class and in a view class.
 * It can only be used for fields that define a {@link javafx.scene.Node} instance. Normally it will be used
 * to annotate children of the view:
 * <tt>
 *     @ViewController
 *     public class SimpleController {
 *
 *        @ViewNode
 *        private Button myButton;
 *
 *     }
 * </tt>
 *
 * By using the annotation in the view class the annotated view element (node) will get an id. If the {@link #value()} of the
 * annotation is != "" the {@link #value()} will be used. Otherwise the name of the field will be used as the id of the node.
 *
 * By using the annotation in the controller class the annotated view element (node) will be injected. To find the matching
 * node the {@link #value()} of the annotation will be used. If the {@link value} is == "" the name of the field will be
 * used as the id of the node.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ViewNode {

    String value() default "";

}
