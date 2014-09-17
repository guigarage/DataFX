package io.datafx.featuretoggle;

import io.datafx.controller.context.ViewContext;
import io.datafx.controller.context.resource.ControllerResourceConsumer;
import javafx.scene.Node;

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
