package org.datafx.featuretoggle;

import javafx.scene.Node;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.ControllerResourceConsumer;

public class DisableByFeatureResourceConsumer implements ControllerResourceConsumer<DisabledByFeature, Node> {

    @Override
    public void consumeResource(DisabledByFeature annotation, Node resource, ViewContext<?> context) {
        FeatureHandler.getInstance().disableByFeature(resource, annotation.value());
    }

    @Override
    public Class<DisabledByFeature> getSupportedAnnotation() {
        return DisabledByFeature.class;
    }
}
