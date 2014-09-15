package io.datafx.controller.injection;

import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.injection.scopes.ApplicationScoped;
import io.datafx.controller.injection.scopes.FlowScoped;
import io.datafx.controller.injection.scopes.ViewScoped;

import javax.inject.Singleton;

public class Injector {

    private FlowHandler handler;

    public Injector(FlowHandler handler) {
        this.handler = handler;
    }

    public <S, T extends S> void inject(T object, Class<S> asClass) {
        if (asClass.isAnnotationPresent(ApplicationScoped.class) || asClass.isAnnotationPresent(Singleton.class)) {
            handler.registerInApplicationContext(asClass, object);
        } else if (asClass.isAnnotationPresent(FlowScoped.class)) {
            handler.registerInFlowContext(asClass, object);
        } else if (asClass.isAnnotationPresent(ViewScoped.class)) {
            handler.registerInViewContext(asClass, object);
        } else {
            //Dependent Scope. Nothing to do here...
        }
    }

    public <T> void inject(T object) {
        if (object.getClass().isAnnotationPresent(ApplicationScoped.class) || object.getClass().isAnnotationPresent(Singleton.class)) {
            handler.registerInApplicationContext(object);
        } else if (object.getClass().isAnnotationPresent(FlowScoped.class)) {
            handler.registerInFlowContext(object);
        } else if (object.getClass().isAnnotationPresent(ViewScoped.class)) {
            handler.registerInViewContext(object);
        } else {
            //Dependent Scope. Nothing to do here...
        }
    }
}
