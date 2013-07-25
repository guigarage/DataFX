package org.datafx.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.sun.javafx.WeakReferenceQueue;

public class ViewFlowContext {

    private String id = UUID.randomUUID().toString();

    private ViewContext currentViewContext;
    
    private Map<String, Object> registeredObjects;
    
    public ViewFlowContext() {
        registeredObjects = new HashMap<>();
    }
    
    public String getId() {
        return id;
    }
    
    public void setCurrentViewContext(ViewContext currentViewContext) {
		this.currentViewContext = currentViewContext;
	}
    
    public ViewContext getCurrentViewContext() {
		return currentViewContext;
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
