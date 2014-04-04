package org.datafx.crud.jpa;

import org.datafx.crud.EntityWithId;

import javax.persistence.EntityManager;

public class JpaGetByIdCall<S extends EntityWithId<T>, T> extends JpaCall<T, S> {

    private Class<S> entityClass;

    public JpaGetByIdCall(EntityManager manager, Class<S> entityClass) {
        super(manager);
        this.entityClass = entityClass;
    }

    @Override
    public S call(T id) throws Exception {
        return getManager().find(entityClass, id);
    }
}
