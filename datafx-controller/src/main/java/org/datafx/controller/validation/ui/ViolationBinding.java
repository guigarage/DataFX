package org.datafx.controller.validation.ui;

public @interface ViolationBinding {

    String path();

    boolean useSubPaths() default false;
}
