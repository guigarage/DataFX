package org.datafx.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ApplicationContext {
    
    private static ApplicationContext instance;
    
    private String id = UUID.randomUUID().toString();
    
    private Map<String, Object> registeredObjects;
    
    private ApplicationContext() {
        registeredObjects = new HashMap<>();
    }
    
    public String getId() {
        return id;
    }
    
    public void register(String key, Object value) {
        registeredObjects.put(key, value);
    }
    
    public Object getRegisteredObject(String key) {
        return registeredObjects.get(key);
    }
    
    @SuppressWarnings("unchecked")
	public <T> T getRegisteredObject(Class<T> cls) {
        return (T) registeredObjects.get(cls.toString());
    }
    
    public void register(Object value) {
        registeredObjects.put(value.getClass().toString(), value);
    }
    
    public static synchronized ApplicationContext getInstance() {
        if(instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }
}
