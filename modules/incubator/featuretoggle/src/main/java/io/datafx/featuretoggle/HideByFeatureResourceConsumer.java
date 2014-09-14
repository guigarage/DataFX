package io.datafx.featuretoggle;

import io.datafx.controller.context.ViewContext;
import io.datafx.controller.context.resource.ControllerResourceConsumer;
import javafx.scene.Node;

public class HideByFeatureResourceConsumer implements ControllerResourceConsumer<HideByFeature, Node> {

    @Override
    public void consumeResource(HideByFeature annotation, Node resource, ViewContext<?> context) {
        FeatureHandler.getInstance().hideByFeature(resource, annotation.value());
    }

    @Override
    public Class<HideByFeature> getSupportedAnnotation() {
        return HideByFeature.class;
    }
}
