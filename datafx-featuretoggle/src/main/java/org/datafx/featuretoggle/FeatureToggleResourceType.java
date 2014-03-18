package org.datafx.featuretoggle;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.AnnotatedControllerResourceType;

public class FeatureToggleResourceType implements AnnotatedControllerResourceType<FeatureToggle, FeatureProperty<?>> {

    @Override
    public FeatureProperty<?> getResource(FeatureToggle annotation, Class<FeatureProperty<?>> resourceClass, ViewContext<?> context) {
        return FeatureHandler.getInstance().createFeatureProperty(annotation.value());
    }

    @Override
    public Class<FeatureToggle> getSupportedAnnotation() {
        return FeatureToggle.class;
    }
}
