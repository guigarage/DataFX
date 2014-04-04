package org.datafx.crud.jpa;

import org.datafx.crud.EntityWithId;

import javax.persistence.EntityManager;

public class JpaPersistCall<S extends EntityWithId<T>, T> extends JpaCall<S, S> {

    public JpaPersistCall(EntityManager manager) {
        super(manager);
    }

    @Override
    public S call(S entity) throws Exception {
        getManager().persist(entity);
        return entity;
    }
}
