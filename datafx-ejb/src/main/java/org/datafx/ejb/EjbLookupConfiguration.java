package org.datafx.ejb;

import java.util.Hashtable;

public class EjbLookupConfiguration {

	private String contextId;
	
	@SuppressWarnings("rawtypes")
	private Hashtable contextEnvironment;

	public EjbLookupConfiguration() {
		this(null);	
	}
	
	@SuppressWarnings("rawtypes")
	public EjbLookupConfiguration(String contextId) {
		this.contextId = contextId;	
		contextEnvironment = new Hashtable();
	}
	
	public String getContextId() {
		return contextId;
	}

	public Hashtable<?, ?> getContextEnvironment() {
		return contextEnvironment;
	}
	
	@SuppressWarnings("unchecked")
	public void addToEnvironment(Object key, Object value) {
		contextEnvironment.put(key, value);
	}

    protected void addToEnvironment(Hashtable<?, ?> contextEnvironment) {
        for (Object key : contextEnvironment.keySet()) {
            addToEnvironment(key, contextEnvironment.get(key));
        }
    }
	
	public EjbLookup createLookup() {
		return new EjbLookup(this);
	}
}
