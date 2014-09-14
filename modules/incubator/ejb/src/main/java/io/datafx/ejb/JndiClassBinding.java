package io.datafx.ejb;

public class JndiClassBinding<T> {
	
    private Class<T> boundClass;

    private String jndiPath;

    public JndiClassBinding(String jndiPath, Class<T> boundClass) {
        this.jndiPath = jndiPath;
        this.boundClass = boundClass;
    }

    public String getJndiPath() {
        return jndiPath;
    }

    public Class<T> getBoundClass() {
        return boundClass;
    }
}
