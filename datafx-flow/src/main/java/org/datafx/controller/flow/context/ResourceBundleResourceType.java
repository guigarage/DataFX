package org.datafx.controller.flow.context;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.AnnotatedControllerResourceType;

import java.util.ResourceBundle;

public class ResourceBundleResourceType implements AnnotatedControllerResourceType<FlowResourceBundle, ResourceBundle> {

    @Override
    public ResourceBundle getResource(FlowResourceBundle annotation, Class<ResourceBundle> cls, ViewContext<?> context) {
        return context.getRegisteredObject(ViewFlowContext.class).getRegisteredObject(ResourceBundle.class);
    }

    @Override
    public Class<FlowResourceBundle> getSupportedAnnotation() {
        return FlowResourceBundle.class;
    }
}
