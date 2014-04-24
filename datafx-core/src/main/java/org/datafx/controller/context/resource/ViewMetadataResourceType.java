package org.datafx.controller.context.resource;

import org.datafx.controller.context.Metadata;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.ViewMetadata;

public class ViewMetadataResourceType implements AnnotatedControllerResourceType<Metadata, ViewMetadata>{


    @Override
    public ViewMetadata getResource(Metadata annotation, Class<ViewMetadata> resourceClass, ViewContext<?> context) {
        return context.getMetadata();
    }

    @Override
    public Class<Metadata> getSupportedAnnotation() {
        return Metadata.class;
    }
}
