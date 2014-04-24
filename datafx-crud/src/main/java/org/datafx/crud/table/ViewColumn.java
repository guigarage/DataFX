package org.datafx.crud.table;

public @interface ViewColumn {

    String value() default "";

    int index() default -1;

    boolean sortable() default true;

    boolean editable() default true;

    boolean resizeable() default true;
}
