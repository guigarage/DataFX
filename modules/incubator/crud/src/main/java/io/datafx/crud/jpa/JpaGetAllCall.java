package io.datafx.crud.jpa;

import io.datafx.crud.util.EntityWithId;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Supplier;

public class JpaGetAllCall<S extends EntityWithId<T>, T> extends JpaCall<Void, List<S>> {

    private Class<S> entityClass;

    public JpaGetAllCall(Supplier<EntityManager> managerSupplier, Class<S> entityClass) {
        super(managerSupplier);
        this.entityClass = entityClass;
    }

    public JpaGetAllCall(EntityManager manager, Class<S> entityClass) {
        this(() -> manager, entityClass);
    }

    @Override
    public List<S> call(Void dummy) throws Exception {
        System.out.println("Select a from " + entityClass.getSimpleName() + " a");
        return getManager()
                .createQuery("Select a from " + entityClass.getSimpleName() + " a", entityClass)
                .getResultList();
    }
}
