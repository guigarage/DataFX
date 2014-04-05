package org.datafx.controller.context.resource;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.ViewResourceBundle;
import org.datafx.controller.context.resource.AnnotatedControllerResourceType;

import java.util.ResourceBundle;

public class ResourceBundleResourceType implements AnnotatedControllerResourceType<ViewResourceBundle, ResourceBundle> {

    @Override
    public ResourceBundle getResource(ViewResourceBundle annotation, Class<ResourceBundle> cls, ViewContext<?> context) {
        return context.getConfiguration().getResources();
    }

    @Override
    public Class<ViewResourceBundle> getSupportedAnnotation() {
        return ViewResourceBundle.class;
    }
}
