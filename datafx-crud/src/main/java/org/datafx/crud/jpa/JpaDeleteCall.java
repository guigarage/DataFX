package org.datafx.crud.jpa;

import org.datafx.util.EntityWithId;

import javax.persistence.EntityManager;

public class JpaDeleteCall<S extends EntityWithId<T>, T> extends JpaCall<T, Void> {

    private Class<S> entityClass;

    public JpaDeleteCall(EntityManager manager, Class<S> entityClass) {
        super(manager);
        this.entityClass = entityClass;
    }

    @Override
    public Void call(T id) throws Exception {
        S entity = getManager().find(entityClass, id);
        getManager().remove(entity);
        return null;
    }
}
