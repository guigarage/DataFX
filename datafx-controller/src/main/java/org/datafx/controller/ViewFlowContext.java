package org.datafx.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.sun.javafx.WeakReferenceQueue;

public class ViewFlowContext {

    private String id = UUID.randomUUID().toString();

    private WeakReferenceQueue<ViewContext> registeredContexts;
    
    private Map<String, Object> registeredObjects;
    
    public ViewFlowContext() {
        registeredContexts = new WeakReferenceQueue<>();
        registeredObjects = new HashMap<>();
    }
    
    public String getId() {
        return id;
    }
    
    protected void registerContext(ViewContext context) {
        registeredContexts.add(context);
    }
    
    @SuppressWarnings("unchecked")
    public Iterator<ViewContext> getViewContextIterator() {
        return (Iterator<ViewContext>) registeredContexts.iterator();
    }
    
    public void register(String key, Object value) {
        registeredObjects.put(key, value);
    }
    
    public Object getRegisteredObject(String key) {
        return registeredObjects.get(key);
    }
    
    @SuppressWarnings("unchecked")
	public <T> T getRegisteredObject(Class<T> cls) {
        return (T) getRegisteredObject(cls.toString());
    }
    
    public void register(Object value) {
        registeredObjects.put(value.getClass().toString(), value);
    }
    
    public ApplicationContext getApplicationContext() {
        return ApplicationContext.getInstance();
    }
}
