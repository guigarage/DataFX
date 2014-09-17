package io.datafx.crud.jpa;

import io.datafx.crud.util.EntityWithId;

import javax.persistence.EntityManager;
import java.util.function.Supplier;

public class JpaGetByIdCall<S extends EntityWithId<T>, T> extends JpaCall<T, S> {

    private Class<S> entityClass;

    public JpaGetByIdCall(Supplier<EntityManager> managerSupplier, Class<S> entityClass) {
        super(managerSupplier);
        this.entityClass = entityClass;
    }

    public JpaGetByIdCall(EntityManager manager, Class<S> entityClass) {
        this(() -> manager, entityClass);
    }

    @Override
    public S call(T id) throws Exception {
        return getManager().find(entityClass, id);
    }
}
