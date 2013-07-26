package org.datafx.controller.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PreDestroy;

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
    
    public void destroy() {
    	//TODO: All managed Object should be checked for a pre destroy....
    	Object controller = getRegisteredObject("controller");
		if(controller != null) {
			for (final Method method : controller.getClass().getMethods()) {
				if (method.isAnnotationPresent(PreDestroy.class)) {
					try {
						method.invoke(controller);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
    }
}
