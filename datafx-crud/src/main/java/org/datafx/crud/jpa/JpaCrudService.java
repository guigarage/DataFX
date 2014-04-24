package org.datafx.crud.jpa;

import org.datafx.util.EntityWithId;
import org.datafx.crud.BasicCrudService;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class JpaCrudService<S extends EntityWithId<T>, T extends Serializable> extends BasicCrudService<S, T> {

    private EntityManager entityManager;

    public JpaCrudService(EntityManager entityManager, Class<S> entityClass) {
        super(new JpaGetAllCall<S, T>(entityManager),
                new JpaGetByIdCall<S, T>(entityManager, entityClass),
                new JpaDeleteCall<S, T>(entityManager, entityClass),
                new JpaPersistCall<S, T>(entityManager),
                new JpaUpdateCall<S, T>(entityManager));
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
