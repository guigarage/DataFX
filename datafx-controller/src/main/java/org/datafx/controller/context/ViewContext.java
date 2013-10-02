/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.controller.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PreDestroy;

import javafx.scene.Node;

public class ViewContext<U> {

    private ViewFlowContext viewFlowContext;
    
    private Node rootNode;
    
    private String id = UUID.randomUUID().toString();
    
    private Map<String, Object> registeredObjects;
    
    private U controller;
    
    public ViewContext(Node rootNode, U controller) {
        this(rootNode, new ViewFlowContext(), controller);
    }
    
    public ViewContext(Node rootNode, ViewFlowContext viewFlowContext, U controller) {
        this.viewFlowContext = viewFlowContext;
        this.rootNode = rootNode;
        registeredObjects = new HashMap<>();
        this.controller = controller;
    }
    
    public U getController() {
		return controller;
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
