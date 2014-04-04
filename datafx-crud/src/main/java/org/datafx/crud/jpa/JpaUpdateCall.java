package org.datafx.crud.jpa;

import org.datafx.crud.EntityWithId;

import javax.persistence.EntityManager;

public class JpaUpdateCall<S extends EntityWithId<T>, T> extends JpaCall<S, S> {

    public JpaUpdateCall(EntityManager manager) {
        super(manager);
    }

    @Override
    public S call(S entity) throws Exception {
        return getManager().merge(entity);
    }
}
