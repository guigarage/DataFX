package io.datafx.crud.jpa;

import io.datafx.crud.BasicCrudService;
import io.datafx.crud.util.EntityWithId;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.function.Supplier;

public class JpaCrudService<S extends EntityWithId<T>, T extends Serializable> extends BasicCrudService<S, T> {

    private CreateOnceSupplier<EntityManager> entityManagerSupplier;

    private EntityManager createdManager;

    public JpaCrudService(EntityManager entityManager, Class<S> entityClass) {
        this(() -> entityManager, entityClass);
    }

    public JpaCrudService(Supplier<EntityManager> entityManagerSupplier, Class<S> entityClass) {
       this(new CreateOnceSupplier<EntityManager>(entityManagerSupplier), entityClass);
    }

    public JpaCrudService(CreateOnceSupplier<EntityManager> entityManagerSupplier, Class<S> entityClass) {
        super(new JpaGetAllCall<S, T>(entityManagerSupplier, entityClass),
                new JpaGetByIdCall<S, T>(entityManagerSupplier, entityClass),
                new JpaDeleteCall<S, T>(entityManagerSupplier, entityClass),
                new JpaPersistCall<S, T>(entityManagerSupplier),
                new JpaUpdateCall<S, T>(entityManagerSupplier));
        this.entityManagerSupplier = entityManagerSupplier;
    }

    public EntityManager getEntityManager() {
        if(createdManager == null) {
            createdManager = entityManagerSupplier.get();
        }
        return createdManager;
    }
}
