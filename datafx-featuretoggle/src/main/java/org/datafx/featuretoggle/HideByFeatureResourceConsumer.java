package org.datafx.featuretoggle;

import javafx.scene.Node;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.ControllerResourceConsumer;

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
