package org.datafx.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javafx.scene.Node;

public class ViewContext {

    private ViewFlowContext viewFlowContext;
    
    private Node rootNode;
    
    private String id = UUID.randomUUID().toString();
    
    private Map<String, Object> registeredObjects;
    
    public ViewContext(Node rootNode) {
        this(rootNode, new ViewFlowContext());
    }
    
    public ViewContext(Node rootNode, ViewFlowContext viewFlowContext) {
        this.viewFlowContext = viewFlowContext;
        this.rootNode = rootNode;
        viewFlowContext.registerContext(this);
        registeredObjects = new HashMap<>();
    }
    
    public String getId() {
        return id;
    }
    
    public Node getRootNode() {
        return rootNode;
    }
    
    public ViewFlowContext getViewFlowContext() {
        return viewFlowContext;
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
    
    public ApplicationContext getApplicationContext() {
        return ApplicationContext.getInstance();
    }
}
