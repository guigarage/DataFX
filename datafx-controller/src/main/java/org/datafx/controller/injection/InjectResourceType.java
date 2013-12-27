package org.datafx.controller.injection;

import org.datafx.controller.context.ApplicationContext;
import org.datafx.controller.context.FXMLApplicationContext;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.resource.AnnotatedControllerResourceType;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;

public class InjectResourceType implements AnnotatedControllerResourceType<Inject, Object> {

    @Override
    public Object getResource(Inject annotation, Class<Object> cls, ViewContext<?> context) {
        try {
            return new InjectionHandler(context).createProxy(cls);
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    @Override
    public Class<Inject> getSupportedAnnotation() {
        return Inject.class;
    }
}
