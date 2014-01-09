package org.datafx.ejb;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.AnnotatedControllerResourceType;

import javax.naming.NamingException;

public class EjbControllerResourceType<T> implements AnnotatedControllerResourceType<RemoteEjb, T> {

    private EjbLookupFactory factory;

    public EjbControllerResourceType() {
        factory = new EjbLookupFactory();
    }

    @Override
    public T getResource(RemoteEjb annotation, Class<T> resourceClass, ViewContext<?> context) {
        EjbLookup lookup = factory.get(annotation.value());
        try {
            if(annotation.jndiPath() != null && !annotation.jndiPath().isEmpty()) {
                return lookup.lookupByJndi(annotation.jndiPath());
            }
            return lookup.lookup(resourceClass);
        } catch (NamingException e) {
            //TODO: Exception Handling
            throw new RuntimeException("EJB Injection Error", e);
        }
    }

    @Override
    public Class<RemoteEjb> getSupportedAnnotation() {
        return RemoteEjb.class;
    }
}
