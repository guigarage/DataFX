package org.datafx.crud.jpa;

import org.datafx.util.EntityWithId;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

public class JpaPersistCall<S extends EntityWithId<T>, T> extends JpaCall<S, S> {

    public JpaPersistCall(EntityManager manager) {
        super(manager);
    }

    @Override
    public S call(S entity) throws Exception {
        EntityTransaction transaction = getManager().getTransaction();
        try {
            transaction.begin();
            getManager().persist(entity);
            transaction.commit();
            return entity;
        } catch (PersistenceException e) {
            transaction.rollback();
            throw new PersistenceException("Rollback on entity persistence", e);
        }
    }
}
