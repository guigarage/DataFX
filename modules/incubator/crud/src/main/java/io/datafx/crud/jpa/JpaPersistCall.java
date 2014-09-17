package io.datafx.crud.jpa;

import io.datafx.crud.util.EntityWithId;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.util.function.Supplier;

public class JpaPersistCall<S extends EntityWithId<T>, T> extends JpaCall<S, S> {

    public JpaPersistCall(Supplier<EntityManager> managerSupplier) {
        super(managerSupplier);
    }

    public JpaPersistCall(EntityManager manager) {
        this(() -> manager);
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
