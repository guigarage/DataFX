package org.datafx.crud.jpa;

import org.datafx.util.EntityWithId;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

public class JpaDeleteCall<S extends EntityWithId<T>, T> extends JpaCall<T, Void> {

    private Class<S> entityClass;

    public JpaDeleteCall(EntityManager manager, Class<S> entityClass) {
        super(manager);
        this.entityClass = entityClass;
    }

    @Override
    public Void call(T id) throws Exception {
        EntityTransaction transaction = getManager().getTransaction();
        try {
            transaction.begin();
            S entity = getManager().find(entityClass, id);
            getManager().remove(entity);
            transaction.commit();
            return null;
        } catch (PersistenceException e) {
            transaction.rollback();
            throw new PersistenceException("Rollback on entity delete", e);
        }
    }
}
